package vue.forme.Point;

import Modele.FormeNotFoundExeption;
import Modele.FormeVue;
import vue.DrawPanel;
import vue.forme.Droite.DroiteVector;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by ByTeK on 23/11/2014.
 */
public class PointInterVector extends PointVector {
    private DroiteVector d1;
    private DroiteVector d2;

    public PointInterVector(DroiteVector d1,DroiteVector d2)throws FormeNotFoundExeption{
        super(new BigDecimal(0), new BigDecimal(0), d1.getWidth(), d1.getHeigth(), null,false);
        this.d1 = d1;
        this.d2 = d2;
        d1.setInter(this);
        d2.setInter(this);
        setInter();
        this.movable = false;
        this.clr = new Color(255, 89, 75, 255);
        this.updateBounds();
    }

    public void setInter()throws FormeNotFoundExeption{
        if(d1.coefDir.compareTo(d2.coefDir) == 0){
            this.delete(this);
            throw new FormeNotFoundExeption();
        }
        BigDecimal x = d1.origine.subtract(d2.origine).divide(d2.coefDir.subtract(d1.coefDir), RoundingMode.HALF_EVEN);
        BigDecimal y = d1.coefDir.multiply(x).add(d1.origine);
        this.positionX = x;
        this.positionY = y;
        this.updateBounds();
    }

}
