package Modele;

import org.w3c.dom.Document;
import vue.DrawPanel;
import vue.forme.Droite.DroiteVector;
import vue.forme.Point.PointVector;

import javax.lang.model.element.Element;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Ici on commence les chose sérieuse:
 * Forme vue est la classe abstraite par défault de tout le logiciel chacun des forme afficher sur le droit panel
 * herite de celle ci.
 * Cette derniere implémente certaine méthode par défault commune a la pluspart des figure:
 * Elle force egalement ces fille a implémenter des méthode tel que
 * update qui explique leur facon de s'afficher
 * setPoint qui leur demande la facon de se creer
 * updateBounds pour savoir si il doicent etre afficher
 * moveX moveY pour comprendre leur facon de bouger
 * et enfin pointMoved pour expliquer leur facon de traiter le mouvement d'un de leur point.
 */

public abstract class FormeVue extends Path2D.Double{

    protected static final Stroke normalStroke = new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    protected static final Stroke overStroke = new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    protected Color clr = Color.black;
    protected int heigth;
    protected int width;
    protected boolean over;
    protected boolean select;
    protected boolean valide = true;
    protected boolean movable = true;
    protected HashSet<FormeVue> dependance = new HashSet<FormeVue>();
    protected HashSet<FormeVue> linkers = new HashSet<FormeVue>();
    protected String name = "";
    protected BigDecimal boundTopY;
    protected BigDecimal boundBotY;
    protected BigDecimal boundRigthX;
    protected BigDecimal boundLeftX;
    protected int scale = 100;
    protected int rotation = 0;
    private Color stringColor = new Color(82, 87, 100);
    protected PointVector centreRota = null;
    protected ArrayList<PointVector> liste_points = new ArrayList<PointVector>();


    /**
     *
     * Fonction prenant les bounds du drawPanel et vérifiant si
     * elle doit s'afficher et donc se calculer.
     *
     * @param top
     * @param bot
     * @param rigth
     * @param left
     * @return
     */
    public boolean inBounds(BigDecimal top,BigDecimal bot,BigDecimal rigth,BigDecimal left){
        boolean vert = false;
        boolean hori = false;
        if(boundTopY.subtract(boundBotY).compareTo(DrawPanel.offSetY) == -1)return false;
        if(boundRigthX.subtract(boundLeftX).compareTo(DrawPanel.offSetX) == -1)return false;
        if(boundTopY.compareTo(bot) == 1) vert = true;
        if(boundBotY.compareTo(top) ==  -1) vert = true;
        if(boundLeftX.compareTo(rigth) == -1) hori = true;
        if(boundRigthX.compareTo(left) == 1) hori = true;
        return hori && vert;
    }

    /**
     * Simple fonction de update des bounds quand la figure est modifier.
     */

    public abstract void updateBounds();

    /**
     * Fonction appeler dans l'update permetant d'un poser une rotation sur chacun des element du drawPanel
     * si c'est dernier ont un angles rotation imposé.
     */
    public void rotate(){
        if(rotation != 0) {
            double rad = ((double) rotation / 360) * (2 * Math.PI);
            AffineTransform trans = AffineTransform.getRotateInstance(rad, positionPixelX(centreRota.positionX), this.heigth - positionPixelY(centreRota.positionY));
            this.transform(trans);
        }
    }

    /**
     *
     * Fonction mettant a jour l'angle de rotation de la figure et tout ces connexion.
     *
     * @param scale l'angle de rotation
     * @param center le centre de la rotation
     * @param done l'ensemble des element déja mis a jour (evite les loop)
     */
    public void setRotate(int scale,PointVector center,HashSet<FormeVue> done){
        done.add(this);
        for(PointVector p: liste_points){
            if(!done.contains(p)) p.setRotate(scale, center, done);
        }
        this.centreRota = center;
        this.rotation = scale;
    }

    /**
     * Fonction mettant a jour le scale de la figure et tout ces connexion.
     *
     * @param scale le scale a appliqué
     * @param done l'ensemble des element déja mis a jour (evite les loop)
     */
    public void setScale(int scale,HashSet<FormeVue> done){
        done.add(this);
        for(PointVector p: liste_points){
            if(!done.contains(p)) p.setScale(scale, done);
        }
        this.scale = scale;
    }

    /**
     * Fonction appeler dans l'update permetant d'un poser un scale  sur chacun des element du drawPanel
     * si ces dernier on un scale != 100
     */
    public void scale(){
        double scale = (double)this.scale/100;
        AffineTransform trans = AffineTransform.getScaleInstance(scale,scale);
        this.transform(trans);
    }

    public FormeVue(int width){
        super(GeneralPath.WIND_NON_ZERO,width);
        this.width = width;
    }

    public final boolean setOver(boolean over){
        boolean tmp = this.over != over;
        this.over = over;
        for (PointVector p: liste_points){
            tmp = tmp | p.setOver(over);
        }
        return tmp;
    }

    public final boolean isOver() {
        return over;
    }

    public final void setSelect(boolean over){
        this.select = over;
        for (PointVector p: liste_points){
            p.setSelect(over);
        }
    }

    public final boolean isSelect() {
       return select;
    }

    /**
     * Fonction renvoyant l'abscice sur le drawPanel correspondant au bigDecimal x
     * @param x
     * @return
     */
    public double positionPixelX(BigDecimal x){
        return x.subtract(DrawPanel.positionX).divide(DrawPanel.offSetX, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Fonction renvoyant l'ordonnée sur le drawPanel correspondant au bigDecimal y
     * @param y
     * @return
     */
    public double positionPixelY(BigDecimal y){
        return y.subtract(DrawPanel.positionY).divide(DrawPanel.offSetY,RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Fonction appelant simplement inBounds() avant de lancer update() pour éviter des calcul inutile
     * @param g2d
     * @param top
     * @param bot
     * @param rigth
     * @param left
     */
    public void updated(Graphics2D g2d,BigDecimal top,BigDecimal bot,BigDecimal rigth,BigDecimal left){
        if(this.inBounds(top,bot,rigth,left))update(g2d);
    }

    public abstract void update(Graphics2D g2d);

    /**
     * Fonction permetant de creer la figure, elle sont écrite dans chacune des forme de facon a respecter
     * les propriété imposer
     * @param x
     * @param y
     * @param set
     * @return
     */
    public abstract boolean setPoint(BigDecimal x,BigDecimal y,boolean set);

    /**
     * Fonction permetant de creer la figure, elle sont écrite dans chacune des forme de facon a respecter
     * les propriété imposer mais fonctionnant avec un point déja existant.
     * @param p
     * @param set
     * @return
     */
    public abstract boolean setPoint(PointVector p,boolean set);

    /**
     * Fonction définisant le mouvement. who permet de savoir qui est a l'initiative du mouvement.
     * Elle fait simplement appelle a movet si elle en a le droit.
     * @param nb
     * @param who
     */
    public void moveX(double nb,FormeVue who){
        if(movable)moveXt(nb,who);
    }

    /**
     * Fonction définisant le mouvement. who permet de savoir qui est a l'initiative du mouvement.
     * Elle fait simplement appelle a movet si elle en a le droit.
     * @param nb
     * @param who
     */
    public void moveY(double nb,FormeVue who){
        if(movable)moveYt(nb,who);
    }

    /**
     * Fonction indiquant la facon dont chaque figure bouge vis a vis d'un input de l'utilisateur
     * elle dépendent du type de figure donc se redéfini.
     * @param nb
     * @param who
     */
    public  abstract void moveXt(double nb,FormeVue who);
    public  abstract void moveYt(double nb,FormeVue who);

    /**
     * Fonction indiquant la facon dont chaque figure bouge vis a vis d'un input de l'utilisateur
     * elle dépendent du type de figure donc se redéfini.
     * @return
     */
    //public  FormeVue selectedForme(BigDecimal x, BigDecimal y,boolean deep){return this;}

    /**
     * Fonction retournant une string correpondant a un Code HTML de représentation de la forme.
     * @return
     */
    public  abstract String htmlString();

    /**
     * Fonction ajoutant la forme à la map du logiciel, en prenant soin d'ajouter ces constutant également.
     * @param map
     */
    public void add(HashMapComponent map){
        map.put(Integer.toString(this.hashCode()),this);
        for(PointVector p : liste_points){
            if(!p.tmp)
                p.add(map);
        }
    }


    /**
     * Fonction assurant la suppresion d'une figure si elle doit être effectuer
     * Le cas échant elle creer une cascade de suppresion avec ces dépendance et constituant.
     * @param who
     * @return
     */
    public  HashSet<FormeVue> delete(FormeVue who){
        HashSet<FormeVue> retour = new HashSet<FormeVue>();
        for (FormeVue q : dependance) {
            if(q!=who)retour.addAll(q.delete(this));
        }
        for (PointVector p : liste_points) {
            if(p!=who)retour.addAll(p.delete(this));
        }
        if(!(this instanceof PointVector))retour.add(this);
        return retour;
    }

    /**
     * Fonction signifiant a la figure qu'elle a perdu un point la constituant suite a un delete
     * elle doit alors agir en conséquance.
     * @param p
     * @return
     */
    public abstract HashSet<FormeVue> removePoint(PointVector p);

    public void removeDependance(FormeVue q) {
        dependance.remove(q);
    }


    //public abstract void dependanceMoved(PointVector p);

    /**
     * Fonction appeler par un points sur la figure pour lui signifier qu'il a bouger et de quelle maniere.
     * La figure doit alors se modifier pour toujours respecter ces contrainte.
     * @param p
     * @param who
     * @param howX
     * @param howY
     */
    public abstract void pointMoved(PointVector p,FormeVue who,double howX,double howY);

    /**
     * Fonction permetant de donner un custom nom a la figure
     * @param s
     */
    public  void named(String s){
        DrawPanel.mapName.put(s,Integer.toString(this.hashCode()));this.name = s;
    }
    public  String getName(){
        return name;
    }
    public boolean isValide() {
        return valide;
    }
    public void moveDependance(FormeVue q){}
    public void moveDependance(DroiteVector d){
        moveDependance((FormeVue) d);
    }
    public void setDependance(FormeVue q){
        this.dependance.add(q);
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Fonction utliser par la sauvegarde: la forme concatene alors a doc un element la represantant.
     * @param Doc
     * @param title
     * @return
     */
    public org.w3c.dom.Element createSVGElement(Document Doc,String title){
        return null;
    }

    /**
     * Fonction verifiant si la mouse intersect la figure.
     * @param x
     * @param y
     * @return
     */
    public boolean mouseOn(double x, double y) {
        BasicStroke pathStroke = new BasicStroke(1);
        Shape clippedPath = pathStroke.createStrokedShape(this);
        return clippedPath.intersects(x - 5, y - 5, 10, 10);
    }

    /**
     * Fonction déssinant le nom de la figure.
     * @param g2d
     * @param x
     * @param y
     */
    public void drawName(Graphics2D g2d, int x, int y){
        if(DrawPanel.isShowName()){
            g2d.setFont(new Font("Arial", Font.ITALIC, 10));
            g2d.setStroke(normalStroke);
            g2d.setColor(stringColor);
            g2d.drawString(name,x,y);
        }
    }

    public void drawAngle(Graphics2D g2d){
        if(isSelect()){
            g2d.setFont(new Font("Arial", Font.ITALIC, 10));
            g2d.setStroke(normalStroke);
            g2d.setColor(stringColor);
            for(int i = 1; i < liste_points.size()-1;i++ ){
                BigDecimal opp1 = liste_points.get(i).positionY.subtract(liste_points.get(i+1).positionY);
                BigDecimal adj1 = liste_points.get(i).positionX.subtract(liste_points.get(i+1).positionX);
                BigDecimal opp2 = liste_points.get(i-1).positionY.subtract(liste_points.get(i).positionY);
                BigDecimal adj2 = liste_points.get(i-1).positionX.subtract(liste_points.get(i).positionX);
                double angle1 = Math.atan(opp1.divide(adj1, RoundingMode.HALF_EVEN).doubleValue());
                double angle2 = Math.atan(opp2.divide(adj2, RoundingMode.HALF_EVEN).doubleValue());
                if(liste_points.get(i).positionX.compareTo(liste_points.get(i-1).positionX)<=0)
                    angle2 = - angle2;
                if(liste_points.get(i+1).positionX.compareTo(liste_points.get(i).positionX)<=0 && liste_points.get(i+1).positionY.compareTo(liste_points.get(i-1).positionY)>=0)
                   angle1 = Math.PI + angle1;
                System.out.println(i + " : " + angle1 * 180 / Math.PI);
                int angle_final = (int)((Math.PI + angle1 - angle2)*180/Math.PI);
                g2d.drawString(Integer.toString(angle_final),(int)positionPixelX(liste_points.get(i).positionX)-5,(int)(this.heigth - positionPixelY(liste_points.get(i).positionY))-5);
            }
        }
    }

    public BigDecimal getBoundTopY() {
        return boundTopY;
    }

    public BigDecimal getBoundBotY() {
        return boundBotY;
    }

    public BigDecimal getBoundRigthX() {
        return boundRigthX;
    }

    public BigDecimal getBoundLeftX() {
        return boundLeftX;
    }
    public ArrayList<PointVector> getListe_points() {
        return liste_points;
    }

    public Color getClr() {
        return clr;
    }

    public void setClr(Color clr) {
        this.clr = clr;
    }


    public int getScale() {
        return scale;
    }

    public int getRotation() {
        return rotation;
    }


}
