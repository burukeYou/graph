/**
 *
 */
package com.burukeyou.graph.impl;

import com.burukeyou.graph.AbstractNoWeightGraph;
import com.burukeyou.graph.iterator.Iterator;
import com.burukeyou.graph.iterator.impl.AdjacencyListsIterator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 领接表-无权图
 *      （适合稀疏图 边的数量远远小于节点数量）
 *
 * @param <T>
 * @author burukeyou
 */
public class AdjacencyListsGraph<T> extends AbstractNoWeightGraph<T> {

    // Map<节点序号， 相邻节点序号列表>
    private final Map<Integer, LinkedList<Integer>> graphData = new HashMap<>();

    public AdjacencyListsGraph(boolean isDirectedGraph) {
        super(isDirectedGraph);
    }

    public Map<Integer, LinkedList<Integer>> getGraphData() {
        return graphData;
    }

    @Override
    protected void addEdge(Integer startKey, Integer endKey) {
        //graphData[startKey].add(endKey);
        graphData.putIfAbsent(startKey, new LinkedList<>());
        graphData.get(startKey).add(endKey);

        //无向边 --忽略自旋边
        if (!startKey.equals(endKey) && !isDirectedGraph) {
            graphData.putIfAbsent(endKey, new LinkedList<>());
            graphData.get(endKey).add(startKey);
        }
    }

    @Override
    protected boolean hasEdge(Integer v, Integer w) {
        if (v == null || w == null) {
            return false;
        }

        assert (v >= 0 || v < nodeSize());
        assert (w >= 0 || w < nodeSize());
        return graphData.get(v) != null && graphData.get(v).contains(w);
    }

    @Override
    public Iterator<T> iterator(T curNode) {
        return new AdjacencyListsIterator<>(curNode, this);
    }

    @Override
    public Iterator<T> iterator() {
        return new AdjacencyListsIterator<>(this);
    }

    @Override
    public boolean hasAdjacentEdge(Integer nodeKey) {
        return graphData.get(nodeKey) != null && !graphData.get(nodeKey).isEmpty();
    }
}
