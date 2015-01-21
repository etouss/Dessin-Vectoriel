package controlleur;

import Modele.*;
import controlleur.action.GrabAction;
import controlleur.action.SelectAction;
import vue.DrawPanel;
import vue.LeftPane;
import vue.RightPane;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.HashSet;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class Controlleur {

    private RightPane rightPane;
    private DrawPanel drawPanel;
    private LeftPane leftPane;
    private SelectAction selectAction;

    public void setSelectAction(SelectAction selectAction) {
        this.selectAction = selectAction;
    }

    public void initControlleur(RightPane rightPane, DrawPanel drawPanel,LeftPane leftPane){
        this.drawPanel = drawPanel;
        //this.drawPanel.controlleur = this;
        this.rightPane = rightPane;
        this.leftPane = leftPane;
    }

    public Controlleur(){

    }

    public void setRightPane(String name){
        if(name.equals(GrabAction.NAME) || (name.equals(SelectAction.NAME) && drawPanel.getSelectShape() == null)){
            rightPane.setVisible(false);
            return;
        }
        rightPane.setVisible(true);
        rightPane.setPan(name);
    }

    public void setMode(Mode mode){
        drawPanel.setMode(mode);
        drawPanel.setSousMode(null);
        //FormeVue d = drawPanel.getSelectShape();
        setSelectShape(null);
        drawPanel.repaint();
    }
    public void setSousMode(SousMode sousMode) {
        drawPanel.setSousMode(sousMode);
    }


    public void addToRightPan(JPanel jPanel, String name){
        rightPane.addLayout(jPanel, name);
    }

    public void updateLeftPane(HashSet<FormeVue> quadra){
        leftPane.setHTMLContent(quadra);
    }

    public void setSelectShape(FormeVue q){
        FormeVue d = drawPanel.getSelectShape();
        if(d!=null){
            d.setSelect(false);
            //if(drawPanel.mode == Mode.select)setRightPane("GRAB");
            //addToRightPan(null, "");
        }
        drawPanel.setAction(false);
        drawPanel.setSelectShape(q);
        if(drawPanel.getMode() == Mode.select){
            if(q!=null){
                selectAction.majSelectPanel(q);
                q.setSelect(true);
            }
            setRightPane(SelectAction.NAME);
            //System.out.println("4");
        }
    }
    public void forceUpdate(){
        drawPanel.repaint();
    }
    public void delete(FormeVue q){
        HashSet<FormeVue> up = q.delete(null);
        for(FormeVue tmp : up){
            System.out.println("del ::::: "+tmp);
            drawPanel.delete(tmp);
            //DrawPanel.map.delete(tmp);
        }
        forceUpdate();
    }
    public void ajouter(FormeVue q){
        drawPanel.ajouter(q);
    }
    public int getWidth(){
        return drawPanel.getWidth();
    }

    public int getHeigth(){
        return drawPanel.getHeight();
    }

    public boolean save(String path){
        Sauvegarde save = new Sauvegarde(drawPanel);
        return save.save(path);
    }

    public void load(String path){
        Charge charge = new Charge(drawPanel,this);
        charge.createSVGDoc(path);
        forceUpdate();
    }

    public void setZoomInBounds(FormeVue q){

        BigDecimal boundLeftX = null;
        BigDecimal boundRigthX= null;
        BigDecimal boundBotY= null;
        BigDecimal boundTopY= null;

        if(q!=null) {
            boundLeftX = q.getBoundLeftX();
            boundBotY = q.getBoundBotY();
            boundRigthX = q.getBoundRigthX();
            boundTopY= q.getBoundTopY();
        }
        else{
            for(FormeVue f : drawPanel.getQuadras()){
                if(boundLeftX == null){
                    boundLeftX = f.getBoundLeftX();
                    boundBotY = f.getBoundBotY();
                    boundTopY = f.getBoundTopY();
                    boundRigthX = f.getBoundRigthX();
                }
                if(boundLeftX.compareTo(f.getBoundLeftX()) > 0) boundLeftX = f.getBoundLeftX();
                if(boundRigthX.compareTo(f.getBoundRigthX()) < 0) boundRigthX = f.getBoundRigthX();
                if(boundBotY.compareTo(f.getBoundBotY()) > 0) boundBotY = f.getBoundBotY();
                if(boundTopY.compareTo(f.getBoundTopY()) < 0) boundTopY = f.getBoundTopY();
            }
        }
        DrawPanel.positionX = boundLeftX;
        DrawPanel.positionY = boundBotY;
        DrawPanel.offSetX = boundRigthX.subtract(boundLeftX).divide(new BigDecimal(drawPanel.getWidth()), RoundingMode.HALF_EVEN);
        DrawPanel.offSetY = boundTopY.subtract(boundBotY).divide(new BigDecimal(drawPanel.getHeight()), RoundingMode.HALF_EVEN);
        if(DrawPanel.offSetX.compareTo(DrawPanel.offSetY) > 0)
            DrawPanel.offSetY = DrawPanel.offSetX;
        else DrawPanel.offSetX = DrawPanel.offSetY;
        forceUpdate();
    }


}
