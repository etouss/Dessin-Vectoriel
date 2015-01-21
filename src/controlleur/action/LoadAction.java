package controlleur.action;

import controlleur.Controlleur;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class LoadAction extends AbstractAction {
    private Controlleur c;
    public LoadAction(Controlleur c) {
        super("Charger", new ImageIcon("images/load-icon.png"));
        this.c = c;
    }
    public void actionPerformed(ActionEvent ev) {
        JFrame popupMenu = null;
        final JFileChooser fc = new JFileChooser();
        FileFilter fl = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if(f.getName().endsWith(".svg"))return true;
                return false;
            }

            @Override
            public String getDescription() {
                return null;
            }
        };
        fc.addChoosableFileFilter(fl);
        fc.setAcceptAllFileFilterUsed(false);
        fc.showOpenDialog(popupMenu);
        String path = fc.getSelectedFile().getAbsolutePath();
        //System.out.println(path);
        c.load(path);
    }

}
