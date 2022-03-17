package com.burukeyou.graph.mst.impl;

import com.burukeyou.graph.AbstractWeightGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.mst.MstTreeStrategy;
import com.burukeyou.heap.Entry;
import com.burukeyou.heap.IndexHeap;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Prim算法-使用索引堆 实现最小生成树
 * @author burukeyou
 * @param <T>
 * @param <W>
 *
 *
 */
public class PrimMstTreePlusStrategy<T,W extends Comparable<W>> implements MstTreeStrategy<T, W> {

    private AbstractWeightGraph<T, W> graph;

    public PrimMstTreePlusStrategy(AbstractWeightGraph<T, W> graph) {
        this.graph = graph;
    }

    @Override
    public MstTree<T, W> getMinimumSpanningTree() {
        int nodeSize = graph.nodeSize();
        int edgeSize = graph.edgeSize();
        List<Integer> nodeKeyList = graph.nodeKeyList;

        // 记 已构成最小树的顶点集合为 U,  还未构成最小树的顶点的集合记为 V

        // 最小索引堆  -记录从 U 到 V的所有横切边    -IndexHeap<V的顶点x, U到节点x的横切边的权值minWeight>
        IndexHeap<Integer, W> indexMinHeap = new IndexHeap<>(true);

        //记录从 U 到 V的最小横切边,
        // 表示 edgeTo[V的顶点x] =  U到V的最小横切边    边(U顶点, V的顶点x,minWeight)
        Edge<T,W>[] edgeTo = new Edge[edgeSize];

        // 存储U, marked[i]为true 该顶点i表示属于 U集合
        boolean[] marked = new boolean[nodeSize];

        //已生成的最小树的边集合，
        List<Edge<T,W>> mstEdgeList = new ArrayList<>(nodeSize - 1);

        // 随机将一个节点划为一边
        Integer startNodeKey = nodeKeyList.get(0);
        visit(startNodeKey,marked,indexMinHeap, edgeTo);

        while (!indexMinHeap.isEmpty()){
            //取出最小边
            Entry<Integer, W> minNode = indexMinHeap.remove();
            Integer minNodekey = minNode.getKey();

            if (edgeTo[minNodekey] != null){
                mstEdgeList.add(edgeTo[minNodekey]); // 将最小的横切边加入
                visit(minNodekey, marked,indexMinHeap,edgeTo);
            }
        }
        return new MstTree<>(mstEdgeList);
    }

    // 处理node节点
    private void visit(int node,boolean[] marked, IndexHeap<Integer, W> indexMinHeap,Edge<T,W>[] edgeTo) {
        //需是不属于已生成最小树的顶点集合
        assert (!marked[node]);

        //加入到已生成最小树的顶点集合
        marked[node] = true;

        // 将node的所有邻边
        List<Edge<T,W>> edgeList = graph.getAdjacentEdges(graph.keyNodeMap.get(node));

        //
        for (Edge<T,W> edge : edgeList) {
            // 相邻节点
            int otherNode = edge.getOther(node);

            // 相邻节点不在最小树的顶点集合
            if (!marked[otherNode]){
                // 说明是新遍历的边， 不在edgeTo里， 添加到 indexMinHeap 和 edgeTo
                if (edgeTo[otherNode] == null){
                    indexMinHeap.add(otherNode,edge.getWeight());
                    edgeTo[otherNode] = edge;
                } else if (edge.getWeight().compareTo(edgeTo[otherNode].getWeight()) < 0){
                    // 已存在， 找到了更小的横切边， 即从U到V的顶点otherNode的横切边。 则需要更新 indexMinHeap 和 edgeTo
                    edgeTo[otherNode] = edge;
                    indexMinHeap.replace(otherNode, edge.getWeight());
                }else{
                    // 说明此条横切边 大于之前的横切边不做处理
                }
            }
        }
    }

}
