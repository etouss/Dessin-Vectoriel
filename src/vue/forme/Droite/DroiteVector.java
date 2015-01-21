package vue.forme.Droite;

import Modele.DoubleOperation;
import Modele.FormeNotFoundExeption;
import Modele.Utilitaire;
import org.w3c.dom.Document;
import vue.DrawPanel;
import Modele.FormeVue;
import vue.forme.Point.PointInterVector;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * Created by ByTeK on 18/11/2014.
 */
public class DroiteVector extends FormeVue {
    public BigDecimal coefDir;
    public BigDecimal origine;
    //PointVector pointA;
    //PointVector pointB;
    DoubleOperation iop;
    boolean vertical = false;
    public HashSet<PointInterVector> inter = new HashSet<PointInterVector>();
    public static int id = 1;

    public DroiteVector(PointVector p,int width,int heigth){
        super(width);
        p.setTmp(false);
        p.add_to_liste(this);
        this.heigth = heigth;
        liste_points.add(p);
        //this.pointA = new PointVector(x,y,width,heigth,this,false);
        liste_points.add(new PointVector(p.positionX,p.positionY,width,heigth,this,true));
        //this.pointB = new PointVector(x,y,width,heigth,this,true);
        this.coefDir = new BigDecimal(0);
        this.origine = new BigDecimal(0);
        iop = (x1 -> coefDir.multiply(x1).add(origine));
        this.named("d"+id);
        id++;
    }

    public DroiteVector(PointVector p,BigDecimal coefDir,int width,int heigth){
        super(width);
        p.setTmp(false);
        p.add_to_liste(this);
        this.heigth = heigth;
        liste_points.add(p);
        BigDecimal xB = p.positionX.add(DrawPanel.offSetX.multiply(new BigDecimal(50)));
        BigDecimal yB = p.positionY.add(coefDir.multiply(DrawPanel.offSetX.multiply(new BigDecimal(50))));
        PointVector p2 = new PointVector(xB,yB,width,heigth,this,false);
        p2.add_to_liste(this);
        liste_points.add(p2);
        this.coefDir = liste_points.get(0).positionY.subtract(yB).divide(liste_points.get(0).positionX.subtract(xB),RoundingMode.HALF_EVEN);
        this.origine = liste_points.get(0).positionY.subtract(this.coefDir.multiply(liste_points.get(0).positionX));
        iop = (x1 -> coefDir.multiply(x1).add(origine));
        this.named("d"+id);
        id++;
    }

    public DroiteVector(BigDecimal x,BigDecimal y,int width,int heigth) {
        this(new PointVector(x,y,width,heigth,null,false),width,heigth);
    }

    public boolean inBounds(BigDecimal top,BigDecimal bot,BigDecimal rigth,BigDecimal left) {
        this.updateBounds();
        return super.inBounds(top,bot,rigth,left);
    }

    public boolean existInter(DroiteVector d){
        for (PointInterVector p : d.inter){
            if(this.inter.contains(p))return true;
        }
        return false;
    }

    public void update(Graphics2D g2d) {
        PointVector pointA = liste_points.get(0);
        PointVector pointB = liste_points.get(1);
        //this.append(liste_points.,false);
        //this.append(pointB,false);
        drawName(g2d,(int)(positionPixelX(liste_points.get(1).positionX)-20),(int)(this.heigth - positionPixelY(liste_points.get(1).positionY)-20));
        this.reset();
        if (vertical) {
            this.moveTo(positionPixelX(pointA.positionX), 0);
            this.lineTo(positionPixelX(pointA.positionX), this.heigth);
        } else {
            this.moveTo(0, this.heigth - positionPixelY(iop.operate(DrawPanel.positionX)));
            this.lineTo(this.width, this.heigth - positionPixelY(iop.operate(DrawPanel.offSetX.multiply(new BigDecimal(this.width)).add(DrawPanel.positionX))));
        }
        if (isOver() || isSelect()) g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        else g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(clr);
        this.scale();
        this.rotate();
        g2d.draw(this);
        pointA.update(g2d);
        pointB.update(g2d);

    }
    public boolean setPoint(BigDecimal x2,BigDecimal y2,boolean set){
        PointVector pointA = liste_points.get(0);
        if(x2.equals(pointA.positionX)){
            this.coefDir = new BigDecimal(0);
            this.origine = new BigDecimal(0);
            iop = (x -> coefDir.multiply(x).add(origine));
            vertical = true;
        }
        else {
            vertical = false;
            this.liste_points.get(1).positionX = x2;
            this.liste_points.get(1).positionY = y2;
            this.coefDir = pointA.positionY.subtract(y2).divide(pointA.positionX.subtract(x2),RoundingMode.HALF_EVEN);
            this.origine = pointA.positionY.subtract(this.coefDir.multiply(pointA.positionX));
            iop = (x -> coefDir.multiply(x).add(origine));
            this.updateBounds();
        }
        if(set){
            this.liste_points.get(1).setTmp(false);
        }
        return true;
    }

    @Override
    public boolean setPoint(PointVector p, boolean set) {
        if(set){
            this.liste_points.set(1,p);
            p.add_to_liste(this);
        }
        this.setPoint(p.positionX,p.positionY,set);
        return set;
    }

    public  void moveXt(double nb,FormeVue who){
        this.liste_points.get(0).moveX(nb,this);
        this.liste_points.get(1).moveX(nb,this);
        //this.coefDir = this.liste_points.get(0).positionY.subtract(y2).divide(this.liste_points.get(0).positionX.subtract(x2),RoundingMode.HALF_EVEN);
        this.origine = this.liste_points.get(0).positionY.subtract(this.coefDir.multiply(this.liste_points.get(0).positionX));
        for(PointInterVector p1 : inter) {
            try {
                p1.setInter();
            } catch (FormeNotFoundExeption formeNotFoundExeption) {
                //remove si paralele
                formeNotFoundExeption.printStackTrace();
            }
        }
        for(FormeVue q : dependance){
            q.moveDependance(this);
        }
        this.updateBounds();
    }
    public   void moveYt(double nb,FormeVue who){
        this.liste_points.get(0).moveY(nb,this);
        this.liste_points.get(1).moveY(nb,this);
        //this.coefDir = this.liste_points.get(0).positionY.subtract(y2).divide(this.liste_points.get(0).positionX.subtract(x2),RoundingMode.HALF_EVEN);
        //this.origine = this.liste_points.get(0).positionY.subtract(this.coefDir.multiply(this.liste_points.get(0).positionX));
        this.origine = this.liste_points.get(0).positionY.subtract(this.coefDir.multiply(this.liste_points.get(0).positionX));
        for(PointInterVector p1 : inter) {
            try {
                p1.setInter();
            } catch (FormeNotFoundExeption formeNotFoundExeption) {
                //remove si paralele
                formeNotFoundExeption.printStackTrace();
            }
        }
        for(FormeVue q : dependance){
            q.moveDependance(this);
        }
        this.updateBounds();
    }
    public String htmlString(){
        String s = "class=over";

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Droite</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><br>" +
                "<ul>"
                +"<li>a: "+Utilitaire.posToString(coefDir)+"</li>"
                +"<li>b: "+Utilitaire.posToString(origine)+"</li>"
                +"</ul>";
    }

    @Override
    public HashSet<FormeVue> delete(FormeVue who) {
        HashSet<FormeVue> retour = super.delete(this);
        for(PointInterVector p : inter){
            retour.addAll(p.delete(this));
            //inter.remove(p);
        }
        return retour;
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        if(!inter.contains(p))return this.delete(this);
        else {
            inter.remove(p);
            return new HashSet<FormeVue>();
        }
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        PointVector pointB = liste_points.get(1);
        setPoint(pointB.positionX, pointB.positionY, true);
        for(PointInterVector p1 : inter) {
            try {
                p1.setInter();
            } catch (FormeNotFoundExeption formeNotFoundExeption) {
                //remove si paralele
                formeNotFoundExeption.printStackTrace();
            }
        }
        for(FormeVue q : dependance){
            q.moveDependance(this);
        }
        this.updateBounds();
    }


    public void setInter(PointInterVector p){
        this.inter.add(p);
    }

    public void updateBounds(){
        boundLeftX = DrawPanel.positionX;
        boundRigthX = DrawPanel.offSetX.multiply(new BigDecimal(this.width)).add(DrawPanel.positionX);
        if(iop.operate(boundRigthX).compareTo(iop.operate(boundLeftX)) >= 0){
            boundTopY = iop.operate(boundRigthX);
            boundBotY = iop.operate(boundLeftX);
        }
        else{
            boundBotY = iop.operate(boundRigthX);
            boundTopY = iop.operate(boundLeftX);
        }
        if(vertical){
            boundLeftX = this.liste_points.get(0).positionX;
            boundRigthX = this.liste_points.get(0).positionX.add(new BigDecimal(2).multiply(DrawPanel.offSetX));
            boundBotY = this.liste_points.get(0).positionY;
            boundTopY = this.liste_points.get(0).positionY.add(new BigDecimal(heigth).multiply(DrawPanel.offSetY));
        }
    }

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        //<line x1="50" y1="50" x2="150" y2="400" stroke="blue" stroke-linecap="round"/>
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "line");
        //String points ="";
        /*for (PointVector p: liste_points){
            points += positionPixelX(p.positionX)+","+(this.heigth-positionPixelY(p.positionY))+",";
        }
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY));
        rectangle.setAttributeNS(null, "points", points);*/
        rectangle.setAttributeNS(null, "stroke","red");
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
