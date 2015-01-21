package Modele;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vue.DrawPanel;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by ByTeK on 13/12/14.
 */
public class Sauvegarde {
    private DrawPanel drawPanel;
    private Document doc;
    public Sauvegarde(DrawPanel drawPanel){
        this.drawPanel = drawPanel;
    }

    /**
     * Creer le Document SVG a partir du drawpanel
     */
    public void createSVGDoc(){
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        //System.out.println(svgNS);
        this.doc = impl.createDocument(svgNS, "svg", null);

        Element svgRoot = doc.getDocumentElement();
        svgRoot.setAttributeNS(null, "offSetX", DrawPanel.offSetX.toString());
        svgRoot.setAttributeNS(null, "offSetY", DrawPanel.offSetY.toString());
        svgRoot.setAttributeNS(null, "positionX", DrawPanel.positionX.toString());
        svgRoot.setAttributeNS(null, "positionY", DrawPanel.positionY.toString());
        svgRoot.setAttributeNS(null, "width", Integer.toString(drawPanel.getWidth()));
        svgRoot.setAttributeNS(null, "height", Integer.toString(drawPanel.getHeight()));

        for (FormeVue q: DrawPanel.map){
            svgRoot.appendChild(q.createSVGElement(doc,svgNS));
        }
    }

    /**
     * Ecrit le svg Doc.
     * @param path
     * @return
     */
    public boolean writeSVGDoc(String path){
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult console = new StreamResult(path);
        try {
            transformer.transform(source, console);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean save(String path){
        createSVGDoc();
        return writeSVGDoc(path);
    }
}