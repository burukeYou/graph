package com.burukeyou.graph.iterator.impl;

import com.burukeyou.graph.AbstractCommonGraph;
import com.burukeyou.graph.entity.Edge;
import com.burukeyou.graph.impl.AdjacencyMatrixWeightGraph;
import com.burukeyou.graph.iterator.DefaultIterator;

/**
 * 邻接矩阵-有权图-迭代器
 *
 * @author burukeyou
 * @param <T>
 */
public class AdjacencyMatrixWeightIterator<T> extends DefaultIterator<T> {

    public AdjacencyMatrixWeightIterator(T curNode, AbstractCommonGraph<T> graph) {
        super(curNode, graph);
    }

    public AdjacencyMatrixWeightIterator(AbstractCommonGraph<T> graph) {
        super(graph);
    }

    @Override
    public T next() {
        AdjacencyMatrixWeightGraph<T, ?> graph = (AdjacencyMatrixWeightGraph<T, ?>) super.graph;
        Edge<T,?>[][] graphData = graph.getGraphData();
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
                if (curNodeKey < graph.nodeSize() && graphData[curNodeKey][index] != null) {
                    if (!visited[index]) {
                        visited[index] = true;
                        return graph.keyNodeMap.get(index);
                    }
                }
            }
        } else {
            for (index += 1; index < graph.nodeSize(); index++) {
                if (graphData[curNodeKey][index] != null) {
                    return graph.keyNodeMap.get(index);
                }
            }
        }
        return null;
    }
}
