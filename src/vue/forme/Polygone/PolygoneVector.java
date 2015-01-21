package vue.forme.Polygone;

import Modele.HashMapComponent;
import org.w3c.dom.Document;
import vue.DrawPanel;
import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by renaud on 18/11/14.
 */
public class PolygoneVector extends FormeVue {

    //public ArrayList<PointVector> listePoint;
    protected int indice = 1;
    protected PointVector grav;

    private static int id = 1;
    public PolygoneVector(int width) {
        super(width);
    }

    public PolygoneVector(BigDecimal x, BigDecimal y, int width, int heitgh){
        this(new PointVector(x,y,width,heitgh,null, false), width,heitgh);

    }

    public PolygoneVector(PointVector p, int width, int heitgh){
        this(width);
        p.setTmp(false);
        this.liste_points = new ArrayList<>();
        this.heigth = heitgh;
        this.liste_points.add(p);
        liste_points.add(indice, p);
        p.add_to_liste(this);
        this.clr = new Color(100,100,100,50);
        this.updateBounds();
        this.named("po"+id);
        id++;
        //this.pointB = b;
    }

    /*public void setSelect(boolean over){
        super.setSelect(over);
        for (PointVector p: liste_points){
            p.setSelect(over);
        }

    }

    public boolean isSelect() {
        boolean b = super.isSelect();
       for (PointVector  p: liste_points){
           b |= p.isSelect();
       }
        return b;
    }

    public boolean isOver() {
        boolean b = super.isOver();
        for (PointVector  p: liste_points){
            b |= p.isOver();
        }
        return b;
    }
    */

    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        drawAngle(g2d);
        //super.update(g2d);
        //if(grav!=null)grav.update(g2d);
        //setRotate(0.8);
        drawName(g2d,(int)((positionPixelX(liste_points.get(0).positionX)+(positionPixelX(liste_points.get(1).positionX)))/2+5),(int)((this.heigth - positionPixelY(liste_points.get(0).positionY)+(this.heigth - positionPixelY(liste_points.get(1).positionY)))/2)+5);
        //g2d.setColor(new Color(5,67,90));
        this.moveTo(positionPixelX(this.liste_points.get(0).positionX), this.heigth - positionPixelY(this.liste_points.get(0).positionY));
        for(int i = 0; i<liste_points.size()-1; i++){
            this.liste_points.get(i).update(g2d);
            this.liste_points.get(i + 1).update(g2d);
            this.lineTo(positionPixelX(this.liste_points.get(i+1).positionX), this.heigth - positionPixelY(this.liste_points.get(i+1).positionY));
        }
        this.liste_points.get(liste_points.size()-1).update(g2d);
        //this.append(this.listePoint.get(listePoint.size()-1),true);
        g2d.draw(this.liste_points.get(liste_points.size()-1));
        //this.moveTo(positionPixelX(this.listePoint.get(listePoint.size()-1).positionX), this.heigth - positionPixelY(this.listePoint.get(listePoint.size() - 1).positionY));
        this.lineTo(positionPixelX(this.liste_points.get(0).positionX), this.heigth - positionPixelY(this.liste_points.get(0).positionY));

        if(isSelect()||isOver())g2d.setStroke(overStroke);
        else g2d.setStroke(normalStroke);
        g2d.setColor(clr);
        this.closePath();
        this.scale();
        this.rotate();
        g2d.fill(this);
        g2d.draw(this);


    }


    public void close() {
        liste_points.remove(liste_points.size() - 1);
        if(liste_points.size()<3)valide = false;
    }

    public boolean setPoint(BigDecimal x2,BigDecimal y2,boolean set) {
        return this.setPoint(new PointVector(x2,y2, width, heigth, this,!set), set);
    }

    @Override
    public boolean setPoint(PointVector p, boolean set) {
        if (set) {
            if (liste_points.get(0).intersects(positionPixelX(p.positionX)-10,this.heigth - positionPixelY(p.positionY)-10, positionPixelX(p.positionX)+10,this.heigth - positionPixelY(p.positionY)+10)){
                close();
                this.updateBounds();
                return true;
            }
            else {
                p.add_to_liste(this);
                liste_points.set(indice, p);
                indice ++;
                liste_points.add(indice,p);
                this.updateBounds();
                return false;
            }
        }
        else {
            liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }

    /*public  FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep) {
            for (PointVector p : liste_points) {
                if (p.selectedForme(x,y,true)!= null)
                    return p;
            }
        }
        return this;
    }*/

    public void moveXt(double nb,FormeVue who){
        if(grav!=null)grav.moveXt(nb,this);
        for (PointVector p : liste_points) {
            p.moveX(nb,this);
        }
        this.updateBounds();
        for(FormeVue q : dependance){
            q.moveX(nb,this);
        }
        this.updateBounds();
    }
    public void moveYt(double nb,FormeVue who){
        if(grav!=null)grav.moveYt(nb,this);
        for (PointVector p : liste_points) {
            p.moveY(nb,this);
        }
        for(FormeVue q : dependance){
            q.moveY(nb,this);
        }
        this.updateBounds();
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s =  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Polygone :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }
    public void add(HashMapComponent map){
        super.add(map);
        for (PointVector p : liste_points) {
           p.add(map);
        }
    }

    @Override
    public HashSet<FormeVue> delete(FormeVue who) {
        HashSet<FormeVue> retour = super.delete(this);
        if(grav!=null)retour.addAll(grav.delete(this));
        return retour;
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        if(p == grav){
            grav = null;
            return new HashSet<>();
        }
        else if(liste_points.size()>3){
            liste_points.remove(p);
            return new HashSet<>();
        }
        else return this.delete(this);
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        if(who!=this && grav!=null && p!= grav){
            this.setGrav();
        }
    }

    public void setGrav(){
        BigDecimal lastX = grav.positionX;
        BigDecimal lastY = grav.positionY;
        BigDecimal x = new BigDecimal(0);
        BigDecimal y = new BigDecimal(0);
        for(PointVector p : this.liste_points){
            x=x.add(p.positionX);
            y=y.add(p.positionY);
        }
        grav.positionX = x.divide(new BigDecimal(this.liste_points.size()), RoundingMode.HALF_EVEN);
        grav.positionY = y.divide(new BigDecimal(this.liste_points.size()),RoundingMode.HALF_EVEN);
        lastX = lastX.subtract(grav.positionX);
        lastY = lastY.subtract(grav.positionY);
        double howX = lastX.multiply(DrawPanel.offSetX).doubleValue();
        double howY = lastY.multiply(DrawPanel.offSetY).doubleValue();
        for(FormeVue q: grav.liste_formeVue){
            q.pointMoved(grav,grav,howX,howY);
        }

        this.updateBounds();
    }

    /*
    public void dependanceMoved(PointVector p) {
        if(!all && grav != null)
            this.grav.setGrav(this);
    }
    */

    public boolean setGrav(PointVector p){
        if(this.grav != null)return false;
        this.grav = p;
        this.setGrav();
        return true;
    }

    public void updateBounds() {
        boundRigthX = liste_points.get(0).positionX;
        boundLeftX = liste_points.get(0).positionX;
        boundBotY = liste_points.get(0).positionY;
        boundTopY = liste_points.get(0).positionY;
        for(PointVector p : liste_points){
            if(p.positionX.compareTo(boundLeftX) == -1)boundLeftX = p.positionX;
            if(p.positionX.compareTo(boundRigthX) == 1)boundRigthX = p.positionX;
            if(p.positionY.compareTo(boundTopY) == 1)boundTopY = p.positionY;
            if(p.positionY.compareTo(boundBotY) == -1)boundBotY = p.positionY;
        }
    }

    public void setHeigth(int heigth){
        super.setHeigth(heigth);
        for(PointVector p : liste_points){
            p.setHeigth(heigth);
        }
    }

    public void setWidth(int width){
        super.setWidth(width);
        for(PointVector p : liste_points){
            p.setWidth(width);
        }
    }

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "polygon");
        String points ="";
        for (PointVector p: liste_points){
            points += positionPixelX(p.positionX)+","+(this.heigth-positionPixelY(p.positionY))+",";
        }
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY));
        rectangle.setAttributeNS(null, "points", points);
        String hashListe_point = "";
        for(PointVector p : liste_points){
            hashListe_point += Integer.toString(p.hashCode()) + ",";
        }
        //hashListe_point += Integer.toString(liste_points.get(0).hashCode());
        rectangle.setAttributeNS(null, "listePoint", hashListe_point);
        rectangle.setAttributeNS(null, "id",Integer.toString(this.hashCode()));
        return rectangle;

        /*<polygon points="475,175,504,184,522,209,522,240,504,265,475,275,445,265,427,240,427,209,445,184,475,175" />*/

    }

    public PointVector getGrav() {
        return grav;
    }

}
