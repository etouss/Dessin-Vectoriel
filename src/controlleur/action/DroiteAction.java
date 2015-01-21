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
public class DroiteAction extends JToggleButton {

    final static String NAME = "DROITE" ;
    private JPanel droitePanel;
    private JToggleButton droite = new JToggleButton();
    public DroiteAction(Controlleur c) {
        super();
        creatGrabpanel(c);
        c.addToRightPan(droitePanel, NAME);
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(DroiteAction.NAME);
                droite.setSelected(true);
                c.setMode(Mode.droite);
                c.setSousMode(SousMode.droite);
            }
        });
        this.setIcon(new ImageIcon("images/line-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(Controlleur c){
        this.droitePanel = new JPanel();
        JPanel r = new JPanel();
        JPanel g = new JPanel();
        r.setLayout(new BoxLayout(r,BoxLayout.PAGE_AXIS));
        r.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        droitePanel.setLayout(new BorderLayout());
        droitePanel.add(r, BorderLayout.EAST);
        droitePanel.add(g, BorderLayout.WEST);
        ButtonGroup groupe = new ButtonGroup();


        droite.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droite);

            }
        });
        droite.setIcon(new ImageIcon("images/line-icon.png"));


        JToggleButton para = new JToggleButton();
        para.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteParalele);

            }
        });
        para.setIcon(new ImageIcon("images/par-line-icon.png"));
        JToggleButton perp = new JToggleButton();
        perp.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droitePerpendiculaire);
            }
        });
        perp.setIcon(new ImageIcon("images/per-line-icon.png"));

        JToggleButton inter = new JToggleButton();
        inter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteIntersection);
            }
        });
        inter.setIcon(new ImageIcon("images/inter-icon.png"));

        JToggleButton tan = new JToggleButton();
        tan.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setSousMode(SousMode.droiteIntersection);
            }
        });
        tan.setIcon(new ImageIcon("images/inter-icon.png"));


        groupe.add(droite);
        groupe.add(para);
        groupe.add(perp);
        groupe.add(inter);
        groupe.add(tan);
        g.add(droite);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(para);
        g.add(Box.createRigidArea(new Dimension(0,5)));
        g.add(inter);

        r.add(perp);
        r.add(Box.createRigidArea(new Dimension(0,5)));
        r.add(tan);

    }


}
