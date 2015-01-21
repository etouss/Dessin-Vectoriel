package controlleur.action;

import Modele.Mode;
import Modele.SousMode;
import controlleur.Controlleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PointAction extends JToggleButton {

    final static String NAME = "POINT" ;
    private JPanel panel;
    public PointAction(Controlleur c) {
        super();
        creatGrabpanel(c);
        c.addToRightPan(panel, NAME);
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(PointAction.NAME);
                c.setMode(Mode.point);
            }
        });
        this.setIcon(new ImageIcon("images/dot-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(Controlleur c){
        panel = new JPanel();
        JPanel r = new JPanel();
        JPanel g = new JPanel();
        r.setLayout(new BoxLayout(r,BoxLayout.PAGE_AXIS));
        r.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        panel.add(r, BorderLayout.EAST);
        panel.add(g, BorderLayout.WEST);
        ButtonGroup groupe = new ButtonGroup();

        JToggleButton pDroite = new JToggleButton();
        pDroite.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteParalele);

            }
        });
        pDroite.setIcon(new ImageIcon("images/dot-icon.png"));

        JToggleButton pPara = new JToggleButton();
        pPara.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.entre2point);
            }
        });
        pPara.setIcon(new ImageIcon("images/dot-icon.png"));

        JToggleButton pPerp = new JToggleButton();
        pPerp.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.avec2point);
            }
        });
        pPerp.setIcon(new ImageIcon("images/dot-icon.png"));

        JToggleButton pCercle = new JToggleButton();
        pCercle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteIntersection);
            }
        });
        pCercle.setIcon(new ImageIcon("images/dot-icon.png"));

        JToggleButton pInter = new JToggleButton();
        pInter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteIntersection);
            }
        });
        pInter.setIcon(new ImageIcon("images/dot-icon.png"));

        groupe.add(pDroite);
        groupe.add(pPara);
        groupe.add(pPerp);
        groupe.add(pCercle);
        groupe.add(pInter);


        g.add(pDroite);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(pPerp);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(pInter);

        r.add(pPara);
        r.add(Box.createRigidArea(new Dimension(0,5)));
        r.add(pCercle);
    }


}
