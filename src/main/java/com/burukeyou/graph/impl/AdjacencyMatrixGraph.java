package com.burukeyou.graph.impl;

import com.burukeyou.graph.AbstractNoWeightGraph;
import com.burukeyou.graph.iterator.Iterator;
import com.burukeyou.graph.iterator.impl.AdjacencyMatrixIterator;

/**
 * 邻接矩阵-无权图
 *          （适合稠密图（边的数量远远大于节点数量）
 * @author burukeyou
 *
 */
public class AdjacencyMatrixGraph<T> extends AbstractNoWeightGraph<T> {

    private final int[][] graphData;

    /**
     * 构造函数
     * @param initNodeSize              初始化的矩阵空间大小
     * @param isDirectedGraph           是否是有向图
     */
    public AdjacencyMatrixGraph(int initNodeSize, boolean isDirectedGraph) {
        super(initNodeSize,isDirectedGraph);
        this.graphData = new int[initNodeSize][initNodeSize];
    }

    public int[][] getGraphData() {
        return graphData;
    }

    @Override
    protected void addEdge(Integer startKey, Integer endKey) {
        this.graphData[startKey][endKey] = 1;
        //不是有向图
        if (!isDirectedGraph){
            this.graphData[endKey][startKey] = 1;
        }
    }

    @Override
    public boolean hasAdjacentEdge(Integer nodeKey){
        boolean flag = false;
        for (int i = 0; i < initNodeSize; i++) {
            if(this.graphData[nodeKey][i] > 0){
                flag = true;
                break;
            }
        }
        return flag;
    }


    @Override
    protected boolean hasEdge(Integer v, Integer w){
        if (v == null || w == null){
            return false;
        }

        assert(v >= 0 || v < nodeSize());
        assert(w >= 0 || w < nodeSize());
        return this.graphData[v][w] > 0;
    }


    @Override
    public Iterator<T> iterator(T curNode) {
        return new AdjacencyMatrixIterator<>(curNode,this);
    }

    @Override
    public Iterator<T> iterator() {
        return  new AdjacencyMatrixIterator<>(this);
    }


}
