package vue;

import Modele.*;
import controlleur.Controlleur;
import vue.forme.*;
import vue.forme.Droite.DroiteParaVector;
import vue.forme.Droite.DroitePerpenVector;
import vue.forme.Droite.DroiteVector;
import vue.forme.Point.PointEntrePoint;
import vue.forme.Point.PointInterVector;
import vue.forme.Point.PointVector;
import vue.forme.Polygone.*;
import vue.forme.ellipse.ArcCercleVector;
import vue.forme.ellipse.CercleVector;
import vue.forme.ellipse.OvalVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;

public class DrawPanel extends JPanel {

    private Controlleur controlleur;
    private Mode mode = Mode.grab;
    private SousMode sousMode = null;
    private int lastX;
    private int lastY;
    private BigDecimal positionXr = positionX.add(offSetX.multiply(new BigDecimal(getWidth())));
    private  BigDecimal positionYt = positionY.add(offSetY.multiply(new BigDecimal(getHeight())));
    private Repere repere;
    private int previousHeigth;
    private static boolean showName = true;

    public static BigDecimal offSetX = new BigDecimal(0.1);
    public static BigDecimal offSetY = new BigDecimal(0.1);
    public static BigDecimal positionX = new BigDecimal(0);
    public static BigDecimal positionY = new BigDecimal(0);
    public final static int WIDTH_RATIO = 18;
    private HashSet<FormeVue> quadras = new HashSet<>();
    //public static HashSet<PointVector> listPoints = new HashSet<>();
    public final static HashMapComponent map = new HashMapComponent();
    public final static HashMap<String, String> mapName = new HashMap<>();

    private boolean grabRepere = true;
    private boolean action = false;


    private FormeVue selectShape;
    private FormeVue tempShape;

    public DrawPanel(Controlleur controlleur) {
        super();
        this.setFocusable(true);
        previousHeigth = getHeight();
        //this.setBackground(new Color(101,123,131));
        this.controlleur = controlleur;
        this.setBackground(Color.WHITE);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    if (selectShape != null && action) {
                        controlleur.delete(selectShape);
                        controlleur.setSelectShape(null);
                        action = false;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        this.addMouseWheelListener(mouseWheelEvent -> {
            if (mode == Mode.grab) {
                BigDecimal xZoomPrev = offSetX.multiply(new BigDecimal(lastX));
                BigDecimal yZoomPrev = offSetY.multiply(new BigDecimal(getHeight() - lastY));
                DrawPanel.zoom(mouseWheelEvent.getWheelRotation());
                BigDecimal xZoomNxt = offSetX.multiply(new BigDecimal(lastX));
                BigDecimal yZoomNxt = offSetY.multiply(new BigDecimal(getHeight() - lastY));
                positionX = positionX.add(xZoomPrev).subtract(xZoomNxt);
                positionY = positionY.add(yZoomPrev).subtract(yZoomNxt);
                repaint();
            } else if (mode == Mode.select) {
                BigDecimal xZoomPrev = offSetX.multiply(new BigDecimal(repere.getX()));
                BigDecimal yZoomPrev = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
                DrawPanel.zoom(mouseWheelEvent.getWheelRotation());
                BigDecimal xZoomNxt = offSetX.multiply(new BigDecimal(repere.getX()));
                BigDecimal yZoomNxt = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
                positionX = positionX.add(xZoomPrev).subtract(xZoomNxt);
                positionY = positionY.add(yZoomPrev).subtract(yZoomNxt);
                repaint();
            }
            positionXr = positionX.add(offSetX.multiply(new BigDecimal(getWidth())));
            positionYt = positionY.add(offSetY.multiply(new BigDecimal(getHeight())));
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                BottomPanel.set_cursor_text("(" + e.getX() + "; " + e.getY() + ")");
                if (mode == Mode.grab) {
                    if (grabRepere) {
                        repere.moveX(e.getX() - lastX);
                        repere.moveY(e.getY() - lastY);
                    } else {
                        moveX(lastX - e.getX());
                        moveY(e.getY() - lastY);
                        positionXr = positionX.add(offSetX.multiply(new BigDecimal(getWidth())));
                        positionYt = positionY.add(offSetY.multiply(new BigDecimal(getHeight())));
                    }
                } else if (mode == Mode.select) {
                    if ((repere.overCenter(e) || repere.overX(e, getWidth())) && grabRepere) {
                        BigDecimal xZoomPrev = offSetX.multiply(new BigDecimal(repere.getX()));
                        zoomX(e.getX() - lastX);
                        BigDecimal xZoomNxt = offSetX.multiply(new BigDecimal(repere.getX()));
                        positionX = positionX.add(xZoomPrev).subtract(xZoomNxt);
                    } else if ((repere.overCenter(e) || repere.overY(e, getHeight())) && grabRepere) {
                        BigDecimal yZoomPrev = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
                        zoomY(e.getY() - lastY);
                        BigDecimal yZoomNxt = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
                        positionY = positionY.add(yZoomPrev).subtract(yZoomNxt);
                    } else if (selectShape != null) {
                        //System.out.println("Select    " + selectShape);
                        selectShape.moveX(e.getX() - lastX, null);
                        selectShape.moveY(lastY - e.getY(), null);
                    }
                }
                lastX = e.getX();
                lastY = e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                BottomPanel.set_cursor_text("(" + Utilitaire.posToString(new BigDecimal(e.getX()).multiply(DrawPanel.offSetX).add(DrawPanel.positionX)) + "; " + Utilitaire.posToString(new BigDecimal(getHeight() - e.getY()).multiply(DrawPanel.offSetY).add(DrawPanel.positionY)) + ")");
                // mettre les vrai position
                //Pas mis a jour car repaint quand on cliqueet pas quand on bouge !!!!
                if (mode == Mode.select) {
                    if (repere.isOver(e, getWidth(), getHeight())) {
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } else if (mode == Mode.grab) {
                    if (repere.isOver(e, getWidth(), getHeight())) {
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
                if (action) {

                    FormeVue tmp = null;
                    try {
                        tmp = onForm(e, null);
                    } catch (FormeNotFoundExeption ignored) {
                    }
                    if (tmp instanceof PointVector) {
                        selectShape.setPoint((PointVector) tmp, false);
                    } else {
                        selectShape.setPoint(truePosX(e.getX()), truePosY(e.getY()), false);
                    }
                    repaint();
                } else {
                    boolean bool = false;
                    lastX = e.getX();
                    lastY = e.getY();
                    for (FormeVue q : quadras) {
                        bool = bool | q.setOver(false);
                    }
                    FormeVue tmp;
                    try {
                        tmp = onForm(e, null);
                        bool = bool | tmp.setOver(true);
                    } catch (FormeNotFoundExeption ignored) {
                    }
                    if(bool)repaint();
                }
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //boolean select = false;

                if (mode == Mode.select) {
                    /*for (FormeVue q : quadras) {
                        BasicStroke pathStroke = new BasicStroke(1);
                        Shape clippedPath = pathStroke.createStrokedShape(q);
                        if (clippedPath.intersects(e.getX() - 20, e.getY() - 20, 40, 40)) {
                            if(selectShape != null)selectShape.setSelect(false);
                            selectShape = q;
                            selectShape.setSelect(true);
                            select = true;
                            repaint();
                            break;
                        }
                    }*/
                    /*if(!select && selectShape != null){
                        selectShape.setSelect(false);
                        selectShape = null;
                        repaint();
                    }*/
                } else {
                    if (selectShape == null || !action) {
                        creationClickedMouse(e);
                    } else {
                        FormeVue tmp = null;
                        try {
                            tmp = onForm(e, null);
                        } catch (FormeNotFoundExeption ignored) {

                        }
                        boolean valide;
                        if (tmp instanceof PointVector) {
                            valide = selectShape.setPoint((PointVector) tmp, true);
                        } else {
                            valide = selectShape.setPoint(truePosX(e.getX()), truePosY(e.getY()), true);
                        }
                        if (valide) {
                            //listPoints.add(new PointVector(truePosX(e.getX()), truePosY(e.getY()),getWidth(),getHeight(),null));

                            map.add(selectShape);
                            if (!selectShape.isValide()) {
                                controlleur.delete(selectShape);
                            }
                            selectShape = null;
                            action = false;
                        }
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (mode == Mode.grab && repere.isOver(e, getWidth(), getHeight())) grabRepere = true;
                else if (mode == Mode.select && repere.isOver(e, getWidth(), getHeight())) grabRepere = true;
                else if (mode == Mode.select) {
                    try {
                        controlleur.setSelectShape(onForm(e, null));
                        //if (selectShape != null) selectShape.setSelect(false);
                        //selectShape = onForm(e, null);
                        //selectShape.setSelect(true);
                        //controlleur.setZoomInBounds(selectShape);
                    } catch (FormeNotFoundExeption exep) {
                        controlleur.setSelectShape(null);
                    }
                    //System.out.println(selectShape);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                grabRepere = false;
                if (mode == Mode.select && selectShape != null) {
                    FormeVue q;
                    try {
                        q = onForm(e, selectShape);
                    } catch (FormeNotFoundExeption formeNotFoundExeption) {
                        //formeNotFoundExeption.printStackTrace();
                        q = null;
                    }
                    if (selectShape instanceof PointVector && q instanceof PointVector) {
                        //System.out.println("s:"+selectShape);
                        //System.out.println("q:"+q);
                        try {
                            ((PointVector) q).fuse((PointVector) selectShape);
                            controlleur.delete(selectShape);
                            controlleur.setSelectShape(q);
                        } catch (FormeNotFoundExeption formeNotFoundExeption) {
                            //formeNotFoundExeption.printStackTrace();
                            return;
                        }
                    }
                }
                if (!action) {
                    //System.out.println("Bad");
                    //controlleur.setSelectShape(null);
                }
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionY = positionY.add(new BigDecimal(previousHeigth - getHeight()).multiply(offSetY));
                repere.setHeigth(getHeight());
                repere.setWidth(getWidth());
                for (FormeVue f : quadras) {
                    f.setHeigth(getHeight());
                    f.setWidth(getWidth());
                }
                previousHeigth = getHeight();
                repaint();

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        repere = new Repere(100, 100);
        //quadras.add(new CourbeVector(this.getWidth(),x->x.multiply(x)));
        //System.out.println(Utilitaire.cos(new BigDecimal(Math.PI)));
    }

    /**
     * Appelle update sur toutes les figure du panel
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        repere.updateRepere(g2d);
        //BigDecimal positionXr = positionX.add(offSetX.multiply(new BigDecimal(getWidth())));
        //BigDecimal positionYt = positionY.add(offSetY.multiply(new BigDecimal(getHeight())));

        for (FormeVue q : quadras) {
            q.updated(g2d, positionYt, positionY, positionXr, positionX);
        }

        this.controlleur.updateLeftPane(quadras);
    }

    /**
     * Raz le repere pour le mettre sur 0,0
     */
    public void RAZ() {
        positionX = offSetX.multiply(new BigDecimal(-repere.getX()));
        positionY = offSetY.multiply(new BigDecimal(repere.getY() - getHeight()));
        repaint();
    }

    /**
     * Raz le zoom sur 1:1
     */
    public void RAZ_ZOOM() {
        BigDecimal pas = new BigDecimal(getWidth() / WIDTH_RATIO);
        BigDecimal xZoomPrev = offSetX.multiply(new BigDecimal(repere.getX()));
        BigDecimal yZoomPrev = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
        offSetX = (new BigDecimal(1)).divide(pas, 10, RoundingMode.HALF_EVEN);
        offSetY = (new BigDecimal(1)).divide(pas, 10, RoundingMode.HALF_EVEN);
        BigDecimal xZoomNxt = offSetX.multiply(new BigDecimal(repere.getX()));
        BigDecimal yZoomNxt = offSetY.multiply(new BigDecimal(getHeight() - repere.getY()));
        positionX = positionX.add(xZoomPrev).subtract(xZoomNxt);
        positionY = positionY.add(yZoomPrev).subtract(yZoomNxt);
        repaint();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setSelectShape(FormeVue selectShape) {
        this.selectShape = selectShape;
    }

    public FormeVue getSelectShape() {
        return selectShape;
    }

    public BigDecimal truePosX(double x) {
        return offSetX.multiply(new BigDecimal(x)).add(positionX);
    }

    public BigDecimal truePosY(double y) {
        return offSetY.multiply(new BigDecimal(getHeight() - y)).add(positionY);
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public void setSousMode(SousMode sousMode) {
        this.sousMode = sousMode;
    }


    /**
     * Formule itérant sur map de facon a récuperer la figure sur laquelle et la souris si elle existe
     * @param e
     * @param f
     * @return
     * @throws FormeNotFoundExeption
     */
    public FormeVue onForm(MouseEvent e, FormeVue f) throws FormeNotFoundExeption {
        //FormeVue t;
        for (FormeVue q : map) {
            if (q == f) continue;
            if (q.inBounds(positionYt, positionY, positionXr, positionX) && q.mouseOn(e.getX(), e.getY())) return q;
        }
        throw new FormeNotFoundExeption();
    }

    /**
     * Suprime un element de quadra et de map
     * @param q
     */
    public void delete(FormeVue q) {
        this.quadras.remove(q);
        map.delete(q);
    }

    /**
     * Ajoute q a quadra et map
     * @param q
     */
    public void ajouter(FormeVue q) {
        if(q instanceof PointVector)((PointVector) q).inQuadra = true;
        this.quadras.add(q);
        map.add(q);
    }

    public static boolean checkIntersect(Shape s, int x, int y) {
        return s.intersects(x - 5, y - 5, 10, 10);
    }

    static public void zoomX(int nb) {
        offSetX = offSetX.add(offSetX.divide(new BigDecimal(10), RoundingMode.HALF_EVEN).multiply(new BigDecimal(nb)));
    }

    static public void zoomY(int nb) {
        offSetY = offSetY.add(offSetY.divide(new BigDecimal(10), RoundingMode.HALF_EVEN).multiply(new BigDecimal(nb)));
        //System.out.println(offSetX.doubleValue());
    }

    static public void zoom(int nb) {
        zoomX(nb);
        zoomY(nb);
    }

    /**
     * Deplace l'utilisateur sur le DrawPanel
     * @param nb
     */
    static public void moveX(double nb) {
        positionX = positionX.add(offSetX.multiply(new BigDecimal(nb)));
    }

    static public void moveY(double nb) {
        positionY = positionY.add(offSetY.multiply(new BigDecimal(nb)));
    }

    /*public static PointVector checkPoint(BigDecimal x2,BigDecimal y2, int width, int height, FormeVue f){
        PointVector pointVector = new PointVector(x2,y2, width,height,f);
        for(PointVector p : listPoints){
            if(p.intersects(pointVector.positionPixelX(pointVector.positionX) - 5, height - pointVector.positionPixelY(pointVector.positionY) - 5, pointVector.positionPixelX(pointVector.positionX) + 5, height - pointVector.positionPixelY(pointVector.positionY) + 5)){
                p.add_to_liste(f);
                return p;
            }
        }

        return pointVector;
    }*/

    /*public static void addPoint(PointVector p){
        listPoints.add(p);
    }*/

    public HashSet<FormeVue> getQuadras() {
        return quadras;
    }

    public void setQuadras(HashSet<FormeVue> quadras) {
        this.quadras = quadras;
    }

    public static boolean isShowName() {
        return showName;
    }

    public static void setShowName(boolean showName) {
        DrawPanel.showName = showName;
    }


    /**
     * Fonction gerant les clique de la souris en fonction du mode dans lequelle on est:
     * Elle initie la creation des figure.
     * @param e
     */
    public void creationClickedMouse(MouseEvent e) {
        FormeVue d = null;
        PointVector point = null;
        try {
            //On cherche a creer un point si je ne suis pas sur un point ...
            FormeVue q = null;
            try {
                q = onForm(e, null);
            } catch (FormeNotFoundExeption ignored) {
                // je suis sous rien
            }
            if (q instanceof PointVector)
                point = (PointVector) q;
            else
                point = new PointVector(truePosX(e.getX()), truePosY(e.getY()), getWidth(), getHeight(), null, true);

            //On regarde dans quelle mode on est et on agit en conséquance.

            switch (mode) {
                case poly:
                    if (sousMode == SousMode.polygoneGrav) {
                        if (q instanceof PolygoneVector) {
                            if (((PolygoneVector) q).getGrav() != null) return;
                            PointVector p = new PointVector((PolygoneVector) q);
                            ajouter(p);
                            repaint();
                            return;
                        } else throw new FormeNotFoundExeption();
                    } else if (sousMode == SousMode.polygoneTriangleRectangle)
                        d = new TriangleRectangleVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.polygoneRectangle)
                        d = new RectangleVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.polygoneLosange)
                        d = new LosangeVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.polygoneCarre)
                        d = new CarreVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.polygoneParalelograme)
                        d = new ParalelogrameVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.polygoneTriangleIsocele)
                        d = new TriangleIsoceleVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.poly)
                        d = new PolygoneVector(point, getWidth(), getHeight());

                    break;

                case cercle:
                    if (sousMode == SousMode.cercleoval)
                        d = new OvalVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.arcCercle)
                        d = new ArcCercleVector(point, getWidth(), getHeight());
                    else if (sousMode == SousMode.cercle)
                        d = new CercleVector(point, getWidth(), getHeight());

                    break;

                case segment:
                    d = new SegmentVector(point, getWidth(), getHeight());

                    break;

                case bezize:
                    d = new BezierVector(point, getWidth(), getHeight());

                    break;

                case droite:
                    if (sousMode == SousMode.droiteParalele) {
                        if (q instanceof DroiteVector) d = new DroiteParaVector((DroiteVector) q);
                        else throw new FormeNotFoundExeption();
                    } else if (sousMode == SousMode.droitePerpendiculaire) {
                        if (q instanceof DroiteVector) d = new DroitePerpenVector((DroiteVector) q);
                        else throw new FormeNotFoundExeption();
                    } else if (sousMode == SousMode.droiteIntersection) {
                        if (q instanceof DroiteVector && (tempShape instanceof DroiteVector) && q != tempShape) {
                            if (((DroiteVector) q).existInter((DroiteVector) tempShape)) {
                                tempShape = q;
                                return;
                            }
                            PointVector p = new PointInterVector((DroiteVector) tempShape, (DroiteVector) q);
                            tempShape = null;
                            ajouter(p);
                            repaint();
                            return;
                        } else if (q instanceof DroiteVector && !(tempShape instanceof DroiteVector)) {
                            tempShape = q;
                            throw new FormeNotFoundExeption();
                        } else throw new FormeNotFoundExeption();
                    } else if (sousMode == SousMode.droite)
                        d = new DroiteVector(point, getWidth(), getHeight());

                    break;

                case point:
                    PointVector p;
                    if (sousMode == SousMode.entre2point) {
                        if (q instanceof PointVector && (tempShape instanceof PointVector) && q != tempShape) {
                            p = new PointEntrePoint((PointVector) tempShape, (PointVector) q,true);
                            tempShape = null;
                            ajouter(p);
                            repaint();
                            return;
                        } else if (q instanceof PointVector && !(tempShape instanceof PointVector)) {
                            tempShape = q;
                            throw new FormeNotFoundExeption();
                        } else throw new FormeNotFoundExeption();
                    }
                    else if (sousMode == SousMode.avec2point) {
                        if (q instanceof PointVector && (tempShape instanceof PointVector) && q != tempShape) {
                            p = new PointEntrePoint((PointVector) tempShape, (PointVector) q,false);
                            tempShape = null;
                            ajouter(p);
                            repaint();
                            return;
                        } else if (q instanceof PointVector && !(tempShape instanceof PointVector)) {
                            tempShape = q;
                            throw new FormeNotFoundExeption();
                        } else throw new FormeNotFoundExeption();
                    }
                    else {
                        p = new PointVector(truePosX(e.getX()), truePosY(e.getY()), getWidth(), getHeight(), null, false);
                        ajouter(p);
                        repaint();
                    }
                    return;

                //Pas encors fonctionnelle
                default:
                    return;
            }
            ajouter(d);
            selectShape = d;
            action = true;
        }
        catch (FormeNotFoundExeption ignored) {

        }

    }
    public Mode getMode() {
        return mode;
    }
}


















