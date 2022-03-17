package com.burukeyou.graph;

import com.burukeyou.graph.iterator.Iterator;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 图基本功能接口
 * @param <T>
 */
public interface IGraph<T> {

    /**
     * 获取图节点的数量
     */
    int nodeSize();

    /**
     * 获取图边的数量
     */
    int edgeSize();

    /**
     *  计算图连通分量个数
     */
    int componentCount();

    /**
     * 返回图节点v的迭代器， 可获取v节点的邻边节点
     */
    Iterator<T> iterator(T curNode);

    /**
     * 返回图迭代器， 可以获取图的每一个节点
     */
    Iterator<T> iterator();

    /**
     * 获取某个节点的所有有领边节点
     * @param curNode
     * @return
     */
    List<T> getAdjacentNodes(T curNode);

    /**
     *  图的深度优先遍历
     */
    void dfsTraverse(Consumer<T> consumer);
    void dfsTraverse(T startNode,Consumer<T> consumer);

    void dfsTraverse();

    /**
     *  图的广度优先遍历
     */
    void bfsTraverse(Consumer<T> consumer);

    /**
     * 找到从 startNode 到其他节点的最短路径距离 (适合无权图)
     * @param startNode
     * @return              Map<到达的节点，路径>
     */
    Map<T,List<T>> findShortPathFromStartNode(T startNode);

    /**
     * 找到所有从开始节点 到 结束节点的路径
     * @param startNode
     * @param endNode
     */
    List<List<T>> findAllPath(T startNode, T endNode);

    /**
     *  寻找从 startNode 到 endNode的一条路径。 ps： 即使有多条也只会找到一条
     */
    List<T> findOnePath(T startNode, T endNode);

    /**
     *  判断图是否有环
     */
    boolean hasRing();
}
