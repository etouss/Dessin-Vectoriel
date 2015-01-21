package vue.forme.Polygone;

import vue.forme.Point.PointVector;

import java.math.BigDecimal;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class ParalelogrameVector extends PolygoneVector {
    public ParalelogrameVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
    }
    public ParalelogrameVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
    }


    public boolean setPoint(PointVector p,boolean set) {
        if (set) {
            if(liste_points.size() == 4){
                liste_points.set(indice,p);
                liste_points.get(indice+1).setTmp(false);
                this.updateBounds();
                return true;
            }
            else {
                liste_points.set(indice, p);
                indice ++;
                liste_points.add(indice,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                if(liste_points.size() == 3){
                  liste_points.add(indice+1,new PointVector(p.positionX, p.positionY ,width, heigth,this,true));
                }
                this.updateBounds();
                return false;
            }
        }
        else {
            if(liste_points.size() == 4){
                BigDecimal ax = liste_points.get(0).positionX.subtract(liste_points.get(1).positionX);
                BigDecimal ay = liste_points.get(0).positionY.subtract(liste_points.get(1).positionY);
                //BigDecimal by = y2.subtract(liste_points.get(1).positionY);
                //BigDecimal x = ay.multiply(by).divide(ax,BigDecimal.ROUND_HALF_EVEN).negate().add(liste_points.get(1).positionX);
                liste_points.set(indice, p);
                liste_points.get(indice+1).positionX = p.positionX.add(ax);
                liste_points.get(indice+1).positionY = p.positionY.add(ay);
                //liste_points.set(indice+1,new PointVector(x2.add(ax), y2.add(ay), width, heigth,this,!set));
            }
            else liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }


    public String htmlString(){
        String s;
        String s2 = "class=over";

        s =  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Paralelograme :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }
}
