package com.burukeyou.unionfind;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 并查集
 * @author burukeyou
 */
public class UnionFind<T> {

    //parent[i] = x，  表示节点i所指向的父节点为x
    private int[] parent;

    // rank[i]表示以i为根的集合所表示的树的层数。 主要用来在合并集合时作参考，不代表100%的准确性
    private int[] rank;

    private Integer nodeKeyIndex = -1;

    // 当前并查集存在的节点
    private final Set<T> nodeSet = new HashSet<>();

    // Map<节点Key， 实际节点>
    private final Map<Integer,T> keyNodeMap = new HashMap<>();

    // Map<实际节点， 节点Key>
    private final Map<T,Integer> nodeKeyMap = new HashMap<>();

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];

        // 初始化, 每一个parent[i]指向自己, 表示每一个元素自己自成一个集合
        for( int i = 0 ; i < size ; i ++ ){
            parent[i] = i;
            rank[i] = 1;
        }
    }


    public int getSize() {
        return parent.length;
    }

    // 查找cur节点的所属根节点
    private int findRootNode(int cur) {
        if(cur < 0 || cur >= parent.length){
            throw new IllegalArgumentException("cur Index Out Of bounds");
        }

        // 当父指针指向自己的就是根节点
        while (cur != parent[cur]){
            //压缩路径，指向爷爷节点，以此类推
            parent[cur] = parent[parent[cur]];
            // 更新当前迭代的指针
            cur  = parent[cur];
        }

        return cur;
    }

    // 给新加入到并查集的节点 分配唯一Key
    private Integer distributeNodeKey(T node){
        if (!nodeSet.contains(node)){
            nodeSet.add(node);
            nodeKeyIndex++;
            keyNodeMap.put(nodeKeyIndex,node);
            nodeKeyMap.put(node,nodeKeyIndex);
        }
        return nodeKeyMap.get(node);
    }

    //查看元素x和元素y是否所属一个集合
    public boolean isConnected(T x , T y){
        Integer xKey = nodeKeyMap.get(x);
        Integer yKey = nodeKeyMap.get(y);
        if (xKey == null || yKey == null){
            // 任意一个为空，代表没有加入并查集肯定没关系
            return false;
        }

        // 判断根节点是否一样即可
        return findRootNode(xKey) == findRootNode(yKey);
    }

    public void union(T x, T y) {
        Integer xKey = distributeNodeKey(x);
        Integer yKey = distributeNodeKey(y);
        union(xKey,yKey);
    }


    // 合并元素x和元素y所属的集合，
    private void union(int x, int y) {
        int xRoot = findRootNode(x);
        int yRoot = findRootNode(y);

        if(xRoot == yRoot){
            // 已经属于同一个集合不需要再合并
            return;
        }

        // 将rank低的集合合并到rank高的集合上
        if(rank[xRoot] < rank[yRoot]){
            //低深度向高深度树聚合时， 直接低深度树根节点指向 搞深度树的根结点即可
            parent[xRoot] = yRoot;
        }else if (rank[xRoot] > rank[yRoot]){
            // 同上
            parent[yRoot] = xRoot;
        }else {
            //深度一样时，x合并到y即可。 然后 y树层级加1
            parent[xRoot] = yRoot;
            rank[yRoot] += 1;
        }
    }

}
