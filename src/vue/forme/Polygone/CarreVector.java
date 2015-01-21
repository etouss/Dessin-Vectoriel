package vue.forme.Polygone;

import Modele.FormeVue;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class CarreVector extends PolygoneVector {
    public CarreVector(BigDecimal x, BigDecimal y, int width, int heitgh) {
        super(x, y, width, heitgh);
        liste_points.add(indice+1,new PointVector(x, y, width, heigth,this,false));
        liste_points.add(indice+2,new PointVector(x, y, width, heigth,this,false));
        //liste_points.add(indice+3,new PointVector(x, y, width, heigth,this,false));
    }
    public CarreVector(PointVector p, int width, int heitgh) {
        super(p, width, heitgh);
        liste_points.add(indice+1,new PointVector(p.positionX, p.positionY, width, heigth,this,false));
        liste_points.add(indice+2,new PointVector(p.positionX, p.positionY, width, heigth,this,false));
        //liste_points.add(indice+3,new PointVector(x, y, width, heigth,this,false));
    }


    public boolean setPoint(PointVector p,boolean set) {
        //System.out.println("HERE2");
        if (set) {
            liste_points.set(indice, p);
            this.updateBounds();
            return true;
        }
        else {
            //System.out.println(indice);
            if(liste_points.size() == 4){
                //liste_points.get(indice).positionX = p.positionX;
                //liste_points.get(indice).positionY = p.positionY;
                //liste_points.get(indice).setTmp(false);
                liste_points.set(indice, p);
                BigDecimal ax = liste_points.get(1).positionX.subtract(liste_points.get(0).positionX);
                BigDecimal ay = liste_points.get(1).positionY.subtract(liste_points.get(0).positionY);
                liste_points.get(indice+1).positionX = p.positionX.subtract(ay);
                liste_points.get(indice+1).positionY = p.positionY.add(ax);
                //liste_points.get(indice+1).setTmp(false);
                //liste_points.set(indice+1, new PointVector(x2.subtract(ay), y2.add(ax), width, heigth,this,false));
                liste_points.get(indice+2).positionX = liste_points.get(0).positionX.subtract(ay);
                liste_points.get(indice+2).positionY = liste_points.get(0).positionY.add(ax);
                //liste_points.get(indice+2).setTmp(false);
                //liste_points.set(indice+2,new PointVector(liste_points.get(0).positionX.subtract(ay), liste_points.get(0).positionY.add(ax), width, heigth,this,false));
            }
            this.updateBounds();
            return false;
        }
    }


    @Override
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

    private ArrayList<PointVector> oposee(PointVector p){
        ArrayList<PointVector> retour = new ArrayList<PointVector>();
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

        s =  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Carre :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }
}
