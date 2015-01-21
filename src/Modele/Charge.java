package Modele;

import controlleur.Controlleur;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import vue.DrawPanel;
import vue.forme.BezierVector;
import vue.forme.Droite.DroiteVector;
import vue.forme.Point.PointVector;
import vue.forme.Polygone.PolygoneVector;
import vue.forme.SegmentVector;
import vue.forme.ellipse.CercleVector;
import vue.forme.ellipse.OvalVector;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ByTeK on 13/12/14.
 */
public class Charge {
    private DrawPanel drawPanel;
    private Document doc;
    private org.w3c.dom.Element svg;
    private Controlleur controlleur;

    public Charge(DrawPanel drawPanel, Controlleur controlleur){
        this.drawPanel = drawPanel;
        this.controlleur = controlleur;
    }

    /**
     * Creer le document SVG
     * @param path
     */

    public void createSVGDoc(String path){
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            String uri = "file:"+path;
            this.doc = f.createDocument(uri);
        } catch (IOException ex) {
            // ...
        }
        setDrawPanel();
        setQuadra();

    }

    /**
     * Applique les condition du chargement au drawpanel
     */
    public void setDrawPanel(){
        this.svg = doc.getDocumentElement();
        DrawPanel.offSetX = new BigDecimal(svg.getAttribute("offSetX"));
        DrawPanel.offSetY = new BigDecimal(svg.getAttribute("offSetY"));
        DrawPanel.positionX = new BigDecimal(svg.getAttribute("positionX"));
        DrawPanel.positionY = new BigDecimal(svg.getAttribute("positionY"));
    }

    public BigDecimal truePosX(String s){
        return DrawPanel.offSetX.multiply(new BigDecimal(s)).add(DrawPanel.positionX);
    }
    public BigDecimal truePosY(String s){
        return DrawPanel.offSetY.multiply(new BigDecimal(drawPanel.getHeight()).subtract(new BigDecimal(s))).add(DrawPanel.positionY);
    }

    /**
     * Met a jour le drawPanel avec les figure
     */
    public void setQuadra(){
        HashMap<String,PointVector> hashToPoint = new HashMap<>();

        NodeList list = svg.getElementsByTagName("rect");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String x = el.getAttribute("x");
            String y = el.getAttribute("y");
            String quadra = el.getAttribute("quadra");
            String id = el.getAttribute("id");
            String liste = el.getAttribute("liste");
            PointVector p = new PointVector(truePosX(x),truePosY(y),drawPanel.getWidth(),drawPanel.getHeight(),null,false);
            hashToPoint.put(id, p);
            if(quadra.equals("1"))controlleur.ajouter(p);
        }

        list = svg.getElementsByTagName("polygon");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String listePoint = el.getAttribute("listePoint");
            String[] point = listePoint.split(",");

            PolygoneVector q = new PolygoneVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            for(int j = 1; j<point.length;j++){
                q.setPoint(hashToPoint.get(point[j]),true);
            }
            q.liste_points.remove(q.liste_points.size()-1);
            controlleur.ajouter(q);
        }

        list = svg.getElementsByTagName("circle");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String listePoint = el.getAttribute("listePoint");
            String[] point = listePoint.split(",");

            CercleVector q = new CercleVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            for(int j = 1; j<point.length;j++){
                q.setPoint(hashToPoint.get(point[j]),true);
            }
            //Soucis d'incrementation ??
            controlleur.ajouter(q);
        }

        list = svg.getElementsByTagName("ellipse");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String listePoint = el.getAttribute("listePoint");
            String[] point = listePoint.split(",");

            OvalVector q = new OvalVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            for(int j = 1; j<point.length;j++){
                q.setPoint(hashToPoint.get(point[j]),true);
            }
            //Soucis d'incrementation ??
            controlleur.ajouter(q);
        }

        list = svg.getElementsByTagName("line");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String listePoint = el.getAttribute("listePoint");
            String[] point = listePoint.split(",");
            FormeVue q;
            if(el.getAttribute("stroke").equals("red"))
                q = new DroiteVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            else
                q = new SegmentVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            for(int j = 1; j<point.length;j++){
                q.setPoint(hashToPoint.get(point[j]),true);
            }
            //Soucis d'incrementation ??
            controlleur.ajouter(q);
        }

        list = svg.getElementsByTagName("path");
        for(int i = 0; i< list.getLength();i++){
            org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
            String listePoint = el.getAttribute("listePoint");
            String[] point = listePoint.split(",");
            FormeVue q;
            q = new BezierVector(hashToPoint.get(point[0]),drawPanel.getWidth(),drawPanel.getHeight());
            for(int j = 1; j<point.length;j++){
                q.setPoint(hashToPoint.get(point[j]),true);
            }
            //Soucis d'incrementation ??
            controlleur.ajouter(q);
        }

    }

}
