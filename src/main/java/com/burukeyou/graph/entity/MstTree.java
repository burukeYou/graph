package com.burukeyou.graph.entity;

import java.util.List;

/**
 *  最小生成树
 * @author burukeyou
 * @param <T>   节点类型
 * @param <W>   边上权值类型
 */
public class MstTree<T,W extends Comparable<W>> {

    /**
     *  最小生成树包含的边
     */
    private List<Edge<T,W>> edgeList;

    public MstTree(List<Edge<T, W>> edgeList) {
        this.edgeList = edgeList;
    }

    public List<Edge<T, W>> getEdgeList() {
        return edgeList;
    }
}
