package controlleur.action;

import Modele.Mode;
import controlleur.Controlleur;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class SegmentAction extends JToggleButton {

    final static String NAME = "SEGMENT" ;
    private JPanel segmentPanel;
    public SegmentAction(Controlleur c) {
        super();
        creatGrabpanel();
        c.addToRightPan(segmentPanel, NAME);
        this.addActionListener(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(SegmentAction.NAME);
                c.setMode(Mode.segment);
            }
        });
        this.setIcon(new ImageIcon("images/seg-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }

    public void creatGrabpanel(){
        this.segmentPanel = new JPanel();
        this.segmentPanel.add(new JLabel(NAME));
    }


}
