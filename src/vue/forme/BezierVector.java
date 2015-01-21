package vue.forme;

import Modele.FormeVue;
import org.w3c.dom.Document;
import vue.forme.Point.PointVector;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ByTeK on 02/01/15.
 */
public class BezierVector extends FormeVue{
    protected int indice = 1;
    private static int id = 1;

    public BezierVector(PointVector pointVector ,int width,int heigth) {
        super(width);
        this.heigth = heigth;
        pointVector.setTmp(false);
        pointVector.add_to_liste(this);
        this.liste_points = new ArrayList<>();
        this.liste_points.add(pointVector);
        this.liste_points.add(pointVector);
        this.liste_points.add(pointVector);
        this.liste_points.add(pointVector);
        //this.clr = new Color(100,100,100,50);
        this.updateBounds();
    }

    @Override
    public void updateBounds() {
        boundRigthX = liste_points.get(0).positionX;
        boundLeftX = liste_points.get(0).positionX;
        boundBotY = liste_points.get(0).positionY;
        boundTopY = liste_points.get(0).positionY;
        for(PointVector p : liste_points){
            if(p.positionX.compareTo(boundLeftX) == -1)boundLeftX = p.positionX;
            if(p.positionX.compareTo(boundRigthX) == 1)boundRigthX = p.positionX;
            if(p.positionY.compareTo(boundTopY) == 1)boundTopY = p.positionY;
            if(p.positionY.compareTo(boundBotY) == -1)boundBotY = p.positionY;
        }
    }

    @Override
    public void update(Graphics2D g2d) {
        this.reset();
        drawName(g2d,(int)((positionPixelX(liste_points.get(0).positionX)+(positionPixelX(liste_points.get(1).positionX)))/2+5),(int)((this.heigth - positionPixelY(liste_points.get(0).positionY)+(this.heigth - positionPixelY(liste_points.get(1).positionY)))/2)+5);
        this.moveTo(positionPixelX(this.liste_points.get(0).positionX), this.heigth - positionPixelY(this.liste_points.get(0).positionY));
        for(int i = 0; i<liste_points.size(); i++){
            this.liste_points.get(i).update(g2d);
        }
        this.curveTo(positionPixelX(this.liste_points.get(1).positionX),this.heigth - positionPixelY(this.liste_points.get(1).positionY),
                    positionPixelX(this.liste_points.get(2).positionX),this.heigth - positionPixelY(this.liste_points.get(2).positionY),
                    positionPixelX(this.liste_points.get(3).positionX),this.heigth - positionPixelY(this.liste_points.get(3).positionY));
        //this.append(this.listePoint.get(listePoint.size()-1),true);
        g2d.draw(this.liste_points.get(liste_points.size() - 1));
        //this.moveTo(positionPixelX(this.listePoint.get(listePoint.size()-1).positionX), this.heigth - positionPixelY(this.listePoint.get(listePoint.size() - 1).positionY));
        //this.lineTo(positionPixelX(this.liste_points.get(0).positionX), this.heigth - positionPixelY(this.liste_points.get(0).positionY));

        if(isSelect()||isOver())g2d.setStroke(overStroke);
        else g2d.setStroke(normalStroke);
        g2d.setColor(clr);
        //this.closePath();
        this.scale();
        this.rotate();
        //g2d.fill(this);
        g2d.draw(this);
    }

    @Override
    public boolean setPoint(BigDecimal x, BigDecimal y, boolean set) {
        return setPoint(new PointVector(x,y,width,heigth,null,!set),set);
    }

    @Override
    public boolean setPoint(PointVector p, boolean set) {
        if (set) {
            if (indice == 3){
                liste_points.set(indice, p);
                this.updateBounds();
                return true;
            }
            else {
                p.add_to_liste(this);
                liste_points.set(indice, p);
                indice ++;
                liste_points.set(indice,p);
                this.updateBounds();
                return false;
            }
        }
        else {
            liste_points.set(indice, p);
            this.updateBounds();
            return false;
        }
    }

    @Override
    public void moveXt(double nb, FormeVue who) {
        for (PointVector p : liste_points) {
            p.moveX(nb,this);
        }
        this.updateBounds();
        for(FormeVue q : dependance){
            q.moveX(nb,this);
        }
    }

    @Override
    public void moveYt(double nb, FormeVue who) {
        for (PointVector p : liste_points) {
            p.moveY(nb,this);
        }
        for(FormeVue q : dependance){
            q.moveY(nb,this);
        }
        this.updateBounds();
    }

    public String htmlString(){
        String s;
        String s2 = "class=over";

        s =  "<a "+(this.isOver()?s2:"")+" href='http://"+this.hashCode()+"'>Bezizer :"+this.getName()+"</a><a class=select href='http://s"+this.hashCode()+"'> S</a><a class=close href='http://x"+this.hashCode()+"'> X</a><ul>";
        for (PointVector p : liste_points) {
            s+="<li>"+p.htmlString()+"</li>";
        }
        s+="</ul>";
        return s;
    }

    @Override
    public HashSet<FormeVue> removePoint(PointVector p) {
        return null;
    }

    @Override
    public void pointMoved(PointVector p, FormeVue who, double howX, double howY) {

    }

    //<path d="M68,224 C106,77 400,62 428,231 Z" />

    public org.w3c.dom.Element createSVGElement(Document doc,String title) {
        org.w3c.dom.Element rectangle = doc.createElementNS(title, "path");
        String points ="M";
        points+=positionPixelX(liste_points.get(0).positionX)+","+(this.heigth-positionPixelY(liste_points.get(0).positionY))+" C";
        points+=positionPixelX(liste_points.get(1).positionX)+","+(this.heigth-positionPixelY(liste_points.get(1).positionY))+" ";
        points+=positionPixelX(liste_points.get(2).positionX)+","+(this.heigth-positionPixelY(liste_points.get(2).positionY))+" ";
        points+=positionPixelX(liste_points.get(3).positionX)+","+(this.heigth-positionPixelY(liste_points.get(3).positionY))+" Z";
        rectangle.setAttributeNS(null, "d", points);
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
