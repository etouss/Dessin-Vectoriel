import controlleur.Controlleur;
import vue.MainFrame;

import javax.swing.*;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class Main {

    public static class ThreadParcours extends Thread{
        public ThreadParcours(){
            super();
        }
        public void run(){

        }
    }

    private static void createAndShowGUI(Controlleur controlleur) {
        MainFrame mainFrame = new MainFrame(controlleur);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
    public static void main(String[] args) {
        /*String str = "SEGMENT [(0,0;1;2);(3;4,4;5);(6;7;8,8)]";
        Parser p = new Parser();
        p.parseCmd(str);
        */
        Controlleur controlleur = new Controlleur();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI(controlleur);
            }
        });
    }
}
