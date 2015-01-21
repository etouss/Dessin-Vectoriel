package vue.forme;

import Modele.HashMapComponent;
import org.w3c.dom.Document;
import vue.DrawPanel;
import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Created by renaud on 18/11/14.
 */
public class SegmentVector extends FormeVue {
    //PointVector pointA;
    //PointVector pointB;
    private static int id = 1;


    public SegmentVector(int width) {
        super(width);
    }

    public SegmentVector(BigDecimal xA, BigDecimal yA, int width, int heitgh){
        this(new PointVector(xA,yA,width,heitgh,null,false),width,heitgh);
    }

    public SegmentVector(PointVector p, int width, int heitgh){
        this(width);
        p.setTmp(false);
        this.heigth = heitgh;
        this.liste_points.add(p);
        p.add_to_liste(this);
        this.liste_points.add(p);
        updateBounds();
        this.named("s"+id);
        id++;

    }


    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        //super.update(g2d);
        drawName(g2d,(int)((positionPixelX(liste_points.get(0).positionX)+(positionPixelX(liste_points.get(1).positionX)))/2+5),(int)((this.heigth - positionPixelY(liste_points.get(0).positionY)+(this.heigth - positionPixelY(liste_points.get(1).positionY)))/2)+5);
        this.moveTo(positionPixelX(this.liste_points.get(0).positionX), this.heigth - positionPixelY(this.liste_points.get(0).positionY));
        this.lineTo(positionPixelX(this.liste_points.get(1).positionX), this.heigth - positionPixelY(this.liste_points.get(1).positionY));
        if(isOver()||isSelect())g2d.setStroke(overStroke);
        else g2d.setStroke(normalStroke);
        g2d.setColor(clr);
        this.scale();
        this.rotate();
        g2d.draw(this);
        this.liste_points.get(0).update(g2d);
        this.liste_points.get(1).update(g2d);
        this.append(this.liste_points.get(0),true);
        this.append(this.liste_points.get(1),true);

    }

    public boolean setPoint(BigDecimal x2,BigDecimal y2,boolean set){
        PointVector pointVector = new PointVector(x2,y2, width, heigth, this,!set);
        return setPoint(pointVector,set);
    }

    public boolean setPoint(PointVector point,boolean set){
        this.liste_points.set(1,point);
        if(set)point.add_to_liste(this);
        this.updateBounds();
        return set;
    }

    /*public  FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep){
            //System.out.println("AZERTYUIOPOIUYTREZAZERTYUIOIUYTREZERTYUI                    " + pointB.getName());
            if(pointA.selectedForme(x,y,true)!= null)return pointA;
            if(pointB.selectedForme(x,y,true)!= null)return pointB;
        }
        return this;
    }
    */
    public void moveXt(double nb,FormeVue who){
        this.liste_points.get(0).moveX(nb,who);
        this.liste_points.get(1).moveX(nb, who);
        this.updateBounds();
    }
    public void moveYt(double nb,FormeVue who){
        this.liste_points.get(0).moveY(nb, who);
        this.liste_points.get(1).moveY(nb, who);
        this.updateBounds();
    }
    public String htmlString(){
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Segment</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>"
                +"<li>"+this.liste_points.get(0).htmlString()+"</li>"
                +"<li>"+this.liste_points.get(1).htmlString()+"</li>"
                +"</ul>";
    }
    public void add(HashMapComponent map){
        super.add(map);
        this.liste_points.get(0).add(map);
        this.liste_points.get(1).add(map);
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return this.delete(this);
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {

    }


    /*@Override
    public  HashSet<FormeVue> (FormeVue who) {
        HashSet<FormeVue> retour = super.delete(this);
        return retour;
    }
    */



    /*public void setSelect(boolean over){
        super.setSelect(over);
        this.liste_points.get(0).setSelect(over);
        this.liste_points.get(1).setSelect(over);

    }*/

    /*public void setOver(boolean over){
        super.setOver(over);
        this.liste_points.get(0).setOver(over);
        this.liste_points.get(1).setOver(over);
    }*/

    public void updateBounds() {
        if(this.liste_points.get(0).positionX.compareTo(this.liste_points.get(1).positionX) == -1)boundLeftX = this.liste_points.get(0).positionX;
        else boundLeftX = this.liste_points.get(1).positionX;
        if(this.liste_points.get(0).positionX.compareTo(this.liste_points.get(1).positionX) == 1)boundRigthX = this.liste_points.get(0).positionX;
        else boundRigthX = this.liste_points.get(1).positionX;
        if(this.liste_points.get(0).positionY.compareTo(this.liste_points.get(1).positionY) == 1)boundTopY = this.liste_points.get(0).positionY;
        else boundTopY = this.liste_points.get(1).positionY;
        if(this.liste_points.get(0).positionY.compareTo(this.liste_points.get(1).positionY) == -1)boundBotY = this.liste_points.get(0).positionY;
        else boundBotY = this.liste_points.get(1).positionY;
        if(boundBotY.compareTo(boundTopY) == 0)boundTopY = boundBotY.add(new BigDecimal(2).multiply(DrawPanel.offSetY));
        if(boundRigthX.compareTo(boundLeftX)==0)boundRigthX = boundLeftX.add(new BigDecimal(2).multiply(DrawPanel.offSetX));
    }

    //public String toString(){
        //return "SEGMENT:"+this.name+"\nA: "+this.pointA+"\nB: "+pointB;
        //return "SEGMENT:"+this.name+"\nA: "+this.pointA+"\nBdele: "+pointB;
   // }

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        //<line x1="50" y1="50" x2="150" y2="400" stroke="blue" stroke-linecap="round"/>
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "line");
        //String points ="";
        /*for (PointVector p: liste_points){
            points += positionPixelX(p.positionX)+","+(this.heigth-positionPixelY(p.positionY))+",";
        }
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY));
        rectangle.setAttributeNS(null, "points", points);*/
        rectangle.setAttributeNS(null, "stroke","blue");
        rectangle.setAttributeNS(null, "x1",""+positionPixelX(this.liste_points.get(0).positionX));
        rectangle.setAttributeNS(null, "y1",""+(this.heigth-positionPixelY(liste_points.get(0).positionY)));
        rectangle.setAttributeNS(null, "x2",""+positionPixelX(this.liste_points.get(1).positionX));
        rectangle.setAttributeNS(null, "y2",""+(this.heigth-positionPixelY(liste_points.get(1).positionY)));
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
