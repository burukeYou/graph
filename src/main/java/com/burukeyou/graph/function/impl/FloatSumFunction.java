package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

public class FloatSumFunction extends SumFunction<Float> {

    @Override
    public Float sumImpl(Float value, Float value2) {
        return value + value2;
    }
}
