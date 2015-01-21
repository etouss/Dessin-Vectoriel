package controlleur.action;

import Modele.Mode;
import controlleur.Controlleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class GrabAction extends JToggleButton {

    public final static String NAME = "GRAB" ;
    private JPanel grabPanel;
    public GrabAction(Controlleur c) {
        super();
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(GrabAction.NAME);
                c.setMode(Mode.grab);
                JLabel label = new JLabel("http://stackoverflow.com");
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        this.setIcon(new ImageIcon("images/grab-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(){
        this.grabPanel = new JPanel();
        this.grabPanel.add(new JLabel(NAME));
    }


}
