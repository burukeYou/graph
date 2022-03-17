package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

public class ShortSumFunction extends SumFunction<Short> {
    @Override
    protected Short sumImpl(Short value, Short value2) {
        return (short)(value + value2);
    }
}
