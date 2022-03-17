package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

public class LongSumFunction extends SumFunction<Long> {
    @Override
    protected Long sumImpl(Long value, Long value2) {
        return value + value2;
    }
}
