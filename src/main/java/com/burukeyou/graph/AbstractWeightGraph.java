package com.burukeyou.graph;

import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.entity.ShortPathTree;
import com.burukeyou.graph.function.SumFunction;
import com.burukeyou.graph.function.SumFunctionEnum;
import com.burukeyou.graph.mst.MstTreeStrategy;
import com.burukeyou.graph.mst.impl.KruskalMstTreeStrategy;
import com.burukeyou.graph.mst.impl.PrimMstTreeStrategy;
import com.burukeyou.heap.Entry;
import com.burukeyou.heap.IndexHeap;

import java.lang.reflect.Array;
import java.util.*;

/**
 *  有权图抽象
 * @author burukeyou
 * @param <T>           节点类型
 * @param <W>           权值类型
 */
public abstract class AbstractWeightGraph<T,W extends Comparable<W>> extends AbstractCommonGraph<T> implements WeightedGraph<T, W> {

    protected AbstractWeightGraph(int nodeSize, boolean isDirectedGraph) {
        super(nodeSize, isDirectedGraph);
    }

    protected AbstractWeightGraph(boolean isDirectedGraph) {
        super(isDirectedGraph);
    }

    @Override
    public abstract boolean hasAdjacentEdge(Integer nodeKey);

    @Override
    public void addEdge(T startNode, T endNode , W weight) {
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

        //
        addEdge(startKey,endKey,weight);

        //
        this.edgeSize++;
    }

    protected abstract void addEdge(Integer startKey, Integer endKey, W weight);


    @Override
    public MstTree<T, W> getMinimumSpanningTree() {
        if (isDirectedGraph){
            throw new RuntimeException("get Minimum Spanning Tree Only supports undirected graphs ");
        }
        //基于 Prim 算法 实现
        //MstTreeStrategy<T, W> strategy = new PrimMstTreeStrategy<>(this);

        // 基于 Prim-索引堆 实现
        //MstTreeStrategy<T, W> strategy = new PrimMstTreePlusStrategy<>(this);

        // 基于Kruskal算法
        MstTreeStrategy<T, W> strategy = new KruskalMstTreeStrategy<>(this);

        MstTree<T,W> mstTree = strategy.getMinimumSpanningTree();
        return mstTree;
    }

    @Override
    public MstTree<T,W> getMinimumSpanningTreePrim(){
        return  new PrimMstTreeStrategy<>(this).getMinimumSpanningTree();
    }

    @Override
    public MstTree<T,W> getMinimumSpanningTreeKruskal(){
        return new KruskalMstTreeStrategy<>(this).getMinimumSpanningTree();
    }


    @Override
    public abstract List<Edge<T,W>> getAdjacentEdges(T node);

    public  List<Edge<T,W>> getAdjacentEdges(Integer nodeKey){
        return getAdjacentEdges(keyNodeMap.get(nodeKey));
    }

    @Override
    public ShortPathTree<T,W> getShortPathTree(T startNode, Class<W> wClass) {
        SumFunction<W> sumFunction = SumFunctionEnum.getByClassType(wClass);
        return getShortPathTree(startNode,wClass, sumFunction);
    }

    @Override
    public ShortPathTree<T,W> getShortPathTree(T startNode, Class<W> wClass, SumFunction<W> sumFunction) {
        // 基于单源最短路径算法Dijkstra

        //已经找到到原点的最短路径的节点
        boolean[] marked =  new boolean[nodeSize()];

        // distanceFromSource[i] 记录从原点到节点i的最短距离
        W[] distanceFromSource = (W[]) Array.newInstance(wClass, nodeSize());

        // from[i] 记录哪条边到达节点i
        Edge<T,W>[] fromEdge = new Edge[edgeSize];

        // 记录未访问过的从原点到达KEY这个节点的最短距离VALUE
        IndexHeap<Integer, W> indexMinHeap = new IndexHeap<>(true);

        // 从原点开始处理
        Integer startNodeKey = nodeKeyMap.get(startNode);
        marked[startNodeKey] = true;
        indexMinHeap.add(startNodeKey, distanceFromSource[startNodeKey]);

        while(!indexMinHeap.isEmpty()){
            // 从堆中找出未访问过的路径最小的顶点
            Entry<Integer, W> minIndex = indexMinHeap.remove();
            //标记未为已找到最短路径的顶点
            Integer nodeKey = minIndex.getKey();
            marked[nodeKey] = true;

            // 获取该节点的邻边
            List<Edge<T,W>> adjacentEdges = getAdjacentEdges(nodeKey);
            for (Edge<T,W> edge : adjacentEdges) {
                int otherNodeKey = edge.getOther(nodeKey);
                if (marked[otherNodeKey]){
                    // 已加入最短路径树的顶点集合不做处理
                    continue;
                }
                /*
                            其他路径
                         /     \
                     nodeKey --- otherNodeKey
                 */
                // 如果从 nodeKey 到达 otherNodeKey 比 nodeKey通过其他路径直接到达otherNodeKey还小， 则更新最短路径
                //  如果到达otherNodeKey的最短路径不存在,  直接更新最短路径即可

                // 从 nodeKey 到达 otherNodeKey的路径长度
                W basePathLength =  sumFunction.sum(distanceFromSource[nodeKey],edge.getWeight());

                // nodeKey通过其他路径直接到达otherNodeKey 的长度
                W otherPathLength = distanceFromSource[otherNodeKey];

                if (fromEdge[otherNodeKey] == null || basePathLength.compareTo(otherPathLength) < 0){
                    // 更新最短路径
                    distanceFromSource[otherNodeKey] = basePathLength;
                    fromEdge[otherNodeKey] = edge;

                    // 更新堆中原点到达otherNodeKey的最短距离
                    indexMinHeap.addOrReplace(otherNodeKey,distanceFromSource[otherNodeKey]);
                }
            }
        }

        // 根据 distTo 和 from 还原最短路径树
        Map<T,W> distanceMap = new HashMap<>();
        for (int i = 0; i < distanceFromSource.length; i++) {
            if(distanceFromSource[i] != null){
                T node = keyNodeMap.get(i);
                distanceMap.put(node,distanceFromSource[i]);
            }
        }

        Map<T, List<Edge<T,W>>> endEdgePathMap = new HashMap<>(nodeSize());
        Map<T, List<T>> endNodePathMap = new HashMap<>(nodeSize());
        for (int i = 0; i < distanceFromSource.length; i++) {
            if(distanceFromSource[i] != null){
                Entry<List<Edge<T,W>>, List<T>> tmpEntry = getShortPathToEnd(i, fromEdge, startNodeKey);
                endEdgePathMap.put(keyNodeMap.get(i), tmpEntry.getKey());
                endNodePathMap.put(keyNodeMap.get(i), tmpEntry.getValue());
            }
        }

        // 收集结果
        ShortPathTree<T,W> shortPathTree = new ShortPathTree<>();
        shortPathTree.setStartNode(startNode);
        shortPathTree.setFormSourceToEndPathEdgeMap(endEdgePathMap);
        shortPathTree.setFormSourceToEndPathNodeMap(endNodePathMap);
        shortPathTree.setDistanceMap(distanceMap);
        return shortPathTree;
    }

    /**
     * 返回从 源点startNodeKey 到节点end 的最短路径经过的所有边
     * @param end               目的节点
     * @param fromEdge
     * @param startNodeKey      开始节点
     * @return                  Entry<经过的所有边, 经过的所有节点>
     */
    public Entry<List<Edge<T,W>>,List<T>> getShortPathToEnd(Integer end, Edge<T,W>[] fromEdge, Integer startNodeKey){
        Deque<Edge<T,W>> edgeStack = new ArrayDeque<>();
        Deque<T> nodeStack = new ArrayDeque<>();
        nodeStack.push(keyNodeMap.get(end));

        // 获取前一条边
        Edge<T,W> preEdge = fromEdge[end];

        // 根据fromEdge 不断往前找经过的边 和 节点
        while (preEdge.getOther(end) != startNodeKey){
            end = preEdge.getOther(end);
            edgeStack.push(preEdge);
            nodeStack.push(keyNodeMap.get(end));
            preEdge = fromEdge[end];
        }

        edgeStack.push(preEdge);
        nodeStack.push(keyNodeMap.get(startNodeKey));

        List<Edge<T,W>> allEdgeList = new ArrayList<>();
        while (!edgeStack.isEmpty()){
            allEdgeList.add(edgeStack.pop());
        }

        List<T> allNodeList = new ArrayList<>();
        while (!nodeStack.isEmpty()){
            allNodeList.add(nodeStack.pop());
        }
        return new Entry<>(allEdgeList,allNodeList);
    }




}
