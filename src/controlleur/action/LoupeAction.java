package controlleur.action;

import Modele.Mode;
import controlleur.Controlleur;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class LoupeAction extends JToggleButton {

    final static String NAME = "LOUPE" ;
    private JPanel panel;
    public LoupeAction(Controlleur c) {
        super();
        creatLoupepanel();
        c.addToRightPan(panel, NAME);
        this.setAction(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(LoupeAction.NAME);
                c.setMode(Mode.loupe);
            }
        });
        this.setIcon(new ImageIcon("images/loupe-icon.png"));
        this.setHideActionText(true);
    }


    public void creatLoupepanel(){
        this.panel = new JPanel();
        this.panel.add(new JLabel(NAME));
    }

}