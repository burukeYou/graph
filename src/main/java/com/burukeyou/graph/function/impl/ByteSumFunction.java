package com.burukeyou.graph.function.impl;

import com.burukeyou.graph.function.SumFunction;

public class ByteSumFunction extends SumFunction<Byte> {
    @Override
    protected Byte sumImpl(Byte value, Byte value2) {
        return (byte)(value + value2);
    }
}
