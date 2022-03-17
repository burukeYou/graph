package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

import java.math.BigDecimal;

public class BigDecimalSumFunction extends SumFunction<BigDecimal> {
    @Override
    protected BigDecimal sumImpl(BigDecimal value, BigDecimal value2) {
        return value.add(value2);
    }
}
