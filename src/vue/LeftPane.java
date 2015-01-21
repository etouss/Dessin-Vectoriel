package vue;

import Modele.Mode;
import controlleur.Controlleur;
import Modele.FormeVue;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.util.HashSet;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class LeftPane extends JScrollPane implements HyperlinkListener {

    private JEditorPane editorPane;
    private Controlleur controlleur;
    //int val = 0;

    public LeftPane(JEditorPane jEditorPane,Controlleur controlleur){
        super(jEditorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.controlleur = controlleur;
        this.editorPane = jEditorPane;
        this.editorPane.setEditable(false);
        //this.editorPane.setEnabled(false);
        this.editorPane.setContentType("text/html");
        this.editorPane.addHyperlinkListener(this);
        this.setPreferredSize(new Dimension(130, 100));
    }

    /*public void paintComponent(Graphics g){
        this.getVerticalScrollBar().setValue(this.val);
    }*/

    /**
     * Met a jour le panel de gauche avec les données HTML du drawpanel.
     * @param quadra
     */
    public void setHTMLContent(HashSet<FormeVue> quadra){
        //this.val = this.getVerticalScrollBar().getValue();
        String s = "<style>\n" +
                "a {text-decoration:none;}\n" +
                "li:hover {\n" +
                "        color: red;\n" +
                "    }\n" +
                "a:active {background-color: #FF0000;}\n" +
                ".close {color: #ff0000;}\n" +
                ".select {color: #008000;}\n" +
                ".over {background-color: #FE9A2E;}\n" +
                "a:active .close{background-color: #FFFF00;}\n" +
                "ul {list-style-type: none;padding-left: 10px;margin: 0px;}\n" +
                "</style>";
        for(FormeVue q : quadra){
            s+= q.htmlString()+"<br>";
        }
        this.editorPane.setText(s);
        //repaint();

    }

    /**
     * Catch l'hyperlink pour zoomer ou selectionner ou supprimer un élément
     * @param hle
     */

    @Override
    public void hyperlinkUpdate(HyperlinkEvent hle) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
            String s = hle.getURL().toString().substring(7);
            if(s.charAt(0) == 'x'){
                controlleur.delete(DrawPanel.map.get(s.substring(1)));
                return;
            }
            else if (s.charAt(0) == 's'){

                controlleur.setMode(Mode.select);
                FormeVue q = DrawPanel.map.get(s.substring(1));
                controlleur.setSelectShape(q);
                //q.setSelect(true);
                //controlleur.setSelectShape(q);
                controlleur.setZoomInBounds(q);
                controlleur.forceUpdate();
                return;
            }
            else {
                controlleur.setMode(Mode.select);
                controlleur.setSelectShape(DrawPanel.map.get(s));
            }
        }
    }
}
