package vue.forme.ellipse;

import Modele.FormeVue;
import Modele.Utilitaire;
import org.w3c.dom.Document;
import vue.DrawPanel;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * Created by renaud on 03/12/14.
 */
public class OvalVector extends FormeVue{
    BigDecimal rayon1;
    double rot = 0;
    boolean rayon1set = false;
    boolean rayon2set = false;
    BigDecimal rayon2;
    public static int id = 1;


    public OvalVector(int width) {
        super(width);
    }

    public OvalVector(BigDecimal xA, BigDecimal yA, int width, int heitgh) {
        this(new PointVector(xA,yA,width,heitgh,null,false),width,heitgh);
    }

    public OvalVector(PointVector p, int width, int heitgh) {
        super(width);
        p.setTmp(false);
        this.heigth = heitgh;
        this.liste_points.add(p);
        this.rayon1 = new BigDecimal(1);
        this.rayon2 = rayon1;
        p.add_to_liste(this);
        this.updateBounds();
        this.name = "o"+id;
        id++;
        this.clr = new Color(255, 84, 0, 50);
    }


    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        drawName(g2d,(int)(positionPixelX(liste_points.get(1).positionX)-20),(int)(this.heigth - positionPixelY(liste_points.get(1).positionY)-20));
        if (liste_points.size()<3)this.append(new Ellipse2D.Double(positionPixelX(liste_points.get(0).positionX.subtract(rayon1)),this.heigth - positionPixelY(liste_points.get(0).positionY.add(rayon1)),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX,RoundingMode.HALF_EVEN).doubleValue(),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetY,RoundingMode.HALF_EVEN).doubleValue()),false);
        else this.append(new Ellipse2D.Double(positionPixelX(liste_points.get(0).positionX.subtract(rayon2)),this.heigth - positionPixelY(liste_points.get(0).positionY.add(rayon1)),new BigDecimal(2).multiply(rayon2).divide(DrawPanel.offSetX, RoundingMode.HALF_EVEN).doubleValue(),new BigDecimal(2).multiply(rayon1).divide(DrawPanel.offSetX,RoundingMode.HALF_EVEN).doubleValue()),false);
        AffineTransform trans = AffineTransform.getRotateInstance(rot,positionPixelX(liste_points.get(0).positionX),this.heigth - positionPixelY(liste_points.get(0).positionY));
        this.append(liste_points.get(0), false);
        this.transform(trans);
        if(isSelect()||isOver())g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        else g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        this.closePath();
        g2d.setColor(this.clr);
        this.scale();
        this.rotate();
        g2d.fill(this);
        g2d.draw(this);
        this.liste_points.get(0).update(g2d);
        if(liste_points.size()>1)this.liste_points.get(1).update(g2d);
        if(liste_points.size()>2)this.liste_points.get(2).update(g2d);
    }

    /*@Override
    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        if(!rayon1set){
            if (liste_points.size() == 1) liste_points.add(new PointVector(x,y,width,heigth,this,false));
            else {
                this.liste_points.get(1).positionX = x;
                this.liste_points.get(1).positionY = y;
            }
            rayon1 = Utilitaire.sqrt(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY))));
            //if(rayon2.equals(rayon1)) rayon2 = rayon1;
            if (set){
                rayon1set = true;
            }
            updateBounds();
            return false;
        }else {
            if (liste_points.size() == 2) liste_points.add(new PointVector(x,y,width,heigth,this,false));
            else {
                this.liste_points.get(2).positionX = x;
                this.liste_points.get(2).positionY = y;
            }
            rayon2 = Utilitaire.sqrt(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY))));


            updateBounds();
            BigDecimal yac = liste_points.get(2).positionY.subtract(liste_points.get(0).positionY);
            if(yac.doubleValue() <0)
                rot= Math.acos(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            else rot= - Math.acos(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            this.liste_points.get(1).positionX = liste_points.get(0).positionX.subtract(rayon1.multiply(new BigDecimal(Math.cos(Math.toRadians(90) - rot))));
            this.liste_points.get(1).positionY = liste_points.get(0).positionY.subtract(rayon1.multiply(new BigDecimal(Math.sin(Math.toRadians(90) - rot))));
            return true;
        }





    }*/


    /*@Override
    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        if (!rayon1set) {
            if (liste_points.size() == 1){
                liste_points.add(new PointVector(x,y,width,heigth,this,false));
                liste_points.add(new PointVector(x,y,width,heigth,this,false));
            }

            this.liste_points.get(1).positionX = x;
            this.liste_points.get(1).positionY = y;

            rayon1 = Utilitaire.sqrt(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY))));
            if(!rayon2set)rayon2 = rayon1;
            BigDecimal yac = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
            if(yac.doubleValue() <0)
                rot= Math.acos(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            else rot= - Math.acos(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            rot += Math.toRadians(270);
            this.liste_points.get(2).positionX = liste_points.get(0).positionX.add(rayon2.multiply(new BigDecimal(Math.cos(Math.toRadians(180) - rot))));
            this.liste_points.get(2).positionY = liste_points.get(0).positionY.add(rayon2.multiply(new BigDecimal(Math.sin(Math.toRadians(180) - rot))));
            rayon1set = set;
            updateBounds();
            return false;
        }else{
            this.liste_points.get(2).positionX = x;
            this.liste_points.get(2).positionY = y;
            rayon2set = set;
            rayon2 = Utilitaire.sqrt(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY))));



            BigDecimal yac = liste_points.get(2).positionY.subtract(liste_points.get(0).positionY);
            if(yac.doubleValue() <0)
                rot= Math.acos(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            else rot= - Math.acos(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            this.liste_points.get(1).positionX = liste_points.get(0).positionX.add(rayon1.multiply(new BigDecimal(Math.cos(Math.toRadians(90) - rot))));
            this.liste_points.get(1).positionY = liste_points.get(0).positionY.add(rayon1.multiply(new BigDecimal(Math.sin(Math.toRadians(90) - rot))));
            updateBounds();
            return true;
        }
    }
        @Override
    public boolean setPoint(PointVector p, boolean set) {
        if(set){
            //this.liste_points.set(2,p);
            //p.add_to_liste(this);
        }
        this.setPoint(p.positionX,p.positionY,set);
        return set;
    }*/

    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        System.out.println(set);
        return this.setPoint(new PointVector(x,y,this.width,this.heigth,this,true),set);
    }
    @Override
    public boolean setPoint(PointVector p, boolean set) {
        if (!rayon1set) {
            if (liste_points.size() == 1){
                liste_points.add(p);
                liste_points.add(new PointVector(p.positionX,p.positionY,width,heigth,this,true));
            }
            this.liste_points.set(1, p);

            PointVector pointA = this.liste_points.get(0);
            PointVector pointB = this.liste_points.get(1);
            rayon1 = Utilitaire.sqrt(pointB.positionX.subtract(pointA.positionX).multiply(pointB.positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(1).positionY.subtract(pointA.positionY).multiply(pointB.positionY.subtract(pointA.positionY))));

            BigDecimal yac = pointB.positionY.subtract(pointA.positionY);
            if(yac.doubleValue() <0)
                rot= Math.acos(pointB.positionX.subtract(pointA.positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            else rot= - Math.acos(pointB.positionX.subtract(pointA.positionX).divide(rayon1,RoundingMode.HALF_EVEN).doubleValue());
            rot += Math.toRadians(270);
            if(!rayon2set){
                rayon2 = rayon1;
                this.liste_points.get(2).positionX = pointA.positionX.add(rayon2.multiply(new BigDecimal(Math.cos(Math.toRadians(180) - rot))));
                this.liste_points.get(2).positionY = pointA.positionY.add(rayon2.multiply(new BigDecimal(Math.sin(Math.toRadians(180) - rot))));
            }

            if(set){
                rayon1set = true;
                liste_points.get(1).setTmp(false);
                liste_points.get(2).add_to_liste(this);
            }

            updateBounds();
            return false;
        }else{
            this.liste_points.set(2, p);
            PointVector pointA = this.liste_points.get(0);
            PointVector pointC = this.liste_points.get(2);

            rayon2set = set;
            rayon2 = Utilitaire.sqrt(pointC.positionX.subtract(pointA.positionX).multiply(pointC.positionX.subtract(pointA.positionX)).add(pointC.positionY.subtract(pointA.positionY).multiply(pointC.positionY.subtract(pointA.positionY))));



            BigDecimal yac = pointC.positionY.subtract(pointA.positionY);
            if(yac.doubleValue() <0)
                rot= Math.acos(pointC.positionX.subtract(pointA.positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            else rot= - Math.acos(pointC.positionX.subtract(pointA.positionX).divide(rayon2,RoundingMode.HALF_EVEN).doubleValue());
            this.liste_points.get(1).positionX = pointA.positionX.add(rayon1.multiply(new BigDecimal(Math.cos(Math.toRadians(90) - rot))));
            this.liste_points.get(1).positionY = pointA.positionY.add(rayon1.multiply(new BigDecimal(Math.sin(Math.toRadians(90) - rot))));
            updateBounds();
            if(set){
                liste_points.get(2).add_to_liste(this);
                liste_points.get(2).setTmp(false);
            }
            return true;
        }
    }

    public boolean setRayon(BigDecimal r, boolean set){
        return setPoint(liste_points.get(0).positionX.add(r),liste_points.get(0).positionY,set);
    }

    public void moveXt(double nb,FormeVue who){
        PointVector pointA = this.liste_points.get(0);
        PointVector pointB = this.liste_points.get(1);
        PointVector pointC = this.liste_points.get(2);
        pointA.moveX(nb, this);
        rayon1set = false;
        rayon2set = false;
        pointB.moveX(nb, this);
        pointC.moveX(nb, this);
        this.setPoint(pointB,true);
        this.setPoint(pointC,true);
        this.updateBounds();
    }
    public void moveYt(double nb,FormeVue who){
        PointVector pointA = this.liste_points.get(0);
        PointVector pointB = this.liste_points.get(1);
        PointVector pointC = this.liste_points.get(2);
        rayon1set = false;
        rayon2set = false;
        pointA.moveY(nb, this);
        pointB.moveY(nb,this);
        pointC.moveY(nb,this);
        this.setPoint(pointB,true);
        this.setPoint(pointC,true);
        this.updateBounds();
    }

    @Override
    public String htmlString() {
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Oval</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>"
                +"<li>"+liste_points.get(0).htmlString()+"</li>"
                +"<li>r: "+Utilitaire.posToString(rayon1)+"</li>"
                +"<li>r: "+Utilitaire.posToString(rayon2)+"</li>"
                +"</ul>";
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return null;
    }
/*
<<<<<<< HEAD
=======

    public  FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep){
            if(pointA.selectedForme(x,y,true)!= null)return pointA;
            if(pointB.selectedForme(x,y,true)!= null)return pointB;
            if(pointC.selectedForme(x,y,true)!= null)return pointC;

        }
        return this;
    }

>>>>>>> 8b7ce183090900883efc4519ff080ee4a245c8c3
*/
    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        PointVector pointB = this.liste_points.get(1);
        PointVector pointC = this.liste_points.get(2);
        if(p == pointB) {
            rayon1set = false;
            setPoint(p, true);
            pointC.moveX(howX,this);
            pointC.moveY(howY,this);
            setPoint(pointC, true);
        }
        else if(p == pointC){
            pointB.moveX(howX,this);
            pointB.moveY(howY,this);
            rayon1set = false;
            setPoint(pointB, true);
            setPoint(p, true);
        }
        if(p == this.liste_points.get(0)){
            if(who == null || !(who.getListe_points().contains(pointB) && who.getListe_points().contains(pointC))) {
                pointB.moveX(howX,this);
                pointB.moveY(howY,this);
                rayon1set = false;
                setPoint(pointB, true);
                pointC.moveX(howX,this);
                pointC.moveY(howY,this);
                setPoint(pointC, true);


            }
        }
        this.updateBounds();
    }


    @Override
    public void updateBounds() {
        boundRigthX = liste_points.get(0).positionX.add(rayon1.max(rayon2));
        boundLeftX = liste_points.get(0).positionX.subtract(rayon1.max(rayon2));
        boundBotY = liste_points.get(0).positionY.subtract(rayon1.max(rayon2));
        boundTopY = liste_points.get(0).positionY.add(rayon1.max(rayon2));
    }

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

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        //<ellipse rx="150" ry="100" cx="250" cy="150" fill="yellow" fill-opacity="0.5"/>
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "ellipse");
        //String points ="";
        /*for (PointVector p: liste_points){
            points += positionPixelX(p.positionX)+","+(this.heigth-positionPixelY(p.positionY))+",";
        }
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY));
        rectangle.setAttributeNS(null, "points", points);*/
        rectangle.setAttributeNS(null, "rx",this.rayon1.divide(DrawPanel.offSetX, BigDecimal.ROUND_HALF_EVEN).toString());
        rectangle.setAttributeNS(null, "ry",this.rayon2.divide(DrawPanel.offSetY, BigDecimal.ROUND_HALF_EVEN).toString());
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
