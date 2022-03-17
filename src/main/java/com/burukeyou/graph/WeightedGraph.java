package com.burukeyou.graph;

import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.entity.ShortPathTree;
import com.burukeyou.graph.function.SumFunction;

import java.util.List;

/**
 * 有权图
 * @author burukeyou
 * @param <T>           节点类型
 * @param <W>           权值类型
 */
public interface WeightedGraph<T,W extends Comparable<W>> extends IGraph<T> {

    /**
     * 有权图添加边
     */
     void addEdge(T startNode, T endNode , W weight);

    /**
     * 获取某个节点相邻的边
     * @param node
     * @return
     */
    List<Edge<T,W>> getAdjacentEdges(T node);

    /**
     * 获取最小生成树
     */
    MstTree<T,W> getMinimumSpanningTree();

    MstTree<T,W> getMinimumSpanningTreePrim();

    MstTree<T,W> getMinimumSpanningTreeKruskal();

    /**
     * 找到从源点到其他所有节点的最短路径
     * @param  startNode         源点
     * @param  wClass            权值的Class类型
     */
    ShortPathTree<T,W> getShortPathTree(T startNode, Class<W> wClass);

    /**
     * 找到从源点到其他所有节点的最短路径
     * @param startNode             源点
     * @param wClass                权值类型
     * @param sumFunction           自定义权值加法器
     */
    ShortPathTree<T,W> getShortPathTree(T startNode, Class<W> wClass, SumFunction<W> sumFunction);



}
