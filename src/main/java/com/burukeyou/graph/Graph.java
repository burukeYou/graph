package com.burukeyou.graph;


/**
 * 无权图
 * @author burukeyou
 * @param <T>       节点对象。 注意： 如果是自定义对象必须重写 equals 和 hashCode 方法
 */
public interface Graph<T> extends IGraph<T> {

    /**
     * 无权图添加边
     */
    void addEdge(T startNode,T endNode);

}
