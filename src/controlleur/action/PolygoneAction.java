package controlleur.action;

import Modele.Mode;
import Modele.SousMode;
import controlleur.Controlleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class PolygoneAction extends JToggleButton {

    final static String NAME = "POLYGONE" ;
    private JPanel grabPanel;
    private JToggleButton poly = new JToggleButton();
    public PolygoneAction(Controlleur c) {
        super();
        creatGrabpanel(c);
        c.addToRightPan(grabPanel, NAME);
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(PolygoneAction.NAME);
                poly.setSelected(true);
                c.setMode(Mode.poly);
                c.setSousMode(SousMode.poly);

            }
        });
        this.setIcon(new ImageIcon("images/poly-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(Controlleur c){
        this.grabPanel = new JPanel();
        JPanel r = new JPanel();
        JPanel g = new JPanel();
        r.setLayout(new BoxLayout(r,BoxLayout.PAGE_AXIS));
        r.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        grabPanel.setLayout(new BorderLayout());
        grabPanel.add(r, BorderLayout.EAST);
        grabPanel.add(g, BorderLayout.WEST);
        ButtonGroup groupe = new ButtonGroup();


        poly.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.poly);

            }
        });
        poly.setIcon(new ImageIcon("images/poly-icon.png"));

        groupe.add(poly);
        g.add(poly);
        g.add(Box.createRigidArea(new Dimension(0,5)));


        JToggleButton center = new JToggleButton();
        center.addActionListener(new AbstractAction() {
            boolean bool = false;
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneGrav);}
        });
        center.setIcon(new ImageIcon("images/centre-poly-icon.png"));
        center.setToolTipText("Centre de gravité");


        JToggleButton triangleRec = new JToggleButton();
        triangleRec.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneTriangleRectangle);

            }
        });
        triangleRec.setIcon(new ImageIcon("images/rect-icon.png"));
        triangleRec.setToolTipText("Triangle rectangle");

        JToggleButton triangleIso = new JToggleButton();
        triangleIso.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneTriangleIsocele);

            }
        });
        triangleIso.setIcon(new ImageIcon("images/iso-icon.png"));
        triangleIso.setToolTipText("Triangle isocele");

        JToggleButton triangleEqui = new JToggleButton();
        triangleEqui.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneTriangleEqui);

            }
        });
        triangleEqui.setIcon(new ImageIcon("images/triangle-icon.png"));
        triangleEqui.setToolTipText("Triangle Equilatéral");


        JToggleButton carre = new JToggleButton();
        carre.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneCarre);

            }
        });
        carre.setIcon(new ImageIcon("images/carre-icon.png"));
        carre.setToolTipText("Carre");

        JToggleButton losange = new JToggleButton();
        losange.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneLosange);

            }
        });
        losange.setIcon(new ImageIcon("images/los-icon.png"));
        losange.setToolTipText("Losange");

        JToggleButton rectangle = new JToggleButton();
        rectangle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneRectangle);

            }
        });
        rectangle.setIcon(new ImageIcon("images/rectangle-icon.png"));
        rectangle.setToolTipText("Rectangle");

        JToggleButton paralelo = new JToggleButton();
        paralelo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.polygoneParalelograme);

            }
        });
        paralelo.setIcon(new ImageIcon("images/para-icon.png"));
        paralelo.setToolTipText("Paralelogramme");

        groupe.add(center);
        groupe.add(triangleRec);
        groupe.add(triangleIso);
        groupe.add(triangleEqui);
        groupe.add(carre);
        groupe.add(losange);
        groupe.add(rectangle);
        groupe.add(paralelo);

        g.add(center);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(triangleRec);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(triangleIso);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(triangleEqui);
        r.add(carre);
        r.add(Box.createRigidArea(new Dimension(0, 5)));
        r.add(losange);
        r.add(Box.createRigidArea(new Dimension(0,5)));
        r.add(rectangle);
        r.add(Box.createRigidArea(new Dimension(0,5)));
        r.add(paralelo);


    }


}
