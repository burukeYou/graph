package com.burukeyou.graph.function;

import com.burukeyou.graph.function.impl.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author burukeyou
 */

public class SumFunctionEnum {

    private SumFunctionEnum(){}

    private static Map<Class<?>,SumFunction<?>> map = new HashMap<>();

    public static final SumFunction<Integer> INTEGER = new IntegerSumFunction();
    public static final SumFunction<Float> FLOAT = new FloatSumFunction();
    public static final SumFunction<Double> DOUBLE = new DoubleSumFunction();
    public static final SumFunction<Long> LONG = new LongSumFunction();
    public static final SumFunction<Byte> BYTE = new ByteSumFunction();
    public static final SumFunction<Short> SHORT = new ShortSumFunction();
    public static final SumFunction<BigDecimal> BIG_DECIMAL = new BigDecimalSumFunction();
    public static final SumFunction<BigInteger> BIG_INTEGER = new BigIntegerSumFunction();

    static {
        map.put(Integer.class,SumFunctionEnum.INTEGER);
        map.put(Float.class, SumFunctionEnum.FLOAT);
        map.put(Double.class, SumFunctionEnum.DOUBLE);
        map.put(Long.class, SumFunctionEnum.LONG);
        map.put(Byte.class, SumFunctionEnum.BYTE);
        map.put(Short.class, SumFunctionEnum.SHORT);
        map.put(BigDecimal.class, SumFunctionEnum.BIG_DECIMAL);
        map.put(BigInteger.class, SumFunctionEnum.BIG_INTEGER);
    }


    public static <W> SumFunction<W> getByClassType(Class<W> clz){
          return (SumFunction<W>) map.get(clz);
    }
}
