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
public class CercleAction extends JToggleButton {

    final static String NAME = "CERCLE" ;
    private JPanel circlePanel;
    private JToggleButton cercle = new JToggleButton();
    private JToggleButton oval = new JToggleButton();
    private JToggleButton arcCercle = new JToggleButton();
    private JToggleButton arcOval = new JToggleButton();
    public CercleAction(Controlleur c) {
        super();
        creatGrabpanel(c);
        c.addToRightPan(circlePanel, NAME);
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(CercleAction.NAME);
                c.setMode(Mode.cercle);
                cercle.setSelected(true);
                c.setSousMode(SousMode.cercle);
            }
        });
        this.setIcon(new ImageIcon("images/circle-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(Controlleur c){
        this.circlePanel = new JPanel();
        this.circlePanel.add(new JLabel(NAME));
        JPanel r = new JPanel();
        JPanel g = new JPanel();
        r.setLayout(new BoxLayout(r,BoxLayout.PAGE_AXIS));
        r.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        circlePanel.setLayout(new BorderLayout());
        circlePanel.add(r, BorderLayout.EAST);
        circlePanel.add(g, BorderLayout.WEST);

        cercle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.cercle);

            }
        });
        cercle.setIcon(new ImageIcon("images/circle-icon.png"));
        cercle.setToolTipText("Cercle");


        oval.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.cercleoval);

            }
        });
        oval.setIcon(new ImageIcon("images/oval-icon.png"));
        oval.setToolTipText("Oval");

        arcCercle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.arcCercle);

            }
        });
        arcCercle.setIcon(new ImageIcon("images/arccircle-icon.png"));
        arcCercle.setToolTipText("Arc de Cercle");

        arcOval.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.arcCercle);

            }
        });
        arcOval.setIcon(new ImageIcon("images/arcoval-icon.png"));
        arcOval.setToolTipText("Arc d'Oval");

        ButtonGroup groupe = new ButtonGroup();
        groupe.add(cercle);
        groupe.add(oval);
        groupe.add(arcCercle);
        groupe.add(arcOval);
        g.add(cercle);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(oval);
        g.add(Box.createRigidArea(new Dimension(0,5)));

        r.add(arcCercle);
        r.add(Box.createRigidArea(new Dimension(0, 5)));
        r.add(arcOval);
        r.add(Box.createRigidArea(new Dimension(0,5)));
    }


}
