package controlleur.action;

import Modele.FormeVue;
import Modele.Mode;
import controlleur.Controlleur;
import vue.forme.Point.PointVector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class SelectAction extends JToggleButton {

    public final static String NAME = "SELECT" ;
    private JPanel panel;
    private Controlleur c;
    public SelectAction(Controlleur c) {
        super();
        this.c = c;
        creatSelectpanel();
        //c.addToRightPan(panel, NAME);
        c.setSelectAction(this);
        this.setAction(new AbstractAction(NAME) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.setRightPane(SelectAction.NAME);
                c.setMode(Mode.select);
            }
        });
        this.setIcon(new ImageIcon("images/select-icon.png"));
        this.setHideActionText(true);
        this.repaint();
    }


    public void creatSelectpanel(){
        this.panel = new JPanel();
        c.addToRightPan(this.panel,SelectAction.NAME);
    }

    public void majSelectPanel(FormeVue q){
        panel.removeAll();
        this.panel.add(new JLabel(q.getName()));
        if(!(q instanceof PointVector))this.panel.add(new SliderRotate(q));
        if(!(q instanceof PointVector))this.panel.add(new SliderScale(q));
        this.panel.add(new ColorChooser(q));
        panel.revalidate();
        panel.repaint();


    }

    private class ColorChooser extends  JButton{
        FormeVue q;
        public ColorChooser(FormeVue q){
            super("Couleur ?");
            this.q = q;
            addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(null, "Choisir la couleur du Stroke", null);
                    if (color != null) {
                        if(color.getAlpha()>127) {
                            Color clr = new Color(color.getRed(), color.getGreen(), color.getBlue(),127);
                            //System.out.println(color.getAlpha());
                            q.setClr(clr);
                        }
                        //else q.clr = color;
                        else q.setClr(color);
                    }
                    c.forceUpdate();
                }
            });

        }
    }

    private class SliderRotate extends JSlider{
        public SliderRotate(FormeVue q){
            super(0,360);
            this.setValue(q.getRotation());
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    q.setRotate(getValue(), q.getListe_points().get(0), new HashSet<>());
                    c.forceUpdate();
                }
            });
            this.setPreferredSize(new Dimension(130, 15));
        }
    }

    private class SliderScale extends JSlider{
        public SliderScale(FormeVue q){
            super(0,1000);
            this.setValue(q.getScale());
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    q.setScale(getValue(), new HashSet<>());
                    c.forceUpdate();
                }
            });
            this.setPreferredSize(new Dimension(130, 15));
        }
    }
}