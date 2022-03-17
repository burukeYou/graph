package com.burukeyou.graph.mst;

import com.burukeyou.graph.entity.MstTree;

/**
 * 生成最小生成树算法策略
 * @author burukeyou
 */

public interface MstTreeStrategy<T,W extends Comparable<W>> {

    /**
     *  获取最小生成树
     */
    MstTree<T, W> getMinimumSpanningTree();
}
