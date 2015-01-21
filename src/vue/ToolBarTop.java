package vue;

import controlleur.Controlleur;
import controlleur.action.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class ToolBarTop extends JToolBar {

    public ToolBarTop(DrawPanel drawPanel,Controlleur c){
        super();
        ButtonGroup groupe = new ButtonGroup();

        JToggleButton grab = new GrabAction(c);
        JToggleButton select = new SelectAction(c);
        JToggleButton point = new PointAction(c);
        JToggleButton segment = new SegmentAction(c);
        JToggleButton polygone = new PolygoneAction(c);
        JToggleButton cercle = new CercleAction(c);
        JToggleButton bezize = new BezizerAction(c);
        JToggleButton droite = new DroiteAction(c);
        //JToggleButton link = new LinkAction(c);
        JToggleButton loupe = new LoupeAction(c);
        JButton raz = new JButton("");

        raz.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //controlleur.setZoomInBounds(null);
                drawPanel.RAZ_ZOOM();
            }
        });
        raz.setIcon(new ImageIcon("images/11-icon.png"));

        JButton  center = new JButton("");
        center.setIcon(new ImageIcon("images/rep-icon.png"));
        center.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //controlleur.setZoomInBounds(null);
                drawPanel.RAZ();
            }
        });




        groupe.add(grab);
        groupe.add(select);
        groupe.add(droite);
        groupe.add(segment);
        groupe.add(point);
        groupe.add(polygone);
        groupe.add(cercle);
        groupe.add(bezize);
        //groupe.add(link);
        groupe.add(loupe);



        this.add(grab);
        this.add(select);
        this.add(Box.createRigidArea(new Dimension(30, 0)));
        this.add(loupe);
        this.add(raz);
        this.add(center);
        this.add(Box.createRigidArea(new Dimension(30, 0)));
        this.add(point);
        this.add(segment);
        this.add(polygone);
        this.add(cercle);
        this.add(bezize);
        this.add(droite);
        //this.add(link);

        grab.setSelected(true);


        this.setFloatable(false);

    }
}
