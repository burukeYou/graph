package com.burukeyou.graph.impl;

import com.burukeyou.graph.AbstractWeightGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.iterator.Iterator;
import com.burukeyou.graph.iterator.impl.AdjacencyListsWeightIterator;
import com.burukeyou.util.CollectionsUtil;

import java.util.*;


/**
 * 领接表-有权图
 * @author burukeyou
 * @param <T>           节点类型
 * @param <W>           权值类型
 */
public class AdjacencyListsWeightGraph<T,W extends Comparable<W>> extends  AbstractWeightGraph<T,W>{

    //private LinkedList<Edge<W>[] graphData;

    // Map<节点序号， 相邻边列表>
    private final Map<Integer, LinkedList<Edge<T,W>>> graphData = new HashMap<>();

    public AdjacencyListsWeightGraph(boolean isDirectedGraph) {
        super(isDirectedGraph);
    }

    @Override
    public boolean hasAdjacentEdge(Integer nodeKey) {
        return graphData.containsKey(nodeKey) && CollectionsUtil.isNotEmpty(graphData.get(nodeKey));
    }

    public Map<Integer, LinkedList<Edge<T,W>>> getGraphData() {
        return graphData;
    }

    @Override
    protected boolean hasEdge(Integer startKey, Integer endKey) {
        if (startKey == null || endKey == null){
            return false;
        }
        LinkedList<Edge<T,W>> edgesList = graphData.get(startKey);
        if (CollectionsUtil.isEmpty(edgesList)){
            return false;
        }

        for (Edge<T,W> edge : edgesList) {
            if (edge.getOther(startKey) == endKey){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator(T curNode) {
        return new AdjacencyListsWeightIterator<>(curNode,this);
    }

    @Override
    public Iterator<T> iterator() {
        return new AdjacencyListsWeightIterator<>(this);
    }

    @Override
    protected void addEdge(Integer startKey, Integer endKey, W weight) {
        graphData.putIfAbsent(startKey,new LinkedList<>());

        T startNode = keyNodeMap.get(startKey);
        T endNode = keyNodeMap.get(endKey);

        graphData.get(startKey).add(new Edge<>(startKey,startNode, endKey,endNode, weight));

        //无向边
        if (!startKey.equals(endKey) && !isDirectedGraph){
            graphData.putIfAbsent(endKey,new LinkedList<>());
            graphData.get(endKey).add(new Edge<>(endKey, endNode,startKey, startNode,weight));
        }
    }


    @Override
    public List<Edge<T,W>> getAdjacentEdges(T node) {
        Integer key = nodeKeyMap.get(node);
        if (key != null){
            return graphData.get(key);
        }
        return new ArrayList<>();
    }
}
