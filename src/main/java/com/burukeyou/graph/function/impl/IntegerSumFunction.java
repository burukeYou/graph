package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

/**
 * @author burukeyou
 */
public class IntegerSumFunction extends SumFunction<Integer> {

    @Override
    public Integer sumImpl(Integer value, Integer value2) {
        return value + value2;
    }
}
