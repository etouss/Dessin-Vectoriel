package vue;


import Modele.Utilitaire;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Repere extends Path2D.Double {
    private int x;
    private int y;
    private int heigth;
    private int width;
    private Path2D.Double grille;
    private Path2D.Double pointCenter;
    private Rectangle centre;
    private static final Stroke PointStroke = new BasicStroke(10.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke normalStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public Repere(int x, int y){
        super();
        this.x = x;
        this.y = y;
        this.centre = new Rectangle(x-10,y-10,20,20);
        grille = new Path2D.Double();
        pointCenter = new Path2D.Double();


    }

    public boolean isOver(MouseEvent e, int width, int height){
        return overCenter(e) || overX(e, width) || overY(e, height);
    }

    public boolean overCenter(MouseEvent e){
        return centre.intersects(new Rectangle(e.getX()-5,e.getY()-5,20,20));
    }

    public boolean overY(MouseEvent e, int height){
        return new Rectangle(x-10,0,20,height/2-10).intersects(new Rectangle(e.getX()-10,e.getY()-10,20,20))||
                new Rectangle(x-10,y+10,20,height/2-10).intersects(new Rectangle(e.getX()-10,e.getY()-10,20,20));
    }

    public boolean overX(MouseEvent e, int width){
        return new Rectangle(0,y-10,width/2-10,20).intersects(new Rectangle(e.getX()-10,e.getY()-10,20,20))||
                new Rectangle(x+10,y-10,width/2-10,20).intersects(new Rectangle(e.getX()-10,e.getY()-10,20,20));
    }


    public void updateRepere(Graphics2D g2d){


        this.reset();
        updateGrille(g2d);
        updateCenter(g2d);
        g2d.setColor(Color.BLACK);
        BigDecimal xValeur = new BigDecimal(x).multiply(DrawPanel.offSetX).add(DrawPanel.positionX);
        BigDecimal yValeur = new BigDecimal(heigth-y).multiply(DrawPanel.offSetY).add(DrawPanel.positionY);
        moveTo(x, 0);
        lineTo(x, heigth);
        moveTo(0, y);
        lineTo(width, y);
        int marge = width/DrawPanel.WIDTH_RATIO;
        int i = 1;
        while( (y+i*marge)<heigth) {
            String s = Utilitaire.posToString(new BigDecimal(0).subtract(DrawPanel.offSetY.multiply(new BigDecimal(i * marge))));
            int lenY = (int) g2d.getFontMetrics().getStringBounds(s, g2d).getHeight();
            int startY = lenY/2;
            int lenX = (int) g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
            g2d.drawString(s, x - lenX - marge / 10, y + i * marge + startY - 1);
            i ++;
        }
        i= -1   ;
        while( (y+i*marge)>0) {
            String s = Utilitaire.posToString(new BigDecimal(0).subtract(DrawPanel.offSetY.multiply(new BigDecimal(i * marge))));
            int lenY = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getHeight();
            int startY = lenY/2;

            int lenX = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
            g2d.drawString(s, x-lenX-marge/10, y+i*marge+startY-1);
            i--;
        }
        i=1;
        while( (x+i*marge)<width) {
            String s = Utilitaire.posToString(DrawPanel.offSetX.multiply(new BigDecimal(i * marge)));
            drawStringRepere(g2d,s,x, y, i, width);
            i ++;
        }
        i= -1   ;
        while( (x+i*marge)>0) {
            String s = Utilitaire.posToString(DrawPanel.offSetX.multiply(new BigDecimal(i * marge)));
            drawStringRepere(g2d,s,x, y, i, width);
            i--;
        }
        String xString = Utilitaire.posToString(xValeur);
        String yString = Utilitaire.posToString(yValeur);
        g2d.setColor(new Color(100, 100, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("(" + xString + " ; " + yString + ")", 0, heigth - 10);
        g2d.setColor(Color.BLACK);
        g2d.draw(this);
    }

    public void updateGrille(Graphics2D g2d){
        //System.out.print("refgr");
        grille.reset();
        g2d.setColor(new Color(100,100,100,50));
        int marge = width/DrawPanel.WIDTH_RATIO;
        int i = 1;
        while( (y+i*marge)<heigth) {
            grille.moveTo(0, y + i * marge);
            grille.lineTo(width, y + i * marge);
            i ++;
        }
        i= -1   ;
        while( (y+i*marge)>0) {
            grille.moveTo(0, y + i * marge);
            grille.lineTo(width, y + i * marge);
            i--;
        }
        i=1;
        while( (x+i*marge) < width) {
            grille.moveTo(x + i * marge, 0);
            grille.lineTo(x + i * marge, heigth);
            i ++;
        }
        i= -1   ;
        while( (x + i * marge) > 0) {
            grille.moveTo(x + i * marge, 0);
            grille.lineTo(x + i * marge, heigth);
            i--;
        }
        g2d.draw(grille);
    }
    public void updateCenter(Graphics2D g2d){
        //System.out.print("refgr");
        pointCenter.reset();
        pointCenter.moveTo(x,y);
        pointCenter.lineTo(x,y);
        g2d.setStroke(PointStroke);
        g2d.setColor(new Color(100,100,255));
        g2d.draw(pointCenter);
        g2d.setStroke(normalStroke);
    }
    private void drawStringRepere(Graphics2D g2d, String s, int x, int y, int i, int width){
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        int marge = width/DrawPanel.WIDTH_RATIO;
        int lenX, startX;
        lenX = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        while (marge< lenX+5){

            g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, g2d.getFont().getSize()-1));
            lenX = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        }
        startX = lenX/2;
        int lenY = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getHeight();
        g2d.drawString(s, x+i*marge-startX, y+marge / 10 + lenY);
    }


    public void moveX(double nb){
        x += nb;
    }

    public void moveY(double nb){

        y += nb;
    }
    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
