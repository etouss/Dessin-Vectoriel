package Modele;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by ByTeK on 24/11/2014.
 */
public class Utilitaire {
    /**
     * Fonction effectuant le sqrt d'un big Decimal avec la formul de newton
     * @param A
     * @return
     */
    public static BigDecimal sqrt(BigDecimal A) {
        //Care for scale ?
        final int SCALE = 10;
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        //System.out.println(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal(2), SCALE, RoundingMode.HALF_UP);

        }
        return x1;
    }

    /**
     * Fonction effectuant le cos d'un BigDecimal en appliquant le DSE.
     * @param A
     * @return
     */
    public static BigDecimal cos(BigDecimal A) {
        //Care for scale
        //BigDecimal pi = new BigDecimal(Math.PI);
        final int SCALE = new BigDecimal(Math.PI).scale();
        BigDecimal coef = new BigDecimal(1);
        BigDecimal res = new BigDecimal(0);
        //BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()))
        for(int i = 0;i<10;i++){
            if(i%2==0)
                res = res.add(coef.multiply(A.pow(2*i)));
            else
                res = res.subtract(coef.multiply(A.pow(2*i)));
            coef = new BigDecimal(1);
            for(int j = 1;j<=2*(i+1);j++){
                coef = coef.divide(new BigDecimal(j),SCALE,RoundingMode.HALF_UP);
                //System.out.println(coef);
            }
            //System.out.println(res.doubleValue());
        }
        return res;
    }

    public static String posToString(BigDecimal x){
        DecimalFormat format = new DecimalFormat("0.00E0");
        String s = format.format(x);
        format.applyPattern("###0.###");
        String s2 = format.format(x);
        if(s.endsWith("E0")){
            return s2;
        }
        return s;
    }
}
