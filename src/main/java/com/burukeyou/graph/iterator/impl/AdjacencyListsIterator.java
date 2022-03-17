package com.burukeyou.graph.iterator.impl;

import com.burukeyou.graph.AbstractCommonGraph;
import com.burukeyou.graph.impl.AdjacencyListsGraph;
import com.burukeyou.graph.iterator.DefaultIterator;

import java.util.LinkedList;
import java.util.Map;


/**
 * 领接表-无权图-迭代器
 * @author burukeyou
 * @param <T>
 */
public class AdjacencyListsIterator<T> extends DefaultIterator<T> {

    public AdjacencyListsIterator(T curNode, AbstractCommonGraph<T> graph) {
        super(curNode, graph);
    }

    public AdjacencyListsIterator(AbstractCommonGraph<T> graph) {
        super(graph);
    }

    @Override
    public boolean hasNext() {
        AdjacencyListsGraph<T> graph = (AdjacencyListsGraph<T>) super.graph;
        if (!graph.getGraphData().containsKey(curNodeKey)) {
            return false;
        }

        return iteratorType ? curNodeKey < graph.nodeSize() && index < graph.nodeSize() : index < graph.getGraphData().get(curNodeKey).size();
    }

    @Override
    public T next() {
        AdjacencyListsGraph<T> graph = (AdjacencyListsGraph<T>) super.graph;
        Map<Integer, LinkedList<Integer>> graphData = graph.getGraphData();
        if (iteratorType) {
            while (curNodeKey < graph.nodeSize()) {
                if (!visited[curNodeKey] && graph.hasAdjacentEdge(curNodeKey)) {
                    visited[curNodeKey] = true;
                    return graph.keyNodeMap.get(curNodeKey);
                }

                index += 1;
                if (index >= graphData.get(curNodeKey).size()) {
                    curNodeKey++;
                    while (curNodeKey < graph.nodeSize() && !graphData.containsKey(curNodeKey)) {
                        curNodeKey++;
                    }
                    index = 0;
                }
                if (curNodeKey < graph.nodeSize()) {
                    Integer nodeKey = graphData.get(curNodeKey).get(index);
                    if (!visited[nodeKey]) {
                        visited[nodeKey] = true;
                        return graph.keyNodeMap.get(nodeKey);
                    }
                }
            }
        } else {
            LinkedList<Integer> tmp = graphData.get(curNodeKey);
            if (tmp != null && !tmp.isEmpty()) {
                index++;
                if (index < tmp.size()) {
                    Integer nodeKey = graphData.get(curNodeKey).get(index);
                    return graph.keyNodeMap.get(nodeKey);
                }
            }
        }
        return null;
    }
}
