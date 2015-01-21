package vue;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class RightPane extends JPanel {
    private CardLayout cards = new CardLayout();
    //public int i = 0;
    public RightPane(){
        this.setLayout(cards);
        this.setVisible(false);
        this.setPreferredSize(new Dimension(150, 75));
    }

    public void setPan(String string){
        cards.show(this, string);
        //System.out.println("5");
    }

    public void addLayout(JPanel jPanel, String name){
        this.add(jPanel, name);
    }



}
