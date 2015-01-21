package vue;

import controlleur.Controlleur;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class BottomPanel extends JPanel {

    private JLabel input = new JLabel("Input :") ;
    private static JLabel  result = new JLabel("Cursor :") ;
    private InputArea inputArea;
    //private DrawPanel drawPanel;

    public BottomPanel(Controlleur controlleur){
        super();

        this.inputArea = new InputArea(controlleur);

        this.setLayout(new GridBagLayout());
        //this.drawPanel = drawPanel;

        inputArea.setPreferredSize(new Dimension(30, 25));
        input.setPreferredSize(new Dimension(30, 25));
        result.setPreferredSize(new Dimension(30, 25));


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(input,c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.7;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(inputArea,c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.2;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(result,c);

        this.setPreferredSize(new Dimension(800, 30));
    }

    public static void set_cursor_text(String s){
        result.setText("Cursor : "+s);
    }
}
