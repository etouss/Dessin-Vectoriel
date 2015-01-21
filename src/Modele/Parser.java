package Modele;


import controlleur.Controlleur;
import vue.DrawPanel;
import vue.forme.Droite.DroiteParaVector;
import vue.forme.Droite.DroitePerpenVector;
import vue.forme.Point.PointEntrePoint;
import vue.forme.Point.PointInterVector;
import vue.forme.ellipse.ArcCercleVector;
import vue.forme.ellipse.CercleVector;
import vue.forme.Droite.DroiteVector;
import vue.forme.Point.PointVector;
import vue.forme.Polygone.PolygoneVector;
import vue.forme.SegmentVector;
import vue.forme.ellipse.OvalVector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Parser {
    private final String FLOAT_REGEX = "[-+]?[0-9]*[.,]?[0-9]+(?:[eE][-+]?[0-9]+)?";
    private final String POINT_REGEX = "(\\(("+ FLOAT_REGEX +") *; *("+ FLOAT_REGEX +") *; *("+ FLOAT_REGEX +")\\))";
    private final String NAME_REGEX = "[a-zA-Z]+[\\d\\w]*";

    private static Controlleur controlleur;
    private Autocompletion autocompletion;


    public Parser(Controlleur controlleur, Autocompletion a){
        autocompletion = a;
        Parser.controlleur = controlleur;


    }

    /**
     * cette foction regarde si la commande s existe
     * @param s String
     * @return boolean
     */
    private boolean commandeExist(String s){
        for (String cmd : autocompletion.getCOMANDE()){
            if (s.equalsIgnoreCase(cmd))return true;
        }
        return false;
    }

    /**
     * Construit la figure et l'ajoute au drawPanel
     * @param cmd String
     * @param arguments String []
     * @param name Sring nom de la figure
     * @throws ParserExeption
     */
    public void switchCmd(String cmd, String[] arguments, String name) throws ParserExeption {
        PointVector p;
        PointVector[] p_tab;
        BigDecimal b;
        FormeVue f = null;
        DroiteVector f2 = null,f3 = null;
        PolygoneVector po = null;
        switch (cmd){
            case "POINT":
                if (arguments.length != 3) throw new ParserExeption("error Point 3 args ex : Point [(1;2;3) : (4;5;6) : 0.2]");
                p = returnPoint(arguments[0]);
                PointVector p2 = returnPoint(arguments[1]);
                b = returnBigDecimal(arguments[2]);
                f = new PointEntrePoint(p,p2,false);
                ((PointEntrePoint)f).set_ratio(b);
                break;
            case "SEGMENT":
                if (arguments.length != 2) throw new ParserExeption("error Segment 2 args ex : Segment [(1;2;3) : (4;5;6)]");
                p_tab = liste_point(arguments);
                f = new SegmentVector(p_tab[0],controlleur.getWidth(),controlleur.getHeigth());
                f.setPoint(p_tab[1], true);
                break;
            case "POLYGONE":
                if (arguments.length < 3) throw new ParserExeption("error poly < 3 args ex polygone [(1;2;3) : (4;5;6) :(7;8;9)]");
                p_tab = liste_point(arguments);
                f = new PolygoneVector(p_tab[0],controlleur.getWidth(),controlleur.getHeigth());
                for (int i = 1;i<arguments.length; i++){
                    f.setPoint(p_tab[i], true);
                }
                ((PolygoneVector)f).close();
                break;
            case "CERCLE":
                if (arguments.length != 2) throw new ParserExeption("error Cercle != 2 args ex : cercle [(1;2;3) : 5]");
                p = returnPoint(arguments[0]);
                f = new CercleVector(p,controlleur.getWidth(),controlleur.getHeigth());
                if(isPoint(arguments[1]) || isNamed(arguments[1])){
                    p = returnPoint(arguments[1]);
                    f.setPoint(p, true);
                }
                else if(isBigDecimal(arguments[1])){
                    b = returnBigDecimal(arguments[1]);
                    ((CercleVector)f).setRayon(b, true);
                }

                break;

            case "OVAL":
                if (arguments.length != 3) throw new ParserExeption("error Oval != 2 args ex : oval [(1;2;3) : 5]");
                p = returnPoint(arguments[0]);
                f = new OvalVector(p,controlleur.getWidth(),controlleur.getHeigth());
                b = returnBigDecimal(arguments[1]);
                ((OvalVector)f).setRayon(b, true);
                b = returnBigDecimal(arguments[2]);
                ((OvalVector)f).setRayon(b, true);

                break;
            case "ARC_CERCLE":
                if (arguments.length != 4) throw new ParserExeption("error Cercle != 2 args ex : arc_cercle [(1;2;3) : 5 : 0 : 90]");
                p = returnPoint(arguments[0]);
                f = new ArcCercleVector(p,controlleur.getWidth(),controlleur.getHeigth());
                b = returnBigDecimal(arguments[1]);
                BigDecimal b2 = returnBigDecimal(arguments[2]);
                ((ArcCercleVector) f).set_p1(b, b2 );
                b = returnBigDecimal(arguments[3]);
                ((ArcCercleVector)f).set_p2(b);

                break;
            case "ARC_OVAL":
                break;
            case "DROITE":
                if (arguments.length != 2) throw new ParserExeption("error Droite != 2 args ex : droite [(1;2;3) : (4;5;6)");
                p = returnPoint(arguments[0]);
                if (isPoint(arguments[1]) || isNamed(arguments[1])){
                    f = new DroiteVector(p,controlleur.getWidth(),controlleur.getHeigth());
                    p = returnPoint(arguments[1]);
                    f.setPoint(p,true);
                } else if(isBigDecimal(arguments[1])){
                    b = returnBigDecimal(arguments[1]);
                    f = new DroiteVector(p,b,controlleur.getWidth(),controlleur.getHeigth());
                }
                else{
                    throw new ParserExeption("error Droite Args");
                }
                break;

            case "PARALLELE":
                if (arguments.length != 2) throw new ParserExeption("error PARA != 2 args");
                try {
                    f = returnForme(arguments[0]);
                    if(!(f instanceof DroiteVector)){
                        throw new ParserExeption("La forme "+arguments[0]+" n'est pas une droite");
                    }
                }catch(FormeNotFoundExeption e){
                    throw new ParserExeption("La forme "+arguments[0]+" n'existe pas");
                }
                f2 = (DroiteVector)f;
                p = returnPoint(arguments[1]);
                f = new DroiteParaVector(f2);
                f.setPoint(p.positionX,p.positionY,true);
                break;
            case "PERPENDICULAIRE":
                if (arguments.length != 2) throw new ParserExeption("error PARA != 2 args");
                try {
                    f = returnForme(arguments[0]);
                    if(!(f instanceof DroiteVector)){
                        throw new ParserExeption("La forme "+arguments[0]+" n'est pas une droite");
                    }
                }catch(FormeNotFoundExeption e){
                    throw new ParserExeption("La forme "+arguments[0]+" n'existe pas");
                }
                f2 = (DroiteVector)f;
                p = returnPoint(arguments[1]);
                f = new DroitePerpenVector(f2);
                f.setPoint(p.positionX,p.positionY,true);
                break;

            case "INTER":
                if (arguments.length != 2) throw new ParserExeption("error INTER != 2 args");
                try {
                    f = returnForme(arguments[0]);
                    if(!(f instanceof DroiteVector)){
                        throw new ParserExeption("La forme "+arguments[0]+" n'est pas une droite");
                    }
                }catch(FormeNotFoundExeption e){
                    throw new ParserExeption("La forme "+arguments[0]+" n'existe pas");
                }
                f2 = (DroiteVector)f;
                try {
                    f = returnForme(arguments[1]);
                    if(!(f instanceof DroiteVector)){
                        throw new ParserExeption("La forme "+arguments[0]+" n'est pas une droite");
                    }
                }catch(FormeNotFoundExeption e){
                    throw new ParserExeption("La forme "+arguments[0]+" n'existe pas");
                }
                f3 = (DroiteVector)f;
                try{
                    f = new PointInterVector(f2,f3);
                }catch (FormeNotFoundExeption e){
                    throw new ParserExeption("Ces droite sont PARALLELE");
                }
                break;
            case "GRAV":
                if (arguments.length != 1) throw new ParserExeption("error GRAV != 2 args");
                try {
                    f = returnForme(arguments[0]);
                    if(!(f instanceof PolygoneVector)){
                        throw new ParserExeption("La forme "+arguments[0]+" n'est pas un polygone");
                    }
                    po = (PolygoneVector)f;
                }catch(FormeNotFoundExeption e){
                    throw new ParserExeption("La forme "+arguments[0]+" n'existe pas");
                }

                f = new PointVector(po);
                break;
            default:
                throw new ParserExeption("WTF");
        }
        if(!name.equals("")){
            f.named(name);
        }
        controlleur.ajouter(f);
    }

    /**
     * Fonction pricipal de la class.
     * Elle recupere une chaine correspondant a la commande et
     * @param text
     * @throws ParserExeption
     */
    public void parseCmd(String text) throws ParserExeption {
        String spl[] = text.split(" ",2);
        if (!commandeExist(spl[0])) throw new ParserExeption("La commande n'existe pas");
        if (spl.length != 2)throw new ParserExeption("ERREUR SYNTAXE COMMANDE");
        String cmd = spl[0].toUpperCase();
        String name = "";
        String arg[] = spl[1].split("=");
        String args;
        if(arg.length == 2){
            name = arg[0].trim();
            args = arg[1].trim();
        }
        else{
            args = spl[1].trim();

        }
        if (args.startsWith("[") && args.endsWith("]")){
            args = args.substring(1,args.length()-1);
            String [] arguments = args.split(":");

            switchCmd(cmd, arguments, name);
        }
        else{
            String point[];
            point = getPoint(args);
            if (point == null) throw new ParserExeption("ERREUR SYNTAXE POINT");
            BigDecimal[] big = new BigDecimal[3];
            for (int j = 0; j<point.length;j++) {
                String ss = point[j];
                ss = ss.replaceAll(",", ".");
                try{
                    big[j] = new BigDecimal(ss);
                }catch (NumberFormatException e){
                    throw new ParserExeption("ERROR BIGDECIMAL POINT TROLOLO");
                }
            }
            PointVector p = new PointVector(big[0], big[1], controlleur.getWidth(),controlleur.getHeigth(), null,false);
            controlleur.ajouter(p);
            if(!name.equals("")){
                p.named(name);
            }
        }

        controlleur.forceUpdate();
    }

    /**
     * Retourne un tableau de PointVector Corespondant aux points String[] points
     * @param points String[] ex [(1;2;3);(4;5;6);(7;8;9)]
     * @return Poi
     *
     *
     * ntVector[]
     * @throws ParserExeption
     */
    private PointVector[] liste_point(String[] points) throws ParserExeption{
        PointVector[] p = new PointVector[points.length];
        for (int i = 0; i< points.length; i++){
            p[i] = returnPoint(points[i]);
        }
        return p;
    }

    /**
     * Retourn les 3 coordonnée du point
     * @param s String ex: (1;2;3)
     * @return String [] ex: [1;2;3]
     */
    private String[] getPoint(String s){
        Pattern p = Pattern.compile(POINT_REGEX);
        Matcher m = p.matcher(s);
        ArrayList<String> list  = new ArrayList<>();
        if(m.find()){
            for (int i = 1; i<= m.groupCount();i++){
                if(m.group(i) == null || m.group(i).startsWith("(")){
                    continue;
                }
                list.add(m.group(i));
            }
        }
        if(list.isEmpty())return null;
        return  list.toArray(new String[list.size()]);
    }

    /**
     * Renvoi true si s correspond au regex du point
     * @param s String
     * @return boolean
     */
    private boolean isPoint(String s){
        Pattern p = Pattern.compile(POINT_REGEX);
        Matcher m = p.matcher(s);
        return m.find();
    }

    /**
     * Renvoi true si s correspond au regex d'un nom de forme
     * @param s String
     * @return boolean
     */
    private boolean isNamed(String s){
        Pattern p = Pattern.compile("^"+NAME_REGEX);
        Matcher m = p.matcher(s.trim());
        return m.find();
    }

    /**
     * Renvoi true si s correspond au regex d'un nombre
     * ex 2,5e-10; 10; -5
     * @param s String
     * @return boolean
     */
    private boolean isBigDecimal(String s){
        Pattern p = Pattern.compile(FLOAT_REGEX);
        Matcher m = p.matcher(s);
        return m.find();
    }


    /**
     * Retourne le Point nommé s ou un nouveau point de coordonné s
     * @param s String
     * @return PointVector
     * @throws ParserExeption
     */
    public PointVector returnPoint(String s) throws ParserExeption{
        if (isPoint(s)){
            String point[];
            point = getPoint(s.trim());
            if (point == null) throw new ParserExeption("ERREUR SYNTAXE POINT 1");
            BigDecimal[] big = new BigDecimal[3];
            for (int j = 0; j<point.length;j++) {
                String ss = point[j];
                ss = ss.replaceAll(",", ".");
                try{
                    big[j] = new BigDecimal(ss);
                }catch (NumberFormatException e){
                    throw new ParserExeption("ERROR BIGDECIMAL POINT TROLOLOj");
                }
            }
            return new PointVector(big[0], big[1], controlleur.getWidth(),controlleur.getHeigth(), null,false);
        }
        else if(isNamed(s)){
            s= s.trim();
            String ss = DrawPanel.mapName.get(s);
            for(FormeVue p : DrawPanel.map){
                if (p instanceof PointVector){
                    if (p.hashCode() == Integer.parseInt(ss)){
                        return (PointVector)p;
                    }
                }
            }
            throw new ParserExeption("Named point NOT EXIST:" +s);
        }
        else {
            throw new ParserExeption("invalide argument :" +s);
        }
    }

    /**
     * Retourne la valeur en BigDecimal du string s
     * @param s String
     * @return BigDecimal
     * @throws ParserExeption
     */
    public BigDecimal returnBigDecimal(String s) throws ParserExeption{
        s = s.replaceAll(",", ".").trim();
        try{
            return new BigDecimal(s);
        }catch (NumberFormatException e){
            throw new ParserExeption("ERROR BIGDECIMAL POINT TROLOLOfgh");
        }

    }

    /**
     * Retourne la forme dont le nom est le string s
     * @param s String
     * @return FormeVue
     * @throws FormeNotFoundExeption
     */
    public FormeVue returnForme(String s)throws FormeNotFoundExeption{
        s= s.trim();
        String ss = DrawPanel.mapName.get(s);
        for(FormeVue p : DrawPanel.map){
            if (p.hashCode() == Integer.parseInt(ss)){
                return p;
            }
        }
        throw new FormeNotFoundExeption();
    }

}
