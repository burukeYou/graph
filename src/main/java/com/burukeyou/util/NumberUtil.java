package com.burukeyou.util;

import java.math.BigDecimal;

/**
 * @author burukeyou
 */
public class NumberUtil {

    private NumberUtil(){}

    public static Number getNumber(String value){
        BigDecimal decimal = new BigDecimal(value);
        return new Number(){
            private static final long serialVersionUID = 1L;
            @Override
            public int intValue() {
                return decimal.intValue();
            }
            @Override
            public long longValue() {
                return decimal.longValue();
            }
            @Override
            public float floatValue() {
                return decimal.floatValue();
            }
            @Override
            public double doubleValue() {
                return decimal.doubleValue();
            }
        };
    }

    public static Number sumNumber(Number a, Number b){
        BigDecimal aBig =  new BigDecimal(a.toString());
        BigDecimal bBig =  new BigDecimal(b.toString());
        BigDecimal sum = aBig.add(bBig);
        return getNumber(sum.toString());
    }


    public static <W extends Number & Comparable<W>> W add(W w, W weight) {

        return null;
    }
}
