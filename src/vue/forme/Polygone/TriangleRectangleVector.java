package vue.forme.Polygone;

import Modele.Utilitaire;
import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.math.BigDecimal;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class TriangleRectangleVector extends PolygoneVector {
    public TriangleRectangleVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
    }

    public TriangleRectangleVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
    }

    public boolean setPoint(PointVector p,boolean set) {
        if (set) {
            //PointVector pointVector = new PointVector(x2,y2, width,heigth,this,!set);
            if(liste_points.size() == 3){
                //valide = false;
                this.liste_points.set(indice,p);
                updatePointContraint(p,p);
                this.updateBounds();
                return true;
            }
            else {
                liste_points.set(indice, p);
                indice ++;
                liste_points.add(indice,new PointVector(p.positionX, p.positionY, width, heigth,this,true));
                this.updateBounds();
                return false;
            }
        }
        else {
            if(liste_points.size() == 3){
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
        BigDecimal x = ay.multiply(by).divide(ax,BigDecimal.ROUND_HALF_EVEN).negate().add(liste_points.get(0).positionX);
        p1.positionX = x;
        p1.positionY = p2.positionY;
    }

    /*
    public void dependanceMoved(PointVector p) {
        if(p == liste_points.get(1) && !all) {
            BigDecimal c = liste_points.get(2).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(2).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(2).positionY.subtract(liste_points.get(0).positionY)));
            //Intersection droite et cercle puis le plus prêt.
            BigDecimal ax = liste_points.get(1).positionX.subtract(liste_points.get(0).positionX);
            BigDecimal ay = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
            //BigDecimal by = y2.subtract(liste_points.get(0).positionY);
            BigDecimal a = ay.divide(ax, BigDecimal.ROUND_HALF_EVEN).negate();
            BigDecimal b = liste_points.get(0).positionX.subtract(a.multiply(liste_points.get(0).positionY));
            //System.out.println(b);

            BigDecimal a2 = a.multiply(a).add(new BigDecimal(1));
            BigDecimal a1 = new BigDecimal(2).multiply(a.multiply(b).subtract(a.multiply(liste_points.get(0).positionX)).subtract(liste_points.get(0).positionY));
            BigDecimal a0 = b.multiply(b).add(liste_points.get(0).positionX.multiply(liste_points.get(0).positionX)).add(liste_points.get(0).positionY.multiply(liste_points.get(0).positionY)).subtract(new BigDecimal(2).multiply(liste_points.get(0).positionX).multiply(b)).subtract(c);

            BigDecimal delta = a1.multiply(a1).subtract(new BigDecimal(4).multiply(a2).multiply(a0));
            //System.out.println(delta);


            BigDecimal y1 = a1.negate().add(Utilitaire.sqrt(delta)).divide(new BigDecimal(2).multiply(a2), BigDecimal.ROUND_HALF_EVEN);
            BigDecimal y2 = a1.negate().subtract(Utilitaire.sqrt(delta)).divide(new BigDecimal(2).multiply(a2), BigDecimal.ROUND_HALF_EVEN);
            BigDecimal x1 = a.multiply(y1).add(b);
            BigDecimal x2 = a.multiply(y2).add(b);

            if (x1.subtract(liste_points.get(2).positionX).abs().add(y1.subtract(liste_points.get(2).positionY).abs()).compareTo(x2.subtract(liste_points.get(2).positionX).abs().add(y2.subtract(liste_points.get(2).positionY).abs())) >= 0) {
              liste_points.get(2).positionX = x2;
              liste_points.get(2).positionY = y2;
            }
            else{
              liste_points.get(2).positionX = x1;
              liste_points.get(2).positionY = y1;
            }
        }
        else if(p == liste_points.get(2) && !all){
            BigDecimal c = liste_points.get(1).positionX.subtract(liste_points.get(0).positionX).multiply(liste_points.get(1).positionX.subtract(liste_points.get(0).positionX)).add(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY).multiply(liste_points.get(1).positionY.subtract(liste_points.get(0).positionY)));
            //Intersection droite et cercle puis le plus prêt.
            BigDecimal ax = liste_points.get(2).positionX.subtract(liste_points.get(0).positionX);
            BigDecimal ay = liste_points.get(2).positionY.subtract(liste_points.get(0).positionY);
            //BigDecimal by = y2.subtract(liste_points.get(0).positionY);
            BigDecimal a = ay.divide(ax, BigDecimal.ROUND_HALF_EVEN).negate();
            BigDecimal b = liste_points.get(0).positionX.subtract(a.multiply(liste_points.get(0).positionY));
            //System.out.println(b);

            BigDecimal a2 = a.multiply(a).add(new BigDecimal(1));
            BigDecimal a1 = new BigDecimal(2).multiply(a.multiply(b).subtract(a.multiply(liste_points.get(0).positionX)).subtract(liste_points.get(0).positionY));
            BigDecimal a0 = b.multiply(b).add(liste_points.get(0).positionX.multiply(liste_points.get(0).positionX)).add(liste_points.get(0).positionY.multiply(liste_points.get(0).positionY)).subtract(new BigDecimal(2).multiply(liste_points.get(0).positionX).multiply(b)).subtract(c);

            BigDecimal delta = a1.multiply(a1).subtract(new BigDecimal(4).multiply(a2).multiply(a0));
            //System.out.println(delta);


            BigDecimal y1 = a1.negate().add(Utilitaire.sqrt(delta)).divide(new BigDecimal(2).multiply(a2), BigDecimal.ROUND_HALF_EVEN);
            BigDecimal y2 = a1.negate().subtract(Utilitaire.sqrt(delta)).divide(new BigDecimal(2).multiply(a2), BigDecimal.ROUND_HALF_EVEN);
            BigDecimal x1 = a.multiply(y1).add(b);
            BigDecimal x2 = a.multiply(y2).add(b);

            if (x1.subtract(liste_points.get(1).positionX).abs().add(y1.subtract(liste_points.get(1).positionY).abs()).compareTo(x2.subtract(liste_points.get(1).positionX).abs().add(y2.subtract(liste_points.get(1).positionY).abs())) >= 0) {
              liste_points.get(1).positionX = x2;
              liste_points.get(1).positionY = y2;
            }
            else{
              liste_points.get(1).positionX = x1;
              liste_points.get(1).positionY = y1;
            }
        }
        super.dependanceMoved(p);
        this.updateBounds();
    }
    */
    public FormeVue selectedForme(BigDecimal x , BigDecimal y,boolean deep){
        if(deep) {
            for (PointVector p : liste_points) {
                if (x.subtract(p.positionX).abs().compareTo(new BigDecimal(5)) ==  -1 && y.subtract(p.positionY).abs().compareTo(new BigDecimal(5)) ==  -1 && p != liste_points.get(0))
                    return p;
            }
        }
        return this;
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s = "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Triangle Rectangle :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }

}
