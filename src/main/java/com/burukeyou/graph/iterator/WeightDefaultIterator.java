package com.burukeyou.graph.iterator;

import com.burukeyou.graph.AbstractCommonGraph;
import com.burukeyou.graph.entity.Edge;

/**
 * 默认迭代器
 * @author burukeyou
 */

public abstract class WeightDefaultIterator<T,W extends Comparable<W>> implements WeightIterator<T,W> {

    //迭代器当前指针
    protected int index;

    // 当前指向的节点的key
    protected T curNode;
    protected Integer curNodeKey;
    protected final AbstractCommonGraph<T> graph;
    protected boolean[] visited;

    // true-所有节点迭代器   false-邻边节点迭代器
    protected final boolean iteratorType;

    public WeightDefaultIterator(T curNode, AbstractCommonGraph<T> graph) {
        if(!graph.nodeSet.contains(curNode)){
            throw new RuntimeException("The graph node " + curNode + " not found");
        }
        this.curNode = curNode;
        iteratorType = false;
        this.graph = graph;
        init();
    }

    private void init(){
        this.curNodeKey = iteratorType ? 0 : graph.nodeKeyMap.get(curNode);
        this.visited = new boolean[graph.nodeSize()];
    }

    public WeightDefaultIterator(AbstractCommonGraph<T> graph) {
        this.graph = graph;
        iteratorType = true;
        init();
    }

    @Override
    public Edge<T,W> begin() {
        //当有新的节点调用时重置指向,重塑状态
        index = -1;
        init();
        return next();
    }

    @Override
    public boolean hasNext() {
        return  iteratorType ? curNodeKey < graph.nodeSize() &&  index < graph.nodeSize(): index < graph.nodeSize();
    }

    @Override
    public abstract Edge<T,W> next();

}
