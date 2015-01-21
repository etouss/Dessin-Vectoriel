package vue.forme.Droite;

import Modele.FormeNotFoundExeption;
import Modele.FormeVue;
import Modele.Utilitaire;
import vue.DrawPanel;
import vue.forme.Point.PointInterVector;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ByTeK on 21/11/2014.
 */
public class DroiteParaVector extends DroiteVector {
    //private DroiteVector droite;
    public DroiteParaVector(DroiteVector d){
        super(d.getListe_points().get(0).positionX,d.getListe_points().get(0).positionY,d.getWidth(),d.getHeigth());
        super.setPoint(d.getListe_points().get(1).positionX, d.getListe_points().get(1).positionY, false);
        this.coefDir = d.coefDir;
        this.liste_points.get(0).setTmp(true);
        //this.movable = false;
        //this.droite = d;
        this.clr = new Color(255, 89, 75, 255);
        d.setDependance(this);
        this.named("para " + d.getName());
    }

    public boolean setPoint(BigDecimal x2,BigDecimal y2,boolean set){
        this.liste_points.get(1).positionX = x2;
        this.liste_points.get(1).positionY = y2;
        this.origine = y2.subtract(this.coefDir.multiply(x2));
        iop = (x -> coefDir.multiply(x).add(origine));
        this.liste_points.get(1).setTmp(false);
        return true;
    }

    public void moveDependance(DroiteVector d){
        this.coefDir = d.coefDir;
        this.iop = (x->coefDir.multiply(x).add(origine));
        this.liste_points.get(1).positionY = this.coefDir.multiply(this.liste_points.get(1).positionX).add(origine);
        /*for(FormeVue q : dependance){
            q.moveDependance(this);
        }*/
        //super.moveDependance(d);
        for(FormeVue q : dependance){
            q.moveDependance(this);
        }
        for(PointInterVector p1 : inter) {
            try {
                p1.setInter();
            } catch (FormeNotFoundExeption formeNotFoundExeption) {
                //remove si paralele
                formeNotFoundExeption.printStackTrace();
            }
        }
    }

    public void dependanceMoved(PointVector p){
        //this.coefDir = droite.coefDir;
        this.origine = p.positionY.subtract(this.coefDir.multiply(p.positionX));
        iop = (x -> coefDir.multiply(x).add(origine));
        for(PointInterVector p1 : inter) {
            try {
                p1.setInter();
            } catch (FormeNotFoundExeption formeNotFoundExeption) {
                //remove si paralele
                formeNotFoundExeption.printStackTrace();
            }
        }
        //super.dependanceMoved(p);
    }
    /*public HashSet<FormeVue> delete(boolean utilisateur) {
        removeDependance(droite);
        return super.delete(utilisateur);
    }
    */

    public  void moveXt(double nb,FormeVue who){
        //this.liste_points.get(0).moveX(nb,this);
        this.liste_points.get(1).moveX(nb,this);
        //this.coefDir = this.liste_points.get(0).positionY.subtract(y2).divide(this.liste_points.get(0).positionX.subtract(x2),RoundingMode.HALF_EVEN);
        this.origine = this.liste_points.get(1).positionY.subtract(this.coefDir.multiply(this.liste_points.get(1).positionX));
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
        //this.liste_points.get(0).moveY(nb,this);
        this.liste_points.get(1).moveY(nb,this);
        //this.coefDir = this.liste_points.get(0).positionY.subtract(y2).divide(this.liste_points.get(0).positionX.subtract(x2),RoundingMode.HALF_EVEN);
        //this.origine = this.liste_points.get(0).positionY.subtract(this.coefDir.multiply(this.liste_points.get(0).positionX));
        this.origine = this.liste_points.get(1).positionY.subtract(this.coefDir.multiply(this.liste_points.get(1).positionX));
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

        return "<a "+(this.isOver()?s:"")+" href='http://"+this.hashCode()+"'>Droite parallèle</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><br>" +
                "<ul>"
                +"<li>a: "+ Utilitaire.posToString(coefDir)+"</li>"
                +"<li>b: "+Utilitaire.posToString(origine)+"</li>"
                +"</ul>";
    }

}
