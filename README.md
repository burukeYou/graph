
# 1、graph
java对图数据结构的实现 (仅供学习参考)

#  2、architecture
实现了4种图的实现， 分别是
- `领接矩阵-无权图`：  AdjacencyMatrixGraph
- `领接矩阵-有权图`： AdjacencyMatrixWeightGraph
- `领接表-无权图`：   AdjacencyListsGraph
- `领接表-有权图`： 	AdjacencyListsWeightGraph

类图关系如下
![在这里插入图片描述](https://img-blog.csdnimg.cn/1d73847a4c6d4270a2d2c4603cd2bc05.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA56yo54yq5aSn6Zq-5Li05aS0,size_20,color_FFFFFF,t_70,g_se,x_16)


# 3、Function
并且在这四种数据结构之上实现了以下基础图论算法:
- 图的深度优先遍历
- 图的广度优先遍历
- 计算图连通分量个数
- 最小生成树算法
    - Prim算法
    - Kruskal算法
- 路径算法
    - 有权图单源最短路径算法Dijkstra
    - 无权图单源最短路径算法
    -  寻找从 开始节点 到 结束节点的一条路径
    - 寻找从 开始节点 到 结束节点的所有路径
    



# 4、Quick Start
## 4.1、创建图

```java

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
    // 创建有向无权-领结表图
    static Graph<String> noWeightDirectListedGraph = new AdjacencyListsGraph<>(true);
    // 创建无向无权-邻接矩阵图
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
    
	// 创建无向有权-领接矩阵图
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
```

## 4.2、图遍历

```java
    @Test
    public void testTraverse(){
        // 深度遍历 dfs
        noWeightDirectListedGraph.dfsTraverse(e -> System.out.print(e + "->"));
        System.out.println("\n-------");
        // 广度遍历 bfs
        noWeightMatrixGraph.bfsTraverse(e -> System.out.print(e + "->"));
    }
```



## 4.3、计算联通分量

```java
    int count = noWeightDirectListedGraph.componentCount();
    int count1 = noWeightMatrixGraph.componentCount();
    System.out.println(count);
    System.out.println(count1);
```

## 4.4、最小生成树算法
```java
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
```


## 4.5、无权图求单源最短路径算法

```java
        // 找到所有从开始节点 到 其他节点的最短路径
        String startNoe = "x0";
        Map<String, List<String>> tmp = noWeightDirectListedGraph.findShortPathFromStartNode(startNoe);
        for (Map.Entry<String, List<String>> entry : tmp.entrySet()) {
            String path = entry.getValue().stream().collect(Collectors.joining("->"));
            System.out.println("从" + startNoe + "到节点" + entry.getKey() + "的最短路径为： " + path);
        }
```


##  4.6、路径算法

```java
  System.out.println("=================2、找到所有从开始节点 到 结束节点的一条路径 ====");
        //  寻找从 x0 到x1 的一条路径。
        List<String> onePath = noWeightDirectListedGraph.findOnePath("x0", "x1");
        System.out.println("寻找从x0到x1 的一条路径为: " + onePath.stream().collect(Collectors.joining("->")));


        System.out.println("=================3、找到所有从开始节点 到 结束节点的所有路径 ====");

        List<List<String>> allPath = noWeightDirectListedGraph.findAllPath("x0", "x1");
        for (List<String> list : allPath) {
            System.out.println("从x0 到x1的路径为: " + list.stream().collect(Collectors.joining("->")));
        }
```


## 4.7、有权图单源最短路径算法Dijkstra

```java
    @Test
    public void testShortPathTree(){
        ShortPathTree<String, Integer> tmp = weightListedGraph.getShortPathTree("a0", Integer.class);
        for (Map.Entry<String, List<Edge<String, Integer>>> entry : tmp.getFormSourceToEndPathEdgeMap().entrySet()) {
            String path = entry.getValue().stream().map(Edge::toString).collect(Collectors.joining("->"));
            System.out.println("从a0到节点" + entry.getKey() + "的最短路径为： " + path);
        }

        System.out.println();
    }
```

