package com.burukeyou.graph;

import java.util.*;
import java.util.function.Consumer;
import com.burukeyou.graph.iterator.Iterator;

/**
 * 图通用实现
 * @author burukeyou
 * @param <T>
 */

public abstract class AbstractCommonGraph<T> {

    //非真正节点数量， 只是表示初始化的矩阵空间大小
    public int initNodeSize;

    // 图边的数量
    public int edgeSize;

    // 是否是有向图
    public boolean isDirectedGraph;

    // 当前分配的节点key
    protected int nodeKeyIndex = -1;

    // 存放图中的所有节点
    public final Set<T> nodeSet = new HashSet<>();

    // 存放图中的所有节点Key
    public final List<Integer> nodeKeyList = new ArrayList<>();

    // 节点 和 节点Key 的映射关系
    public final Map<Integer,T> keyNodeMap = new HashMap<>();
    public final Map<T,Integer> nodeKeyMap = new HashMap<>();

    protected AbstractCommonGraph() {
    }

    protected AbstractCommonGraph(int initNodeSize, boolean isDirectedGraph) {
        this.initNodeSize = initNodeSize;
        this.isDirectedGraph = isDirectedGraph;
    }

    protected AbstractCommonGraph(boolean isDirectedGraph) {
        this.isDirectedGraph = isDirectedGraph;
    }

   
    public int nodeSize() {
        // 返回图当前存在的节点数量
        return nodeSet.size();
    }

    public int edgeSize() {
        return edgeSize;
    }

    // 为节点分配key
    protected Integer distributeNodeKey(T node){
        if (!nodeSet.contains(node)){
            nodeSet.add(node);
            nodeKeyIndex++;
            nodeKeyList.add(nodeKeyIndex);
            keyNodeMap.put(nodeKeyIndex,node);
            nodeKeyMap.put(node,nodeKeyIndex);
        }
        return nodeKeyMap.get(node);
    }

    protected abstract boolean hasEdge(Integer startKey, Integer endKey);


    /**
     * 迭代器相关
     * @param curNodeKey
     * @return
     */
    protected Iterator<T> iterator(Integer curNodeKey) {
        return iterator(keyNodeMap.get(curNodeKey));
    }

    public abstract Iterator<T> iterator(T curNode);


    public List<T> getAdjacentNodes(T curNode) {
        return getAdjacentNodes(nodeKeyMap.get(curNode));
    }

    // 获取节点的所有相邻节点
    protected List<T> getAdjacentNodes(Integer curNodeKey) {
        Iterator<T> iterator = iterator(keyNodeMap.get(curNodeKey));
        List<T> tmpList = new ArrayList<>();
        for (T i = iterator.begin();  iterator.hasNext();  i = iterator.next()) {
            tmpList.add(i);
        }
        return tmpList;
    }


    /**
     *  深度遍历相关
     */
   
    public void dfsTraverse() {
        dfsTraverse(System.out::println);
    }

   
    public void dfsTraverse(Consumer<T> consumer){
        boolean[] visited = new boolean[nodeSet.size()];
        for (Integer key : nodeKeyList) {
            if (!visited[key]){
                dfsTraverse(key,visited,consumer);
            }
        }
    }

   
    public void dfsTraverse(T startNode,Consumer<T> consumer){
        dfsTraverse(nodeKeyMap.get(startNode),new boolean[nodeSet.size()], consumer);
    }

    // 对图的v节点做深度优先遍历
    protected void dfsTraverse(int v, boolean[] visited) {
        dfsTraverse(v,visited, (e) -> {});
    }

    protected void dfsTraverse(int cur, boolean[] visited, Consumer<T> consumer){
        //遍历
        visited[cur] = true;
        T node = keyNodeMap.get(cur);
        if (consumer != null){
            consumer.accept(node);
        }

        //递归优先遍历v的有边相邻节点
        List<T> adjacentNodes = getAdjacentNodes(cur);
        for (T otherNode : adjacentNodes) {
            Integer nodeKey = nodeKeyMap.get(otherNode);
            if (!visited[nodeKey]){
                dfsTraverse(nodeKey,visited,consumer);
            }
        }
    }

    // 节点是否有相邻节点
    protected abstract boolean hasAdjacentEdge(Integer nodeKey);

    /**
     * 广度遍历相关
     */
   
    public void bfsTraverse(Consumer<T> consumer) {
        boolean[] visited = new boolean[nodeSet.size()];
        for (Integer key : nodeKeyList) {
            if (!visited[key]){
                bfsTraverse(key,consumer,visited);
            }
        }
    }

    public void bfsTraverse(Integer startKey, Consumer<T> consumer, boolean[] visited){
        // 层序遍历
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(startKey);
        visited[startKey] = true;
        if (consumer != null){
            consumer.accept(keyNodeMap.get(startKey));
        }

        while (!queue.isEmpty()){
            int key = queue.remove();

            // 获取该节点的相邻节点
            List<T> adjacentNodes = getAdjacentNodes(key);
            for (T otherNode : adjacentNodes) {
                Integer subKey = nodeKeyMap.get(otherNode);
                if (!visited[subKey]){
                    // 访问i
                    visited[subKey] = true;
                    queue.add(subKey);
                    if (consumer != null){
                        consumer.accept(otherNode);
                    }
                }
            }
        }
    }


    /**
     * 算法相关
     */

    public int componentCount() {
        //计算连通分量， 能深搜几次就有几个联通分量
        boolean[] visited = new boolean[nodeSet.size()];
        int count = 0;

        for (Integer key : nodeKeyList) {
            // 节点存在 并且未被访问过才去 处理
            if (!visited[key]){
                dfsTraverse(key,visited);
                count++;
            }
        }
        return count;
    }


   
    public List<T> findOnePath(T startNode, T endNode) {
        // from[i] = v   表示节点i的前一个节点是v
        int[] from = new int[nodeSize()];

        Integer startKey = nodeKeyMap.get(startNode);
        from[startKey] = -1;

        boolean[] visited = new boolean[nodeSize()];
        findOnePathDfs(startKey,visited,from);

        // 当endNode 被访问过，说明存在路径上
        Integer endKey = nodeKeyMap.get(endNode);
        if (visited[endKey]){
            //从后往前遍历form
            Stack<T> stack = new Stack<>();
            int pre = endKey;
            while (pre != -1){
                stack.push(keyNodeMap.get(pre));
                pre = from[pre];
            }

            //
            if (!stack.isEmpty()){
                // stack 路径是 逆序存放
                ArrayList<T> tmp = new ArrayList<>(stack);
                Collections.reverse(tmp);
                return tmp;
            }
        }
        return null;
    }

    protected void findOnePathDfs(int key, boolean[] visited, int[] from){
        //遍历
        visited[key] = true;
        // 获得邻边迭代器
       // Iterator<T> iterator = iterator(keyNodeMap.get(key));

        List<T> adjacentNodes = getAdjacentNodes(key);
        for (T adjacentNode : adjacentNodes) {
            Integer nodeKey = nodeKeyMap.get(adjacentNode);
            if (!visited[nodeKey]){
                from[nodeKey] = key;
                findOnePathDfs(nodeKey,visited,from);
            }
        }

       /* for (T i = iterator.begin(); iterator.hasNext(); i = iterator.next()) {
            Integer nodeKey = nodeKeyMap.get(i);
            if (!visited[nodeKey]){
                from[nodeKeyMap.get(i)] = key;
                findOnePathDfs(nodeKey,visited,from);
            }
        }*/
    }


    public Map<T,List<T>> findShortPathFromStartNode(T startNode) {
        // （无权图）通过 bfs 找到的路径一定是最短的路径
        Integer startKey = nodeKeyMap.get(startNode);
        boolean[] visited = new boolean[nodeSize()];

        // from[i] = x   表示节点i的前一个路径节点
        int[] from = new int[edgeSize];
        Arrays.fill(from, -1);

        // 表示从起点到节点i的最短路径距离
        // 通过bfs可找到从s到v的最短路径， 但是如果多条最短只会是其中一条
        int[] shortestDistanceFromStart = new int[edgeSize];
        Arrays.fill(shortestDistanceFromStart, -1);

        // 层序遍历
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(startKey);
        visited[startKey] = true;
        shortestDistanceFromStart[startKey] = 0;

        while (!queue.isEmpty()){
            int key = queue.remove();

            // 获取该节点的相邻节点
            Iterator<T> iterator = iterator(key);
            for (T curNode = iterator.begin();  iterator.hasNext();  curNode = iterator.next()) {
                Integer subKey = nodeKeyMap.get(curNode);
                if (!visited[subKey]){
                    // 访问i
                    visited[subKey] = true;
                    shortestDistanceFromStart[subKey] = shortestDistanceFromStart[key] + 1;
                    from[subKey] = key;
                    queue.add(subKey);
                }
            }
        }

        // 通过 from 反推得到路径
        Map<T,List<T>> pathList = new HashMap<>();
        for (int key = 0; key < from.length; key++) {
            Stack<T> stack = new Stack<>();
            int pre = key;
            while (pre != -1){
                stack.push(keyNodeMap.get(pre));
                pre = from[pre];
            }

            //
            if (!stack.isEmpty() && stack.size() > 1){
                // stack 路径是 逆序存放
                ArrayList<T> tmp = new ArrayList<>(stack);
                Collections.reverse(tmp);
                pathList.put(keyNodeMap.get(key),tmp);
            }
        }
        return pathList;
    }

    public List<List<T>> findAllPath(T startNode, T endNode) {
        if (!nodeSet.contains(startNode) || !nodeSet.contains(endNode)){
            return new ArrayList<>();
        }

        if (startNode.equals(endNode)){
            return new ArrayList<>();
        }

        // 收集所有路径
        List<List<T>> pathList = new ArrayList<>();

        // 标记节点是否访问过
        boolean[] visited = new boolean[nodeSize()];
        // 存放当前访问的路径
        Stack<T> stackPath = new Stack<>();

        // 先访问start节点
        Integer startKey = nodeKeyMap.get(startNode);
        Integer endKey = nodeKeyMap.get(endNode);

        visited[startKey] = true;
        stackPath.push(startNode);

        //
        dfsFindAllPath(startKey,endKey,pathList,stackPath,visited);
        return pathList;
    }

    private void dfsFindAllPath(Integer start, Integer end, List<List<T>> pathList,Stack<T> stackPath,boolean[] visited){
        if (start.equals(end)){
            // 找到路径
            pathList.add(new ArrayList<>(stackPath));
            return;
        }

        // 对start节点的所有相邻节点进行深搜
        List<T> adjacentNodes = getAdjacentNodes(start);
        for (T otherNode : adjacentNodes) {
            Integer otherNodeKey = nodeKeyMap.get(otherNode);
            if (!visited[otherNodeKey]){
                // 选择该节点
                visited[otherNodeKey] = true;
                stackPath.push(otherNode);

                // 递归寻找从 i 到 end 所有路径
                dfsFindAllPath(otherNodeKey, end, pathList,stackPath,visited);

                // 回溯状态
                T peek = stackPath.peek();
                Integer key = nodeKeyMap.get(peek);
                visited[key] = false;
                stackPath.pop();
            }
        }
    }

    public boolean hasRing() {
        if (!isDirectedGraph){
            throw new RuntimeException("无向图判断是否有环无意义");
        }
        //todo 法1：  一条深度遍历路线中如果有结点被第二次访问到，那么有环
        //todo 法2   有向图的拓扑排序kahn算法
        return false;
    }
}
