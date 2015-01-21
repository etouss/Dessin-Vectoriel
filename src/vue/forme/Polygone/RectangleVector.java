package vue.forme.Polygone;

import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class RectangleVector extends PolygoneVector {
    public RectangleVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
    }
    public RectangleVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
    }
    public boolean setPoint(PointVector p,boolean set) {
        if (set) {
            if(liste_points.size() == 4){
                this.liste_points.set(indice,p);
                this.updatePointContraint(p);
                this.liste_points.get(indice+1).setTmp(false);
                //this.liste_points.get(indice+2).setTmp(false);
                this.updateBounds();
                return true;
            }
            else {
                liste_points.set(indice, p);
                indice ++;
                liste_points.add(indice,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                if(liste_points.size() == 3){
                    liste_points.add(indice+1,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                }
                this.updateBounds();
                return false;
            }
        }
        else {
            if(liste_points.size() == 4){
                updatePointContraint(p);
                /*BigDecimal ax = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX);
                BigDecimal ay = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY);
                BigDecimal by = p.positionY.subtract(liste_points.get(1).positionY);
                BigDecimal x = ay.multiply(by).divide(ax,BigDecimal.ROUND_HALF_EVEN).negate().add(liste_points.get(1).positionX);
                liste_points.get(indice).positionX = x;
                liste_points.get(indice).positionY = p.positionY;
                liste_points.get(indice+1).positionX = x.add(ax);
                liste_points.get(indice+1).positionY = p.positionY.add(ay);*/
                //liste_points.set(indice, new PointVector(x, y2, width, heigth,this,false));
                //liste_points.set(indice+1,new PointVector(x.add(ax), y2.add(ay), width, heigth,this,false));
            }
            else liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }

    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {
        if(who != this && (grav==null || p!= grav)){
            ArrayList<PointVector> points = oposee(p);
            points.get(0).moveXt(howX/2+howY/2,this);
            points.get(0).moveYt(howY/2-howX/2, this);
            points.get(1).moveXt(howX/2-howY/2,this);
            points.get(1).moveYt(howX/2+howY/2, this);
            if(grav!=null)setGrav();
        }
    }

    public void updatePointContraint(PointVector p){
        BigDecimal ax = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX);
        BigDecimal ay = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY);
        BigDecimal by = p.positionY.subtract(liste_points.get(1).positionY);
        BigDecimal x = ay.multiply(by).divide(ax,BigDecimal.ROUND_HALF_EVEN).negate().add(liste_points.get(1).positionX);
        liste_points.get(indice).positionX = x;
        liste_points.get(indice).positionY = p.positionY;
        liste_points.get(indice+1).positionX = x.add(ax);
        liste_points.get(indice+1).positionY = p.positionY.add(ay);
    }

    private ArrayList<PointVector> oposee(PointVector p){
        ArrayList<PointVector> retour = new ArrayList<>();
        switch (liste_points.indexOf(p)){
            case 0:
                retour.add(liste_points.get(3));
                retour.add(liste_points.get(1));
                return retour;
            case 1:
                retour.add(liste_points.get(0));
                retour.add(liste_points.get(2));
                return retour;
            case 2:
                retour.add(liste_points.get(1));
                retour.add(liste_points.get(3));
                return retour;
            case 3:
                retour.add(liste_points.get(2));
                retour.add(liste_points.get(0));
                return retour;
            default:
                return null;
        }
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s = "<a "+(this.isOver()?s2:" ")+" href='http://"+this.hashCode()+"'>Rectangle :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }

}
