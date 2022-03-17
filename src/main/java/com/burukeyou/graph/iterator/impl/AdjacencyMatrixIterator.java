package com.burukeyou.graph.iterator.impl;

import com.burukeyou.graph.AbstractCommonGraph;
import com.burukeyou.graph.impl.AdjacencyMatrixGraph;
import com.burukeyou.graph.iterator.DefaultIterator;

/**
 * 邻接矩阵-无权图-迭代器
 *
 * @author burukeyou
 * @param <T>
 */
public class AdjacencyMatrixIterator<T> extends DefaultIterator<T> {

    public AdjacencyMatrixIterator(T curNode, AbstractCommonGraph<T> graph) {
        super(curNode, graph);
    }

    public AdjacencyMatrixIterator(AbstractCommonGraph<T> graph) {
        super(graph);
    }

    @Override
    public T next() {
        AdjacencyMatrixGraph<T> graph = (AdjacencyMatrixGraph<T>) super.graph;
        int[][] graphData = graph.getGraphData();
        if (iteratorType) {
            while (curNodeKey < graph.nodeSize()) {
                if (!visited[curNodeKey] && graph.hasAdjacentEdge(curNodeKey)) {
                    visited[curNodeKey] = true;
                    return graph.keyNodeMap.get(curNodeKey);
                }

                index += 1;
                if (index >= graph.nodeSize()) {
                    curNodeKey++;
                    index = 0;
                }
                if (curNodeKey < graph.nodeSize() && graphData[curNodeKey][index] > 0) {
                    if (!visited[index]) {
                        visited[index] = true;
                        return graph.keyNodeMap.get(index);
                    }
                }
            }
        } else {
            for (index += 1; index < graph.nodeSize(); index++) {
                if (graphData[curNodeKey][index] > 0) {
                    return graph.keyNodeMap.get(index);
                }
            }
        }
        return null;
    }
}
