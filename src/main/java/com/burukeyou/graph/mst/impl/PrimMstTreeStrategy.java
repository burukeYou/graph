package com.burukeyou.graph.mst.impl;

import com.burukeyou.graph.AbstractWeightGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.mst.MstTreeStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 基于Prim算法实现最小生成树
 * @author burukeyou
 * @param <T>
 * @param <W>
 */
public class PrimMstTreeStrategy<T,W extends Comparable<W>> implements MstTreeStrategy<T, W> {

    private AbstractWeightGraph<T, W> graph;

    public PrimMstTreeStrategy(AbstractWeightGraph<T, W> graph) {
        this.graph = graph;
    }

    @Override
    public MstTree<T, W> getMinimumSpanningTree() {
        int nodeSize = graph.nodeSize();
        int edgeSize = graph.edgeSize();
        List<Integer> nodeKeyList = graph.nodeKeyList;

        // 最小堆
        PriorityQueue<Edge<T,W>> minHeap = new PriorityQueue<>(edgeSize);

        //已生成最小树的顶点集合,true代表属于，false代表不属于
        boolean[] marked = new boolean[nodeSize];

        //已生成的最小树的边集合，
        List<Edge<T,W>> mstEdgeList = new ArrayList<>(nodeSize - 1);

        // 随机将一个节点划为一边
        Integer startNodeKey = nodeKeyList.get(0);
        visit(startNodeKey,marked,minHeap);

        while (!minHeap.isEmpty()){
            //取出最小边
            Edge<T,W> minEdge = minHeap.poll();

            //判断这条边的这两个顶点是否在同一个集合
            if (marked[minEdge.getKeyA()]  == marked[minEdge.getKeyB()]){
                continue;
            }

            //添加边到最小树边集合
            mstEdgeList.add(minEdge);

            //对新添加的边的对端节点继续进行visit操作
            if (!marked[minEdge.getKeyA()]){
                visit(minEdge.getKeyA(),marked,minHeap);
            } else{
                visit(minEdge.getKeyB(),marked,minHeap);
            }
        }
        return new MstTree<>(mstEdgeList);
    }

    // 处理node节点加入到最小树集合， 并把他的所有邻边放进堆。
    private void visit(int node,boolean[] marked, PriorityQueue<Edge<T,W>> minHeap) {
        //需是不属于已生成最小树的顶点集合
        assert (!marked[node]);

        //加入到已生成最小树的顶点集合
        marked[node] = true;

        // 将v的所有邻边放进
        List<Edge<T,W>> edgeList = graph.getAdjacentEdges(graph.keyNodeMap.get(node));
        for (Edge<T,W> edge : edgeList) {
            if (!marked[edge.getOther(node)]){
                minHeap.add(edge);
            }
        }
    }

}
