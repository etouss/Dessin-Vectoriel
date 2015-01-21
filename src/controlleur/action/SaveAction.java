package controlleur.action;

import controlleur.Controlleur;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class SaveAction extends AbstractAction {
    private Controlleur c;
    public SaveAction(Controlleur c) {
        super("Sauver", new ImageIcon("images/save-icon.png"));
        this.c = c;
    }

    private class myFc extends JFileChooser{
        public myFc(){
            super();
        }
        @Override
        public void approveSelection()
        {
            File tmp = this.getSelectedFile();
            if(!tmp.getName().endsWith(".svg")){
                File n = new File(tmp.getAbsolutePath()+".svg");
                this.setSelectedFile(n);
            }
            super.approveSelection();
        }
    }

    public void actionPerformed(ActionEvent ev) {
        JFrame popupMenu = null;
        final JFileChooser fc = new myFc();
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
        fc.showSaveDialog(popupMenu);
        String path = fc.getSelectedFile().getAbsolutePath();
        //System.out.println(path);
        c.save(path);
    }

}
