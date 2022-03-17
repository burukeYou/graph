package com.burukeyou.graph.iterator;

/**
 * 图遍历的迭代器
 * @author burukeyou
 */
public interface Iterator<T> {

    /**
     * 遍历初始化并获得第一个迭代的元素
     */
    T begin();

    /**
     * 判断下一个元素是否存在
     */
    boolean hasNext();

    /**
     * 获取下一个元素
     */
    T next();
}