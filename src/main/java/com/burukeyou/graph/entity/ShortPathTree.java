package com.burukeyou.graph.entity;

import java.util.List;
import java.util.Map;

/**
 *  最短路径树
 * @author burukeyou
 * @param <T>   节点类型
 * @param <W>   边上权值类型
 */
public class ShortPathTree<T,W extends Comparable<W>> {
    /**
     * 源点
     */
    private T startNode;

    /**
     *  原点到其他节点经过的所有边（边路径）
     */
    private Map<T, List<Edge<T,W>>> formSourceToEndPathEdgeMap;

    /**
     *  原点到其他节点经过的所有节点 （节点路径）
     */
    private Map<T, List<T>> formSourceToEndPathNodeMap;

    /**
     *   所有从源点到其他节点T的路径距离
     */
    private Map<T,W> distanceMap;


    public ShortPathTree() {
    }

    public T getStartNode() {
        return startNode;
    }

    public Map<T, List<Edge<T,W>>> getFormSourceToEndPathEdgeMap() {
        return formSourceToEndPathEdgeMap;
    }

    public Map<T, List<T>> getFormSourceToEndPathNodeMap() {
        return formSourceToEndPathNodeMap;
    }


    public Map<T, W> getDistanceMap() {
        return distanceMap;
    }

    public void setStartNode(T startNode) {
        this.startNode = startNode;
    }

    public void setFormSourceToEndPathEdgeMap(Map<T, List<Edge<T,W>>> formSourceToEndPathEdgeMap) {
        this.formSourceToEndPathEdgeMap = formSourceToEndPathEdgeMap;
    }

    public void setFormSourceToEndPathNodeMap(Map<T, List<T>> formSourceToEndPathNodeMap) {
        this.formSourceToEndPathNodeMap = formSourceToEndPathNodeMap;
    }


    public void setDistanceMap(Map<T, W> distanceMap) {
        this.distanceMap = distanceMap;
    }
}
