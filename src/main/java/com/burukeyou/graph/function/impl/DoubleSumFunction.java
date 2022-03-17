package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

public class DoubleSumFunction extends SumFunction<Double> {
    @Override
    protected Double sumImpl(Double value, Double value2) {
        return value + value2;
    }
}
