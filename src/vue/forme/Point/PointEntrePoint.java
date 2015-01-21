package vue.forme.Point;

import Modele.FormeNotFoundExeption;
import Modele.FormeVue;
import vue.DrawPanel;
import vue.forme.SegmentVector;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * Created by ByTeK on 22/12/14.
 */
public class PointEntrePoint extends PointVector {

    public BigDecimal coef;
    public double ratio;
    public boolean in;

    public PointEntrePoint(PointVector p1 ,PointVector p2,boolean in) {
        super(p1.positionX, p2.positionY, p1.getWidth(), p1.getHeigth(), null, false);
        //p1.setDependance(this);
        //p2.setDependance(this);
        this.liste_points.add(p1);
        this.liste_points.add(p2);
        this.liste_points.get(0).liste_entrePoint.add(this);
        this.liste_points.get(1).liste_entrePoint.add(this);
        this.positionX = liste_points.get(0).positionX.add(liste_points.get(1).positionX).divide(new BigDecimal(2));
        this.positionY = liste_points.get(0).positionY.add(liste_points.get(1).positionY).divide(new BigDecimal(2));
        BigDecimal ab = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX).multiply(liste_points.get(0).positionX.subtract(liste_points.get(1).positionX)).add(liste_points.get(0).positionY.subtract(liste_points.get(1).positionY).multiply(liste_points.get(0).positionY.subtract(liste_points.get(1).positionY)));
        BigDecimal ag = liste_points.get(0).positionX.subtract(this.positionX).multiply(liste_points.get(0).positionX.subtract(this.positionX)).add(liste_points.get(0).positionY.subtract(this.positionY).multiply(liste_points.get(0).positionY.subtract(this.positionY)));
        this.ratio = Math.sqrt(ag.divide(ab, RoundingMode.HALF_EVEN).doubleValue());
        this.coef = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY).divide(liste_points.get(0).positionX.subtract(liste_points.get(1).positionX),RoundingMode.HALF_EVEN);
        this.in = in;
        this.inQuadra = false;
        //System.out.println(this.ratio);
    }

    public void set_ratio(BigDecimal b){
        ratio = b.doubleValue();
    }

    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        if(p == liste_points.get(0)){
            this.moveX(howX*(1-ratio),this);
            this.moveY(howY *(1-ratio), this);
        }
        else {
            this.moveX(howX*ratio,this);
            this.moveY(howY*ratio,this);
        }
        this.coef = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY).divide(liste_points.get(0).positionX.subtract(liste_points.get(1).positionX),RoundingMode.HALF_EVEN);
    }

    public  void moveXt(double nb, FormeVue who){
        if(who == null) {
            positionX = positionX.add(DrawPanel.offSetX.multiply(new BigDecimal(nb)));
            positionY = positionY.add(DrawPanel.offSetY.multiply(new BigDecimal(nb)).multiply(coef));
            if (!liste_formeVue.isEmpty()) {
                for (FormeVue f : liste_formeVue) {
                    if(f!=who)f.pointMoved(this, who, nb, 0);
                    if(f!=who)f.pointMoved(this, who, 0, nb * coef.doubleValue());
                }
            }
            BigDecimal ab = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX).multiply(liste_points.get(0).positionX.subtract(liste_points.get(1).positionX)).add(liste_points.get(0).positionY.subtract(liste_points.get(1).positionY).multiply(liste_points.get(0).positionY.subtract(liste_points.get(1).positionY)));
            BigDecimal ag = liste_points.get(0).positionX.subtract(this.positionX).multiply(liste_points.get(0).positionX.subtract(this.positionX)).add(liste_points.get(0).positionY.subtract(this.positionY).multiply(liste_points.get(0).positionY.subtract(this.positionY)));
            this.ratio = Math.sqrt(ag.divide(ab, RoundingMode.HALF_EVEN).doubleValue());
            if(ratio > 1 && in){
                this.ratio = 1;
                this.positionX = liste_points.get(1).positionX;
                this.positionY = liste_points.get(1).positionY;
            }
            else if(liste_points.get(0).positionX.subtract(this.positionX).multiply(liste_points.get(0).positionX.subtract(liste_points.get(1).positionX)).compareTo(new BigDecimal(0))<0){
                if(in) {
                    this.ratio = 0;
                    this.positionX = liste_points.get(0).positionX;
                    this.positionY = liste_points.get(0).positionY;
                }
                else{
                    this.ratio = -this.ratio;
                }
            }
            this.updateBounds();
        }
        else{
            super.moveXt(nb,who);
        }
        System.out.println(ratio);
    }
    public  void moveYt(double nb,FormeVue who){
        if(who == null) {

        }
        else{
            super.moveYt(nb,who);
        }
    }
    public void fuse(PointVector p) throws FormeNotFoundExeption {
        if(liste_points.contains(p))throw  new FormeNotFoundExeption();
        super.fuse(p);
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return this.delete(null);
    }

    public  HashSet<FormeVue> delete(FormeVue who) {
        HashSet<FormeVue> retour = new HashSet<FormeVue>();
        for (FormeVue q : dependance) {
            if(q!=this)retour.addAll(q.delete(this));
        }
        retour.add(this);
        if(who == null) {
            HashSet<FormeVue> delForme = new HashSet<FormeVue>();
            for (FormeVue f : liste_formeVue){
                delForme.addAll(f.removePoint(this));
            }
            liste_formeVue.removeAll(delForme);
            retour.addAll(delForme);
            retour.add(this);
        }
        if(who != null){
            liste_formeVue.remove(who);
            if(liste_formeVue.size() == 0) retour.add(this);
        }
        return retour;
    }

    public boolean mouseOn(double x, double y) {
        BasicStroke pathStroke = new BasicStroke(1);
        Shape clippedPath = pathStroke.createStrokedShape(this);
        return !tmp && clippedPath.intersects(x - 7, y - 7, 14, 14);
    }


}
