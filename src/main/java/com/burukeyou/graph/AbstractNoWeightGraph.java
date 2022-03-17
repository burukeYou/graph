package com.burukeyou.graph;

import com.burukeyou.graph.iterator.Iterator;

/**
 * 无权图
 * @author burukeyou
 */
public abstract class AbstractNoWeightGraph<T> extends AbstractCommonGraph<T> implements Graph<T> {

    protected AbstractNoWeightGraph(int initNodeSize, boolean isDirectedGraph) {
        super(initNodeSize, isDirectedGraph);
    }

    protected AbstractNoWeightGraph(boolean isDirectedGraph) {
        super(isDirectedGraph);
    }

    @Override
    protected boolean hasEdge(Integer startKey, Integer endKey) {
        return false;
    }

    @Override
    public void addEdge(T startNode, T endNode) {
        if (startNode == null || endNode == null) {
            throw new IllegalArgumentException("Invalid param:  Any node added should not be empty");
        }

        // 判断新增的边是否已经存在
        if (hasEdge(nodeKeyMap.get(startNode),nodeKeyMap.get(endNode))){
            return;
        }

        // 为每个节点分配key
        Integer startKey = distributeNodeKey(startNode);
        Integer endKey = distributeNodeKey(endNode);

        // 添加边
        addEdge(startKey, endKey);

        edgeSize++;
    }

    protected abstract void addEdge(Integer startKey, Integer endKey);

    @Override
    public abstract boolean hasAdjacentEdge(Integer nodeKey);


    @Override
    public Iterator<T> iterator(T curNode) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

}
