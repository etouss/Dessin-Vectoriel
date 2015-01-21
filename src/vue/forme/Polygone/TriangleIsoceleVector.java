package vue.forme.Polygone;

import Modele.Utilitaire;
import vue.forme.Point.PointVector;

import javax.rmi.CORBA.Util;
import java.math.BigDecimal;
import java.util.UnknownFormatConversionException;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class TriangleIsoceleVector extends PolygoneVector {
    public TriangleIsoceleVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
    }
    public TriangleIsoceleVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
    }

    public boolean setPoint(PointVector p,boolean set) {
        if (set) {
            //PointVector pointVector = new PointVector(x2,y2, width,heigth,this);
            if(liste_points.size() == 3){
                //valide = false;
                this.liste_points.set(indice,p);
                this.updatePointContraint(p,p);
                this.updateBounds();
                return true;
            }
            else {
                this.liste_points.set(indice,p);
                //liste_points.set(indice, new PointVector(x2, y2, width, heigth,this));
                indice ++;
                liste_points.add(indice,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                this.updateBounds();
                return false;
            }
        }
        else {
            if(liste_points.size() == 3){
                //cerlce ?
                updatePointContraint(liste_points.get(indice),p);
            }
            else liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }

    public void updatePointContraint(PointVector p1 , PointVector p2){
        BigDecimal ax = liste_points.get(1).positionX.subtract(liste_points.get(0).positionX);
        BigDecimal ay = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
        BigDecimal by = p2.positionY.subtract(liste_points.get(0).positionY);
        BigDecimal n = ax.multiply(ax).add(ay.multiply(ay));
        if(n.subtract(by.multiply(by)).compareTo(new BigDecimal(0)) == -1){
            this.updateBounds();
            return;
        }
        BigDecimal tmp = Utilitaire.sqrt(n.subtract(by.multiply(by)));
        BigDecimal bx1 = tmp.add(liste_points.get(0).positionX);
        BigDecimal bx2 = tmp.negate().add(liste_points.get(0).positionX);
        BigDecimal bx;
        if (bx1.subtract(p2.positionX).abs().compareTo(bx2.subtract(p2.positionX).abs()) >= 0) {
            bx = bx2;
        }
        else{
            bx = bx1;
        }
        p1.positionX = bx;
        p1.positionY = p2.positionY;
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s =  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Triangle Isocele :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }
}
