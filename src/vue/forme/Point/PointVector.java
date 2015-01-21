package vue.forme.Point;

import Modele.FormeNotFoundExeption;
import Modele.Utilitaire;
import org.w3c.dom.Document;
import vue.DrawPanel;
import Modele.FormeVue;
import vue.forme.Polygone.PolygoneVector;

import javax.lang.model.element.Element;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * Created by ByTeK on 18/11/2014.
 *
 * La classe point et la classe "la plus petite" du drawPanel en ce sens ou chacun des element du drawPanl est constitué de point
 * Ici lorsqu'un point bouge il le dit a l'ensemble des forme auquelle il appartient.
 *
 */
public class PointVector extends FormeVue {
    public BigDecimal positionX;
    public BigDecimal positionY;
    public boolean tmp = true;
    public boolean inQuadra = false;
    public HashSet<FormeVue> liste_formeVue;
    public HashSet<PointEntrePoint> liste_entrePoint;
    private static final Stroke normalPointStroke = new BasicStroke(7.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke overPointStroke = new BasicStroke(10.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static int id = 1;


    public PointVector(BigDecimal x, BigDecimal y, int width, int heitgh,FormeVue liste_formeVue, boolean tmp){
        super(width);
        this.width = width;
        this.heigth = heitgh;
        this.positionX = x;
        this.positionY = y;
        //this.tmp= tmp;
        this.liste_formeVue = new HashSet<>();
        this.liste_entrePoint = new HashSet<>();
        if (liste_formeVue != null)
            this.liste_formeVue.add(liste_formeVue);
        this.clr = new Color(35, 155, 0);
        this.updateBounds();
        setTmp(tmp);

        //System.out.println(x.doubleValue());
        //System.out.println(y.doubleValue());
    }

    public PointVector(PolygoneVector q){
        this(new BigDecimal(0), new BigDecimal(0), q.getWidth(), q.getHeigth(), q,false);
        this.named("grav "+q.getName());
        q.setGrav(this);
        this.movable = false;
        this.clr = new Color(255, 89, 75, 255);
        this.updateBounds();
    }

    public void update(Graphics2D g2d){
        if(!tmp) {
            this.reset();
            drawName(g2d,(int)positionPixelX(positionX)+5,(int)(this.heigth - positionPixelY(positionY))+5);
            this.moveTo(positionPixelX(positionX), this.heigth - positionPixelY(positionY));
            this.lineTo(positionPixelX(positionX), this.heigth - positionPixelY(positionY));
            if (isOver() || isSelect()) {
                g2d.setStroke(overPointStroke);
            } else g2d.setStroke(normalPointStroke);
            g2d.setColor(clr);
            this.scale();
            this.scale();
            this.rotate();
            g2d.draw(this);
        }
    }

    public void fuse(PointVector p) throws FormeNotFoundExeption{
        int i;
        for (FormeVue q:p.liste_formeVue)if(q.getListe_points().contains(this))throw new FormeNotFoundExeption();
        for (FormeVue q:p.liste_formeVue){
            i = q.getListe_points().indexOf(p);
            q.getListe_points().set(i,this);
        }
        this.liste_formeVue.addAll(p.liste_formeVue);
        p.liste_formeVue = new HashSet<FormeVue>();
    }

    @Override
    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        this.updateBounds();
        return true;
    }

    @Override
    public boolean setPoint(PointVector p, boolean set) {
        this.updateBounds();
        return true;
    }

    public  void moveXt(double nb,FormeVue who){
        positionX = positionX.add(DrawPanel.offSetX.multiply(new BigDecimal(nb)));
        for (FormeVue f : liste_formeVue)
            if(f!=who)f.pointMoved(this, who, nb,0);
        for (PointEntrePoint f : liste_entrePoint)
            if(f!=who)f.pointMoved(this, who, nb,0);
        this.updateBounds();
    }
    public  void moveYt(double nb,FormeVue who){
        positionY = positionY.add(DrawPanel.offSetY.multiply(new BigDecimal(nb)));
        for (FormeVue f : liste_formeVue)
            if(f!=who)f.pointMoved(this, who, 0, nb);
        for (PointEntrePoint f : liste_entrePoint)
            if(f!=who)f.pointMoved(this, who, 0, nb);
        this.updateBounds();
    }

    public String htmlString(){
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Point </a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a>"
                +"<br>"+this.name+"<ul>"+
                "<li>x: "+Utilitaire.posToString(positionX)+"</li>\n" +
                "<li>y: "+Utilitaire.posToString(positionY)+"</li>\n" +
                "</ul>";
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        liste_formeVue.remove(p);
        return liste_formeVue;
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {

    }

    public  HashSet<FormeVue> delete(FormeVue who) {
        HashSet<FormeVue> retour = super.delete(this);
        if(who == null) {
            HashSet<FormeVue> delForme = new HashSet<FormeVue>();
            for (FormeVue f : liste_formeVue){
                delForme.addAll(f.removePoint(this));
            }
            for (PointEntrePoint f : liste_entrePoint){
                delForme.addAll(f.removePoint(this));
            }
            liste_formeVue.removeAll(delForme);
            retour.addAll(delForme);
            retour.add(this);
        }
        if(who != null){
            liste_formeVue.remove(who);
            if(liste_formeVue.size() == 0){
                for (PointEntrePoint f : liste_entrePoint){
                    retour.addAll(f.removePoint(this));
                }
                retour.add(this);
            }
        }
        return retour;
    }

    public void updateBounds() {
        boundRigthX = this.positionX;
        boundLeftX = this.positionX;
        boundBotY = this.positionY;
        boundTopY = this.positionY;
    }
    public boolean inBounds(BigDecimal top,BigDecimal bot,BigDecimal rigth,BigDecimal left){
        boolean vert = false;
        boolean hori = false;
        if(boundTopY.compareTo(bot) == 1) vert = true;
        if(boundBotY.compareTo(top) ==  -1) vert = true;
        if(boundLeftX.compareTo(rigth) == -1) hori = true;
        if(boundRigthX.compareTo(left) == 1) hori = true;
        return hori && vert;
    }

    public void add_to_liste(FormeVue f){
        this.liste_formeVue.add(f);
    }

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "rect");
        rectangle.setAttributeNS(null, "x", ""+positionPixelX(positionX));
        rectangle.setAttributeNS(null, "y", ""+(this.heigth-positionPixelY(positionY)));
        rectangle.setAttributeNS(null, "width", "5");
        rectangle.setAttributeNS(null, "height", "5");
        /*String hashListe_formeVue = "";
        for(FormeVue q : liste_formeVue){
            hashListe_formeVue += Integer.toString(q.hashCode()) + "|";
        }
        rectangle.setAttributeNS(null, "liste", hashListe_formeVue);*/
        rectangle.setAttributeNS(null, "id",Integer.toString(this.hashCode()));
        if(this.inQuadra)
            rectangle.setAttributeNS(null, "quadra", "1");
        else
            rectangle.setAttributeNS(null, "quadra", "0");
        return rectangle;
    }

    /*public  FormeVue selectedForme(BigDecimal x, BigDecimal y,boolean deep){
        //System.out.println(tmp + "\n" + x.abs().subtract(this.positionX.abs()).abs().compareTo(new BigDecimal(5).multiply(DrawPanel.offSetX)));
        //System.out.println(y.abs().subtract(this.positionY.abs()).abs().compareTo(new BigDecimal(5).multiply(DrawPanel.offSetY)));
        if(x.abs().subtract(this.positionX.abs()).abs().compareTo(new BigDecimal(5).multiply(DrawPanel.offSetX)) ==  -1 && y.abs().subtract(this.positionY.abs()).abs().compareTo(new BigDecimal(5).multiply(DrawPanel.offSetY)) ==  -1 && !tmp)return this;
        //System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\n\n");
        return null;
    }*/

    public void setTmp(boolean tmp) {
        if(this.tmp == tmp)return;
        if(!tmp){
            this.named("p"+id);
            id++;
        }
        if(tmp){
            id--;
        }
        this.tmp = tmp;


    }
    public boolean isTmp() {
        return tmp;
    }

    public boolean mouseOn(double x, double y) {
        BasicStroke pathStroke = new BasicStroke(1);
        Shape clippedPath = pathStroke.createStrokedShape(this);
        return !tmp && clippedPath.intersects(x - 5, y - 5, 10, 10);
    }

    public void setRotate(int scale,PointVector center,HashSet<FormeVue> done){
        done.add(this);
        for(FormeVue p: liste_formeVue){
            if(!done.contains(p)) p.setRotate(scale, center, done);
        }
        this.centreRota = center;
        this.rotation = scale;
    }

    public void setScale(int scale,HashSet<FormeVue> done){
        done.add(this);
        for(FormeVue p: liste_formeVue){
            if(!done.contains(p)) p.setScale(scale, done);
        }
        this.scale = scale;
    }
}