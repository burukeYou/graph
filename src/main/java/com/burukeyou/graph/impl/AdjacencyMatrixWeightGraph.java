package com.burukeyou.graph.impl;

import com.burukeyou.graph.AbstractWeightGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.iterator.Iterator;
import com.burukeyou.graph.iterator.impl.AdjacencyMatrixWeightIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  领结矩阵-有权图
 * @author burukeyou
 * @param <T>           图节点泛型
 * @param <W>           权值泛型
 */
public class AdjacencyMatrixWeightGraph<T,W extends  Comparable<W>> extends AbstractWeightGraph<T,W> {

    private Edge<T,W>[][] graphData;

    /**
     * 构造函数
     * @param initNodeSize              初始化的矩阵空间大小
     * @param isDirectedGraph           是否是有向图
     */
    public AdjacencyMatrixWeightGraph(int initNodeSize, boolean isDirectedGraph) {
        super(initNodeSize, isDirectedGraph);
        this.graphData = new Edge[initNodeSize][initNodeSize];
    }

    public Edge<T,W>[][] getGraphData() {
        return graphData;
    }

    @Override
    public boolean hasAdjacentEdge(Integer nodeKey) {
        boolean flag = false;
        for (int i = 0; i < initNodeSize; i++) {
            if(this.graphData[nodeKey][i] != null){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    protected  void addEdge(Integer startKey, Integer endKey, W weight) {
        T startNode = keyNodeMap.get(startKey);
        T endNode = keyNodeMap.get(endKey);
        graphData[startKey][endKey] = new Edge<>(startKey,startNode,endKey,endNode,weight);
        //不是有向图
        if (!isDirectedGraph){
            graphData[endKey][startKey] = new Edge<>(endKey, endNode,startKey, startNode,weight);
        }
    }

    @Override
    public List<Edge<T,W>> getAdjacentEdges(T node) {
        Integer key = nodeKeyMap.get(node);
        if (key != null){
            return Arrays.stream(graphData[key]).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    protected boolean hasEdge(Integer startKey, Integer endKey) {
        if (startKey == null || endKey == null){
            return false;
        }

        assert(startKey >= 0 || startKey < nodeSize());
        assert(endKey >= 0 || endKey < nodeSize());
        return this.graphData[startKey][startKey] != null;
    }


    @Override
    public Iterator<T> iterator(T curNode) {
        return new AdjacencyMatrixWeightIterator<>(curNode, this);
    }

    @Override
    public Iterator<T> iterator() {
        return new AdjacencyMatrixWeightIterator<>(this);
    }

}
