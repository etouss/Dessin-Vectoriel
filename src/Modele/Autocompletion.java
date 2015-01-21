package Modele;

import vue.InputArea;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class qui gère l'autocompletion des commandes dans un inputArea
 */
public class Autocompletion {
    /**
     * class des item du memu de popup l'inputArea
     */
    private class MenuItem extends JMenuItem implements ActionListener {
        private String text;
        public MenuItem(String text) {
            super(text);
            this.text = text;
            addActionListener(this);
        }
        public void actionPerformed(ActionEvent e) {
            jTextField.setText(text);
            jTextField.requestFocus();
            nextChevron(jTextField.getText());
        }
    }

    /**
     * Liste des commande possible
     */
    private final String[] COMANDE= {
            "POINT",            // 1  8
            "SEGMENT",          // 3
            "POLYGONE",         // 15
            "CERCLE",           // 3  7
            "OVAL",             // 9  10
            "ARC_CERCLE",       // 9  11
            "ARC_OVAL",         // 12 13 14
            "DROITE",           // 3  6
            "PARALLELE",        // 4
            "PERPENDICULAIRE",  // 4
            "INTER",            // 5
            "GRAV",             // 2
    };

    /**
     *
     */
    private final ArrayList<String> POSSIBLE = new ArrayList<>();

    private JPopupMenu popupMenu;
    InputArea jTextField;

    public Autocompletion(InputArea j){
        jTextField = j;
        ArrayList<String> CMD = new ArrayList<>();
        CMD.add("(<x>;<y>;<z>)");
        CMD.add("[<FIGURE>]");
        CMD.add("[<POINT> : <POINT>]");
        CMD.add("[<DROITE> : <POINT>]");
        CMD.add("[<DROITE> : <DROITE>]");
        CMD.add("[<POINT> : <COEF_DIRECTEUR>]");
        CMD.add("[<POINT> : <RAYON>]");
        CMD.add("[<POINT> : <POINT> : <PROPORTION>]");
        CMD.add("[<POINT> : <POINT> : <POINT>]");
        CMD.add("[<POINT> : <RAYON> : <RAYON>]");
        CMD.add("[<POINT> : <RAYON> : <ANGLE> : <ANGLE>]");
        CMD.add("[<POINT> : <RAYON> : <RAYON> : <ANGLE> : <ANGLE>]");
        CMD.add("[<POINT> : <POINT> : <POINT> : <ANGLE> : <ANGLE>]");
        CMD.add("[<POINT> : <POINT> : <POINT> : <POINT> : <POINT>]");
        CMD.add("[<POINT> : <...>]");


        POSSIBLE.add(COMANDE[0]+" "+ CMD.get(0));
        POSSIBLE.add(COMANDE[0]+" <name> = "+ CMD.get(0));
        POSSIBLE.add(COMANDE[0]+" "+ CMD.get(7));
        POSSIBLE.add(COMANDE[0]+" <name> = "+ CMD.get(7));
        POSSIBLE.add(COMANDE[1]+" "+ CMD.get(2));
        POSSIBLE.add(COMANDE[1]+" <name> = "+ CMD.get(2));
        POSSIBLE.add(COMANDE[2]+" "+ CMD.get(14));
        POSSIBLE.add(COMANDE[2]+" <name> = "+ CMD.get(14));
        POSSIBLE.add(COMANDE[3]+" "+ CMD.get(2));
        POSSIBLE.add(COMANDE[3]+" <name> = "+ CMD.get(2));
        POSSIBLE.add(COMANDE[3]+" "+ CMD.get(6));
        POSSIBLE.add(COMANDE[3]+" <name> = "+ CMD.get(6));
        POSSIBLE.add(COMANDE[4]+" "+ CMD.get(8));
        POSSIBLE.add(COMANDE[4]+" <name> = "+ CMD.get(8));
        POSSIBLE.add(COMANDE[4]+" "+ CMD.get(9));
        POSSIBLE.add(COMANDE[4]+" <name> = "+ CMD.get(9));
        POSSIBLE.add(COMANDE[5]+" "+ CMD.get(8));
        POSSIBLE.add(COMANDE[5]+" <name> = "+ CMD.get(8));
        POSSIBLE.add(COMANDE[5]+" "+ CMD.get(10));
        POSSIBLE.add(COMANDE[5]+" <name> = "+ CMD.get(10));
        POSSIBLE.add(COMANDE[6]+" "+ CMD.get(11));
        POSSIBLE.add(COMANDE[6]+" <name> = "+ CMD.get(11));
        POSSIBLE.add(COMANDE[6]+" "+ CMD.get(12));
        POSSIBLE.add(COMANDE[6]+" <name> = "+ CMD.get(12));
        POSSIBLE.add(COMANDE[6]+" "+ CMD.get(13));
        POSSIBLE.add(COMANDE[6]+" <name> = "+ CMD.get(13));
        POSSIBLE.add(COMANDE[7]+" "+ CMD.get(2));
        POSSIBLE.add(COMANDE[7]+" <name> = "+ CMD.get(2));
        POSSIBLE.add(COMANDE[7]+" "+ CMD.get(5));
        POSSIBLE.add(COMANDE[7]+" <name> = "+ CMD.get(5));
        POSSIBLE.add(COMANDE[8]+" "+ CMD.get(3));
        POSSIBLE.add(COMANDE[8]+" <name> = "+ CMD.get(3));
        POSSIBLE.add(COMANDE[9]+" "+ CMD.get(3));
        POSSIBLE.add(COMANDE[9]+" <name> = "+ CMD.get(3));
        POSSIBLE.add(COMANDE[10]+" "+ CMD.get(4));
        POSSIBLE.add(COMANDE[10]+" <name> = "+ CMD.get(4));
        POSSIBLE.add(COMANDE[11]+" "+ CMD.get(1));
        POSSIBLE.add(COMANDE[11]+" <name> = "+ CMD.get(1));

        this.popupMenu  = new JPopupMenu();
        popup();
    }


    public void popup(){
        JMenu point = new JMenu("POINT");
        JMenu segment = new JMenu("SEGMENT");
        JMenu polygone = new JMenu("POLYGONE");
        JMenu cercle = new JMenu("CERCLE");
        JMenu oval = new JMenu("OVAL");
        JMenu arc_cercle = new JMenu("ARC_CERCLE");
        JMenu arc_oval = new JMenu("ARC_OVAL");
        JMenu droite = new JMenu("DROITE");
        JMenu parallele = new JMenu("PARALLELE");
        JMenu perpendiculaire = new JMenu("PERPENDICULAIRE");
        JMenu inter = new JMenu("INTER");
        JMenu grav = new JMenu("GRAV");
        popupMenu.add(point);
        popupMenu.add(segment);
        popupMenu.add(polygone);
        popupMenu.add(cercle);
        popupMenu.add(oval);
        popupMenu.add(arc_cercle);
        popupMenu.add(arc_oval);
        popupMenu.add(droite);
        popupMenu.add(parallele);
        popupMenu.add(perpendiculaire);
        popupMenu.add(inter);
        popupMenu.add(grav);

        MenuItem pun = new MenuItem(POSSIBLE.get(0));
        MenuItem pdeux = new MenuItem(POSSIBLE.get(1));
        MenuItem ptrois = new MenuItem(POSSIBLE.get(2));
        MenuItem pquatre = new MenuItem(POSSIBLE.get(3));
        point.add(pun);
        point.add(ptrois);
        point.add(pdeux);
        point.add(pquatre);

        MenuItem sun = new MenuItem(POSSIBLE.get(4));
        MenuItem sdeux = new MenuItem(POSSIBLE.get(5));
        segment.add(sun);
        segment.add(sdeux);
        MenuItem poun = new MenuItem(POSSIBLE.get(6));
        MenuItem podeux = new MenuItem(POSSIBLE.get(7));
        polygone.add(poun);
        polygone.add(podeux);

        MenuItem cun = new MenuItem(POSSIBLE.get(8));
        MenuItem cdeux = new MenuItem(POSSIBLE.get(9));
        MenuItem ctrois = new MenuItem(POSSIBLE.get(10));
        MenuItem cquatre = new MenuItem(POSSIBLE.get(11));
        cercle.add(cun);
        cercle.add(ctrois);
        cercle.add(cdeux);
        cercle.add(cquatre);

        MenuItem otrois = new MenuItem(POSSIBLE.get(14));
        MenuItem oquatre = new MenuItem(POSSIBLE.get(15));
        oval.add(otrois);
        oval.add(oquatre);

        MenuItem actrois = new MenuItem(POSSIBLE.get(18));
        MenuItem acquatre = new MenuItem(POSSIBLE.get(19));
        arc_cercle.add(actrois);
        arc_cercle.add(acquatre);

        MenuItem aoun = new MenuItem(POSSIBLE.get(20));
        MenuItem aodeux = new MenuItem(POSSIBLE.get(21));
        MenuItem aotrois = new MenuItem(POSSIBLE.get(22));
        MenuItem aoquatre = new MenuItem(POSSIBLE.get(23));
        arc_oval.add(aoun);
        arc_oval.add(aotrois);
        arc_oval.add(aodeux);
        arc_oval.add(aoquatre);

        MenuItem dun = new MenuItem(POSSIBLE.get(26));
        MenuItem ddeux = new MenuItem(POSSIBLE.get(27));
        MenuItem dtrois = new MenuItem(POSSIBLE.get(28));
        MenuItem dquatre = new MenuItem(POSSIBLE.get(29));
        droite.add(dun);
        droite.add(dtrois);
        droite.add(ddeux);
        droite.add(dquatre);

        MenuItem paun = new MenuItem(POSSIBLE.get(30));
        MenuItem padeux = new MenuItem(POSSIBLE.get(31));
        parallele.add(paun);
        parallele.add(padeux);

        MenuItem peun = new MenuItem(POSSIBLE.get(32));
        MenuItem pedeux = new MenuItem(POSSIBLE.get(33));
        perpendiculaire.add(peun);
        perpendiculaire.add(pedeux);

        MenuItem iun = new MenuItem(POSSIBLE.get(34));
        MenuItem ideux = new MenuItem(POSSIBLE.get(35));
        inter.add(iun);
        inter.add(ideux);

        MenuItem gun = new MenuItem(POSSIBLE.get(36));
        MenuItem gdeux = new MenuItem(POSSIBLE.get(37));
        grav.add(gun);
        grav.add(gdeux);
    }

    public String[] getCOMANDE() {
        return COMANDE;
    }

    /**
     * Retourne la liste des commande pouvant compléter String s
     * @param s String
     * @return ArrayList<String>
     */
    public ArrayList<String> getcorres(String s){
        ArrayList<String> list = new ArrayList<>();
        for (String ss : POSSIBLE){
            if(ss.startsWith(s.toUpperCase())){
                list.add(ss);
            }
        }
        return list;
    }

    /**
     * Fonction principal de l'autocompletion
     * Ouvre un popup d'autocomplétion si inputArea n'a pas de chevron a remplir
     */
    public void tabInput(){
        String input = jTextField.getText();
        input = input.trim();
        jTextField.setText(input);
        int indice = jTextField.getCaretPosition();
        if (input.trim().equals("")){
            jTextField.setComponentPopupMenu(popupMenu);
            popupMenu.setVisible(true);
            popupMenu.show(jTextField, 0,-50);
        }
        else if(indice > 0 && input.charAt(indice-1) == '(') {
            String s1 = input.substring(0, indice);
            String s2 = input.substring(indice, input.length());
            jTextField.setText(s1 + "<x>;<y>;<z>)" + s2);
            nextChevron(jTextField.getText());
        }
        else{
            if(!nextChevron(input)){
                ArrayList<String> list = getcorres(input);
                if(!list.isEmpty()){
                    JPopupMenu menu = new JPopupMenu();
                    for (String s : list){
                        menu.add(new MenuItem(s));
                    }
                    jTextField.setComponentPopupMenu(menu);
                    menu.setVisible(true);
                    menu.show(jTextField, 0,-50);
                }
            }
        }

    }


    /**
     * Selectionne dans le jtexfield les prochains chevrons de l'input
     * @param input String
     * @return true si chevron trouvé
     */

    public boolean nextChevron(String input){
        int deb,fin;
        deb = input.indexOf('<');
        if (deb == -1) return false;
        fin = input.indexOf('>')+1;
        if (fin == 0){
            input = input.replaceFirst("<[\\w]* *", "<POINT> ");
            jTextField.setText(input);
            fin = input.indexOf('>')+1;
        }
        if (input.substring(deb,fin).equals("<...>")){
            String s = input.replace("<...>", "<POINT>");
            deb = s.indexOf('<');
            fin = s.indexOf('>')+1;
            String s1 = s.substring(0, fin);
            String s2 = s.substring(fin, s.length());
            jTextField.setText(s1+" : <POINT>"+s2);
        }
        jTextField.select(deb,fin);
        return true;
    }
}

