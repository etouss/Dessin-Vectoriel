package vue.forme.ellipse;

import Modele.FormeVue;
import Modele.Utilitaire;
import vue.DrawPanel;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * Created by renaud on 09/12/14.
 */
public class ArcCercleVector extends FormeVue {

    BigDecimal rayon1;
    double angle;
    double start_angle = 0;
    double end_angle = 0;
    boolean rayon1set = false;
    public static int id = 1;

    public ArcCercleVector(int width) {
        super(width);
    }

    public ArcCercleVector(BigDecimal xA, BigDecimal yA, int width, int heitgh) {
        this(new PointVector(xA,yA,width,heitgh,null,false),width,heitgh);
    }

    public ArcCercleVector(PointVector pointVector, int width, int heitgh) {
        super(width);
        pointVector.setTmp(false);
        this.heigth = heitgh;
        this.liste_points.add(pointVector);
        pointVector.add_to_liste(this);
        angle = 0;
        rayon1 = new BigDecimal(0);
        updateBounds();
        this.name = "a"+id;
        id++;
        this.clr = new Color(255, 84, 0, 50);
    }




    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        drawName(g2d,(int)(positionPixelX(liste_points.get(1).positionX)-20),(int)(this.heigth - positionPixelY(liste_points.get(1).positionY)-20));
        this.append(liste_points.get(0),true);
        if(liste_points.get(2).isTmp()) this.append(new Ellipse2D.Double(positionPixelX(liste_points.get(0).positionX.subtract(rayon1)),this.heigth - positionPixelY(liste_points.get(0).positionY.add(rayon1)),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX,RoundingMode.HALF_EVEN).doubleValue(),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX,RoundingMode.HALF_EVEN).doubleValue()),true);
        else{
            this.liste_points.get(2) .update(g2d);
            this.append(new Arc2D.Double(positionPixelX(liste_points.get(0).positionX.subtract(rayon1)),this.heigth - positionPixelY(liste_points.get(0).positionY.add(rayon1)), new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX, RoundingMode.HALF_EVEN).doubleValue(),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX, RoundingMode.HALF_EVEN).doubleValue(),Math.toDegrees(start_angle),end_angle,Arc2D.OPEN), true);
        }
        this.closePath();
        if(isSelect()||isOver())g2d.setStroke(overStroke);
        else g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setColor(this.clr);
        this.scale();
        this.rotate();
        g2d.fill(this);
        g2d.draw(this);
        this.liste_points.get(1).update(g2d);
        this.liste_points.get(0).update(g2d);

    }

    @Override
    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        if(!rayon1set){
            if (liste_points.size() == 1){
                liste_points.add(new PointVector(x,y,width,heigth,this,false));
                liste_points.add(new PointVector(x,y,width,heigth,this,true));
            }
            this.liste_points.get(1).positionX = x;
            this.liste_points.get(1).positionY = y;
            BigDecimal yac = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
            rayon1 = Utilitaire.sqrt(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY))));
            if(yac.doubleValue() <0) start_angle = Math.PI * 2 - Math.acos(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            else start_angle = Math.acos(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            if (set){
                liste_points.get(1).add_to_liste(this);
                rayon1set = true;
                setPoint(liste_points.get(0).positionX.add(rayon1.multiply(new BigDecimal(Math.cos(angle)))),liste_points.get(0).positionY.add(rayon1.multiply(new BigDecimal(Math.sin(angle)))),true);
            }
            updateBounds();
            return false;
        }
        else {
            if(this.liste_points.get(2).isTmp())this.liste_points.get(2).setTmp(false);
            this.liste_points.get(2).positionX = x;
            this.liste_points.get(2).positionY = y;
            BigDecimal opp = liste_points.get(2).positionY.subtract(liste_points.get(0).positionY);
            BigDecimal adj = liste_points.get(0).positionX.subtract(liste_points.get(2).positionX);
            if(adj.compareTo(new BigDecimal(0)) == 0) end_angle = 0;
            else end_angle = Math.atan(opp.divide(adj, RoundingMode.HALF_EVEN).doubleValue());//1
            if (liste_points.get(2).positionX.compareTo(liste_points.get(0).positionX) > 0){
                if (liste_points.get(0).positionY.compareTo(liste_points.get(2).positionY) > 0 )end_angle =Math.toRadians(360 - Math.toDegrees(end_angle));
                else end_angle =  Math.abs(end_angle);
            }
            else {
                end_angle = Math.PI - end_angle;
            }
            angle = end_angle;
            end_angle -= start_angle ;
            end_angle = Math.toDegrees(end_angle);

            if(end_angle < 0) end_angle += 360;
            liste_points.get(2).positionX = liste_points.get(0).positionX.add(rayon1.multiply(new BigDecimal(Math.cos(angle))));
            liste_points.get(2).positionY = liste_points.get(0).positionY.add(rayon1.multiply(new BigDecimal(Math.sin(angle))));
            if (set){
                liste_points.get(2).add_to_liste(this);
                return true;
            }
            else return false;
        }


    }

    public void set_p1(BigDecimal rayon, BigDecimal angle){
        setPoint(liste_points.get(0).positionX.add(rayon.multiply(new BigDecimal(Math.cos(Math.toRadians(angle.doubleValue()))))), liste_points.get(0).positionY.add(rayon.multiply(new BigDecimal(Math.sin(Math.toRadians(angle.doubleValue()))))),true);
    }

    public void set_p2(BigDecimal angle){
        setPoint(liste_points.get(0).positionX.add(rayon1.multiply(new BigDecimal(Math.cos(Math.toRadians(angle.doubleValue()))))), liste_points.get(0).positionY.add(rayon1.multiply(new BigDecimal(Math.sin(Math.toRadians(angle.doubleValue()))))),true);
    }
    @Override
    public boolean setPoint(PointVector p, boolean set) {

        return setPoint(p.positionX, p.positionY, set);
    }

    @Override
    public void moveXt(double nb,FormeVue who) {
        this.liste_points.get(0).moveX(nb,who);
        this.updateBounds();
    }

    @Override
    public void moveYt(double nb,FormeVue who) {
        this.liste_points.get(0).moveY(nb,who);
        this.updateBounds();
    }

    @Override
    public String htmlString() {
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Arc de Cercle</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>"
                +"<li>"+liste_points.get(0).htmlString()+"</li>"
                //+"<li>"+liste_points.get(1).htmlString()+"</li>"
                //+"<li>"+pointC.htmlString()+"</li>"
                +"<li>r: "+Utilitaire.posToString(rayon1)+"</li>"
                +"</ul>";
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return null;
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        PointVector pointB = this.liste_points.get(1);
        PointVector pointC = this.liste_points.get(2);
        if(p == pointB) {
            rayon1set = false;
            setPoint(p, true);
        }
        else if(p == pointC){
            System.out.println("erfdzefgfez");
            setPoint(p, true);
        }
        if(p == this.liste_points.get(0)){
            if(who == null || !(who.getListe_points().contains(pointB) && who.getListe_points().contains(pointC))) {
                pointB.moveX(howX,this);
                pointB.moveY(howY,this);
                rayon1set = false;
                setPoint(pointB, true);


            }
        }
        this.updateBounds();
    }

    @Override
    public void updateBounds() {
        boundRigthX = liste_points.get(0).positionX.add(rayon1);
        boundLeftX = liste_points.get(0).positionX.subtract(rayon1);
        boundBotY = liste_points.get(0).positionY.subtract(rayon1);
        boundTopY = liste_points.get(0).positionY.add(rayon1);
    }

    /*public  FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep){
            if(pointA.selectedForme(x,y,true)!= null)return pointA;
            if(pointB != null && pointB.selectedForme(x,y,true)!= null)return pointB;
            if(pointC != null && pointC.selectedForme(x,y,true)!= null)return pointC;

        }
        return this;
    }
    */

    public void setHeigth(int heigth){
        super.setHeigth(heigth);
        liste_points.get(0).setHeigth(heigth);
        liste_points.get(1).setHeigth(heigth);
        liste_points.get(2).setHeigth(heigth);
    }

    public void setWidth(int width){
        super.setWidth(width);
        liste_points.get(0).setWidth(width);
        liste_points.get(1).setWidth(width);
        liste_points.get(2).setWidth(width);
    }
}