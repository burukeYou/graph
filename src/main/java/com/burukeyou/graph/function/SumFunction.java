package com.burukeyou.graph.function;

/**
 * 自定义加法器
 * @author burukeyou
 */
public abstract class SumFunction<W> {

    protected abstract W sumImpl(W value,W value2);

    public W sum(W value,W value2){
        if (value != null && value2 != null){
            return sumImpl(value,value2);
        }else{
            return value == null ? value2 : value;
        }
    }
}
