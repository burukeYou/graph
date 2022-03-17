package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

import java.math.BigInteger;

public class BigIntegerSumFunction extends SumFunction<BigInteger> {
    @Override
    protected BigInteger sumImpl(BigInteger value, BigInteger value2) {
        return value.add(value2);
    }
}
