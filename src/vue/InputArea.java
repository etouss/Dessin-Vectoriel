package vue;

import Modele.Autocompletion;
import Modele.Parser;
import Modele.ParserExeption;
import controlleur.Controlleur;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe de la zone de saisie des manuel des figures
 */
public class InputArea extends JTextField implements KeyListener {
    /**
     *
     */
    private Parser parser;

    /**
     *
     */
    private Autocompletion autocompletion;


    public InputArea(Controlleur controlleur){
        autocompletion = new Autocompletion(this);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(border);
        parser = new Parser(controlleur, autocompletion);
        this.addKeyListener(this);
        setPreferredSize(new Dimension(10,10));
        this.setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){

            try {
                parser.parseCmd(getText());
                setText("");
            } catch (ParserExeption parserExeption) {
                JOptionPane.showOptionDialog(null, parserExeption.printMsg(),"", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            }
        }
        else if(keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
            autocompletion.tabInput();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}


}
