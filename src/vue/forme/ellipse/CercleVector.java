package vue.forme.ellipse;

import Modele.HashMapComponent;
import Modele.Utilitaire;
import org.w3c.dom.Document;
import vue.DrawPanel;
import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

public class CercleVector extends FormeVue {
    //PointVector pointA;
    //PointVector pointB;
    //PointVector last;
    BigDecimal rayon;
    //boolean all = false;
    public static int id = 1;


    public CercleVector(int width) {
        super(width);
    }

    public CercleVector(BigDecimal xA, BigDecimal yA, int width, int heitgh){
        this(new PointVector(xA,yA,width,heitgh,null,false),width,heitgh);
    }

    public CercleVector(PointVector pointVector, int width, int heitgh){
        this(width);
        pointVector.setTmp(false);
        this.heigth = heitgh;
        this.liste_points.add(pointVector);
        this.liste_points.add(new PointVector(this.liste_points.get(0).positionX, this.liste_points.get(0).positionY, width, heigth, this, true));
        this.setPoint(pointVector, false);
        //updateBounds();
        //last = new PointVector(pointA.positionX,pointA.positionY,width,heigth,this,false);
        pointVector.add_to_liste(this);
        this.liste_points.get(1).add_to_liste(this);
        this.name = "c"+id;
        id++;
        this.clr = new Color(255, 84, 0, 50);

    }

    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        PointVector pointA = this.liste_points.get(0);
        PointVector pointB = this.liste_points.get(1);
        drawName(g2d,(int)(positionPixelX(liste_points.get(1).positionX)-20),(int)(this.heigth - positionPixelY(liste_points.get(1).positionY)-20));
        this.append(new Ellipse2D.Double(positionPixelX(pointA.positionX.subtract(rayon)),this.heigth - positionPixelY(pointA.positionY.add(rayon)),new BigDecimal(2).multiply(rayon).divide(DrawPanel.offSetX,RoundingMode.HALF_EVEN).doubleValue(),new BigDecimal(2).multiply(rayon).divide(DrawPanel.offSetY,RoundingMode.HALF_EVEN).doubleValue()),false);
        if(isSelect()||isOver())g2d.setStroke(overStroke);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //this.closePath();
        g2d.setColor(this.clr);
        this.append(pointA, false);
        this.append(pointB, false);
        this.scale();
        this.rotate();
        g2d.fill(this);
        g2d.draw(this);
        pointA.update(g2d);
        pointB.update(g2d);


    }


    public boolean setPoint(PointVector p ,boolean set){
        PointVector pointA = this.liste_points.get(0);
        this.liste_points.set(1, p);
        PointVector pointB = this.liste_points.get(1);
        rayon = Utilitaire.sqrt(pointB.positionX.subtract(pointA.positionX).multiply(pointB.positionX.subtract(pointA.positionX)).add(pointB.positionY.subtract(pointA.positionY).multiply(pointB.positionY.subtract(pointA.positionY))));
        this.updateBounds();
        if(set){
            p.add_to_liste(this);
        }
        return true;
    }

    @Override
    public boolean setPoint(BigDecimal x2,BigDecimal y2, boolean set) {
        this.setPoint(new PointVector(x2,y2,this.width,this.heigth,this,!set),set);
        return set;
    }

    public boolean setRayon(BigDecimal r, boolean set){
        return setPoint(liste_points.get(0).positionX.add(r),liste_points.get(0).positionY,set);
    }

    /*public  FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep){
            if(pointA.selectedForme(x,y,true)!= null)return pointA;
            if(pointB.selectedForme(x,y,true)!= null)return pointB;

        }
        return this;
    }
    */
    public void moveXt(double nb,FormeVue who){
        PointVector pointA = this.liste_points.get(0);
        PointVector pointB = this.liste_points.get(1);
        pointA.moveX(nb, this);
        pointB.moveX(nb, this);
        this.setPoint(pointB,true);
        this.updateBounds();
    }
    public void moveYt(double nb,FormeVue who){
        PointVector pointA = this.liste_points.get(0);
        PointVector pointB = this.liste_points.get(1);
        pointA.moveY(nb, this);
        pointB.moveY(nb,this);
        this.setPoint(pointB,true);
        this.updateBounds();
    }
    public String htmlString(){
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Cercle</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>"
                +"<li>"+this.liste_points.get(0).htmlString()+"</li>"
                +"<li>r: "+Utilitaire.posToString(rayon)+"</li>"
                +"</ul>";
    }

    /*public void setSelect(boolean over){
        super.setSelect(over);
        this.liste_points.get(0).setSelect(over);
        this.liste_points.get(1).setSelect(over);

    }
    */
    /*public void setOver(boolean over){

        super.setOver(over);
        this.liste_points.get(0).setOver(over);
        this.liste_points.get(1).setOver(over);
    }*/
    public void add(HashMapComponent map){
        super.add(map);
        this.liste_points.get(0).add(map);
        this.liste_points.get(1).add(map);
    }

    /*public  HashSet<FormeVue> delete(FormeVue who) {
        HashSet<FormeVue> retour = super.delete(this);
        return retour;
    }*/

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return this.delete(this);
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        PointVector pointB = this.liste_points.get(1);
        if(p != this.liste_points.get(0))
            setPoint(p, true);
        if(p == this.liste_points.get(0)){
            if(who == null || !who.getListe_points().contains(pointB)) {
                pointB.moveX(howX,this);
                pointB.moveY(howY,this);
                this.setPoint(pointB,true);
            }
        }
        this.updateBounds();
    }


    /*@Override
    public void dependanceMoved(PointVector p) {
        if(p != pointA)
            setPoint(p, true);
        if(p == pointA && !all){
            pointB.positionX = pointB.positionX.add(pointA.positionX.subtract(last.positionX));
            pointB.positionY = pointB.positionY.add(pointA.positionY.subtract(last.positionY));
        }
        last.positionX = pointA.positionX;
        last.positionY = pointA.positionY;
        this.updateBounds();
    }
    */

    public void updateBounds() {
        PointVector pointA = this.liste_points.get(0);
        boundRigthX = pointA.positionX.add(rayon);
        boundLeftX = pointA.positionX.subtract(rayon);
        boundBotY = pointA.positionY.subtract(rayon);
        boundTopY = pointA.positionY.add(rayon);
    }

    public void setHeigth(int heigth){
        super.setHeigth(heigth);
        this.liste_points.get(0).setHeigth(heigth);
        this.liste_points.get(1).setHeigth(heigth);
    }

    public void setWidth(int width){
        super.setWidth(width);
        this.liste_points.get(0).setWidth(width);
        this.liste_points.get(1).setWidth(width);
    }

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        //<circle r="50" cx="100" cy="100" fill="cyan" fill-opacity="1"/>
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "circle");
        //String points ="";
        /*for (PointVector p: liste_points){
            points += positionPixelX(p.positionX)+","+(this.heigth-positionPixelY(p.positionY))+",";
        }
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY));
        rectangle.setAttributeNS(null, "points", points);*/
        rectangle.setAttributeNS(null, "r",this.rayon.divide(DrawPanel.offSetX, BigDecimal.ROUND_HALF_EVEN).toString());
        rectangle.setAttributeNS(null, "cx",""+positionPixelX(this.liste_points.get(0).positionX));
        rectangle.setAttributeNS(null, "cy",""+(this.heigth-positionPixelY(liste_points.get(0).positionY)));
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
}
