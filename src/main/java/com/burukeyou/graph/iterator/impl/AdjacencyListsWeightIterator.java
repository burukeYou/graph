package com.burukeyou.graph.iterator.impl;

import com.burukeyou.graph.AbstractCommonGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.impl.AdjacencyListsWeightGraph;
import com.burukeyou.graph.iterator.DefaultIterator;

import java.util.LinkedList;
import java.util.Map;

/**
 * 领接表-有权图-迭代器
 * @author burukeyou
 * @param <T>
 */
public class AdjacencyListsWeightIterator<T>  extends DefaultIterator<T> {

    public AdjacencyListsWeightIterator(T curNode, AbstractCommonGraph<T> graph) {
        super(curNode, graph);
    }

    public AdjacencyListsWeightIterator(AbstractCommonGraph<T> graph) {
        super(graph);
    }

    @Override
    public boolean hasNext() {
        AdjacencyListsWeightGraph<T,?> graph = (AdjacencyListsWeightGraph<T,?>) super.graph;
        Map<Integer, LinkedList<Edge<T,?>>> graphData = (Map) graph.getGraphData();
        if (!graphData.containsKey(curNodeKey)) {
            return false;
        }

        return iteratorType ? curNodeKey < graph.nodeSize() && index < graph.nodeSize() : index < graphData.get(curNodeKey).size();
    }

    @Override
    public T next() {
        AdjacencyListsWeightGraph<T, ?> graph = (AdjacencyListsWeightGraph<T, ?>) super.graph;
        Map<Integer, LinkedList<Edge<T,?>>> graphData = (Map) graph.getGraphData();
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
                    Edge<T,?> edge = graphData.get(curNodeKey).get(index);
                    int otherNodeKey = edge.getOther(curNodeKey);
                    if (!visited[otherNodeKey]) {
                        visited[otherNodeKey] = true;
                        return graph.keyNodeMap.get(otherNodeKey);
                    }
                }
            }
        } else {
            LinkedList<Edge<T,?>> tmp = graphData.get(curNodeKey);
            if (tmp != null && !tmp.isEmpty()) {
                index++;
                if (index < tmp.size()) {
                    Edge<T,?> edge = graphData.get(curNodeKey).get(index);
                    int otherNodeKey = edge.getOther(curNodeKey);
                    return graph.keyNodeMap.get(otherNodeKey);
                }
            }
        }
        return null;
    }
}
