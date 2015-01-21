package vue.forme.Polygone;

import Modele.Utilitaire;
import vue.forme.Point.PointVector;

import java.math.BigDecimal;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class LosangeVector extends PolygoneVector {
    public LosangeVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
        liste_points.add(indice+1,new PointVector(x, y, width, heigth,this, false));
        //liste_points.add(indice+2,new PointVector(x, y, width, heigth,this, false));
    }
    public LosangeVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
        liste_points.add(indice+1,new PointVector(p.positionX, p.positionY, width, heigth,this, false));
        //liste_points.add(indice+2,new PointVector(p.positionX, p.positionY, width, heigth,this, false));
    }
    /*public boolean setPoint(BigDecimal x2,BigDecimal y2,boolean set) {
        //System.out.println("HERE2");
        if (set) {
            this.updateBounds();
            return true;
        }
        else {
            //System.out.println("HERE8");
            if(liste_points.size() == 4){
                liste_points.set(indice, new PointVector(x2, y2, width, heigth,this,!set));
                BigDecimal ax = liste_points.get(1).positionX.subtract(liste_points.get(0).positionX);
                BigDecimal ay = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
                liste_points.set(indice+1, new PointVector(x2.add(ay), y2.add(ax), width, heigth,this,!set));
                liste_points.set(indice+2,new PointVector(liste_points.get(0).positionX.add(ay), liste_points.get(0).positionY.add(ax), width, heigth,this,!set));
            }
            else liste_points.set(indice, new PointVector(x2, y2, width, heigth,this,!set));
            this.updateBounds();
            return false;
        }
    }*/

    public boolean setPoint(PointVector p,boolean set) {
        if (set) {
            if(liste_points.size() == 4){
                this.liste_points.set(indice,p);
                updatePointContraint();
                this.liste_points.get(indice+1).setTmp(false);
                //this.liste_points.get(indice+2).setTmp(false);
                this.updateBounds();
                return true;
            }
            else {
                liste_points.set(indice, p);
                indice ++;
                liste_points.add(indice,new PointVector(p.positionX, p.positionY.add(new BigDecimal(1)), width, heigth,this,true));
                if(liste_points.size() == 3){
                    liste_points.add(indice+1,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                }
                this.updateBounds();
                return false;
            }
        }
        else {
            if(liste_points.size() == 4){
                liste_points.set(indice, p);
                updatePointContraint();
            }
            else liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }

    public void updatePointContraint(){
        BigDecimal x01 = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX);
        BigDecimal y01 = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY);
        BigDecimal x12 = liste_points.get(1).positionX.subtract(liste_points.get(2).positionX);
        BigDecimal y12 = liste_points.get(1).positionY.subtract(liste_points.get(2).positionY);

        if(y12.compareTo(new BigDecimal(0)) == 0)return;

        BigDecimal rapport = x12.divide(y12, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal norme = x01.multiply(x01).add(y01.multiply(y01));

        BigDecimal ny12 = Utilitaire.sqrt(norme.divide(new BigDecimal(1).add(rapport.multiply(rapport)),BigDecimal.ROUND_HALF_EVEN));
        if(rapport.compareTo(new BigDecimal(1)) > 0)
            ny12 = ny12.negate();
        if(rapport.compareTo(new BigDecimal(0)) > 0)
            ny12 = ny12.negate();
        if(rapport.compareTo(new BigDecimal(-1)) > 0)
            ny12 = ny12.negate();

        BigDecimal nx12 = ny12.multiply(rapport);

        BigDecimal x2 = liste_points.get(1).positionX.subtract(nx12);
        BigDecimal y2 = liste_points.get(1).positionY.subtract(ny12);

        BigDecimal x3 = liste_points.get(0).positionX.subtract(nx12);
        BigDecimal y3 = liste_points.get(0).positionY.subtract(ny12);

        liste_points.get(indice).positionX = x2;
        liste_points.get(indice).positionY = y2;
        liste_points.get(indice+1).positionX = x3;
        liste_points.get(indice+1).positionY = y3;
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s=  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Losange :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }
}
