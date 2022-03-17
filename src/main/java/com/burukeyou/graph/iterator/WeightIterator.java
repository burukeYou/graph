package com.burukeyou.graph.iterator;


import com.burukeyou.graph.entity.Edge;

/**
 * 有权图迭代器
 * @author burukeyou
 */
public interface WeightIterator<T,W extends Comparable<W>> {

    /**
     * 遍历初始化并获得第一个迭代的元素
     */
    Edge<T,W> begin();

    /**
     * 判断下一个元素是否存在
     */
    boolean hasNext();

    /**
     * 获取下一个元素
     */
    Edge<T,W> next();
}