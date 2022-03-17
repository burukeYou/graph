package com.burukeyou.graph.mst.impl;

import com.burukeyou.graph.AbstractWeightGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.mst.MstTreeStrategy;
import com.burukeyou.unionfind.UnionFind;

import java.util.*;


/**
 * 基于Kruskal算法 实现最小生成树
 * @author burukeyou
 * @param <T>
 * @param <W>
 *
 */
public class KruskalMstTreeStrategy<T,W extends Comparable<W>> implements MstTreeStrategy<T, W> {

    private AbstractWeightGraph<T, W> graph;

    public KruskalMstTreeStrategy(AbstractWeightGraph<T, W> graph) {
        this.graph = graph;
    }

    @Override
    public MstTree<T, W> getMinimumSpanningTree() {
        // 每次从所有边中选取权值最小的边，并且在最小树集合中不形成回路则加入到最小树中
        int edgeSize = graph.edgeSize();

        //已生成的最小树的边集合，
        List<Edge<T,W>> mstTreeList = new ArrayList<>();

        // 最小堆
        PriorityQueue<Edge<T,W>> minHeap = new PriorityQueue<>(edgeSize);

        Set<Edge<T,W>> existEdgeSet = new HashSet<>(edgeSize);

        //遍历每个节点, 得到所有边放进最小堆
        for (T node : graph.nodeSet) {
            List<Edge<T, W>> adjacentEdges = graph.getAdjacentEdges(node);
            for (Edge<T, W> edge : adjacentEdges) {
                if (!existEdgeSet.contains(edge)){
                    existEdgeSet.add(edge);
                    minHeap.add(edge);
                }
            }
        }

        // 并查集   --用来判断新加入到最小树的边是否形成回路, 因为新加入的两个点如果属于同一个集合， 说明就是有回路
        UnionFind<Integer> unionFind = new UnionFind<>(edgeSize);

        // Kruskal 算法
        while (!minHeap.isEmpty() && mstTreeList.size() < edgeSize - 1){
             Edge<T,W> minEdge = minHeap.poll();

            if (unionFind.isConnected(minEdge.getKeyA(),minEdge.getKeyB())){
                continue;
            }

            mstTreeList.add(minEdge);
            unionFind.union(minEdge.getKeyA(),minEdge.getKeyB());
        }


        return new MstTree<>(mstTreeList);
    }
}
