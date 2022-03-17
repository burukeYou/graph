package com.burukeyou;


import com.burukeyou.graph.Graph;
import com.burukeyou.graph.WeightedGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.entity.MstTree;
import com.burukeyou.graph.entity.ShortPathTree;
import com.burukeyou.graph.impl.AdjacencyListsGraph;
import com.burukeyou.graph.impl.AdjacencyMatrixGraph;
import com.burukeyou.graph.impl.AdjacencyMatrixWeightGraph;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试用例
 * @author burukeyou
 */
public class TestAll {

    /** 有以下图
     *             0
     *           /   \
     *          2    3
     *         /    /  \
     *        v    |    5  ->  1
     *        6    v   ^
     *            4   /
     *
     *           8--> 9
     *           12
     */
    // 有向无权-领结表-图
    static Graph<String> noWeightDirectListedGraph = new AdjacencyListsGraph<>(true);
    // 无向无权-邻接矩阵-图
    static Graph<String> noWeightMatrixGraph = new AdjacencyMatrixGraph<>(50,false);

    static {
        noWeightDirectListedGraph.addEdge("x0","x2");
        noWeightDirectListedGraph.addEdge("x2","x6");
        noWeightDirectListedGraph.addEdge("x0","x3");
        noWeightDirectListedGraph.addEdge("x3","x4");
        noWeightDirectListedGraph.addEdge("x3","x5");
        noWeightDirectListedGraph.addEdge("x4","x5");
        noWeightDirectListedGraph.addEdge("x5","x1");
        noWeightDirectListedGraph.addEdge("x8","x9");
        noWeightDirectListedGraph.addEdge("x12","x12");

        noWeightMatrixGraph.addEdge("x0","x2");
        noWeightMatrixGraph.addEdge("x2","x6");
        noWeightMatrixGraph.addEdge("x0","x3");
        noWeightMatrixGraph.addEdge("x3","x4");
        noWeightMatrixGraph.addEdge("x3","x5");
        noWeightMatrixGraph.addEdge("x4","x5");
        noWeightMatrixGraph.addEdge("x8","x9");
        noWeightMatrixGraph.addEdge("x12","x12");

    }

    /**
     *  测试深度遍历、广度遍历
     */
    @Test
    public void testTraverse(){
        // 深度遍历 dfs
        noWeightDirectListedGraph.dfsTraverse(e -> System.out.print(e + "->"));
        System.out.println("\n-------");
        // 广度遍历 bfs
        noWeightMatrixGraph.bfsTraverse(e -> System.out.print(e + "->"));
    }

    /**
     *  测试联通分量
     */
    @Test
    public void testComponent(){
        int count = noWeightDirectListedGraph.componentCount();
        int count1 = noWeightMatrixGraph.componentCount();
        System.out.println(count);
        System.out.println(count1);
    }

    /**
     *  测试路径算法
     */
    @Test
    public void testPath(){
        System.out.println("=================1、无向图单元最短路径算法。 找到所有从开始节点 到 其他节点的最短路径 ====");

        String startNoe = "x0";
        Map<String, List<String>> tmp = noWeightDirectListedGraph.findShortPathFromStartNode(startNoe);
        for (Map.Entry<String, List<String>> entry : tmp.entrySet()) {
            String path = entry.getValue().stream().collect(Collectors.joining("->"));
            System.out.println("从" + startNoe + "到节点" + entry.getKey() + "的最短路径为： " + path);
        }

        System.out.println("=================2、找到所有从开始节点 到 结束节点的一条路径 ====");
        //  寻找从 x0 到x1 的一条路径。
        List<String> onePath = noWeightDirectListedGraph.findOnePath("x0", "x1");
        System.out.println("寻找从x0到x1 的一条路径为: " + onePath.stream().collect(Collectors.joining("->")));


        System.out.println("=================3、找到所有从开始节点 到 结束节点的所有路径 ====");

        List<List<String>> allPath = noWeightDirectListedGraph.findAllPath("x0", "x1");
        for (List<String> list : allPath) {
            System.out.println("从x0 到x1的路径为: " + list.stream().collect(Collectors.joining("->")));
        }
    }


    /**
     *  有权图
     */
    // 无向有权-领结矩阵图
    static WeightedGraph<String, Integer> weightListedGraph = new AdjacencyMatrixWeightGraph<>(20,false);
    /*
         0 - 2
          \
           3 -  4 - 6                                       a2-a0(10)
             \ / \                                          a3-a5(10)
          1-  5   7                                         a3-a4(20)
                                                            a0-a3(30)
                                                            a1-a5(40)
                                                            a6-a4(98)
                                                            a7-a4(100)
     */
    static {
        weightListedGraph.addEdge("a0","a2",10);
        weightListedGraph.addEdge("a0","a3",10);
        weightListedGraph.addEdge("a3","a4",20);
        weightListedGraph.addEdge("a3","a5",10);
        weightListedGraph.addEdge("a5","a1",40);
        weightListedGraph.addEdge("a4","a5",50);
        weightListedGraph.addEdge("a4","a5",60);
        weightListedGraph.addEdge("a4","a5",36);
        weightListedGraph.addEdge("a6","a4",98);
        weightListedGraph.addEdge("a4","a7",100);
    }

    /**
     * 获取无向图的最小生成树 （包含Prim算法、Kruskal算法）
     */
    @Test
    public void testGetMinimumSpanningTree(){
        MstTree<String, Integer> minimumSpanningTree = weightListedGraph.getMinimumSpanningTree();
        // Prim算法
        MstTree<String, Integer> minimumSpanningTreePrim = weightListedGraph.getMinimumSpanningTreePrim();
        // Kruskal算法
        MstTree<String, Integer> minimumSpanningTreeKruskal = weightListedGraph.getMinimumSpanningTreeKruskal();
        List<Edge<String,Integer>> edgeList = minimumSpanningTree.getEdgeList();
        for (Edge<String,Integer> e : edgeList){
            System.out.println(e.toString());
        }
    }

    /**
     *  找到从源点到其他所有节点的最短路径
     */
    @Test
    public void testShortPathTree(){
        ShortPathTree<String, Integer> tmp = weightListedGraph.getShortPathTree("a0", Integer.class);
        for (Map.Entry<String, List<Edge<String, Integer>>> entry : tmp.getFormSourceToEndPathEdgeMap().entrySet()) {
            String path = entry.getValue().stream().map(Edge::toString).collect(Collectors.joining("->"));
            System.out.println("从a0到节点" + entry.getKey() + "的最短路径为： " + path);
        }

        System.out.println();
    }

}
