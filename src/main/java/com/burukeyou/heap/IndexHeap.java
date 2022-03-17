package com.burukeyou.heap;

import java.util.*;

/** 索引堆
 * @author burukeyou
 * @param <KEY>     索引
 * @param <VALUE>   索引对应的值
 *
 */
public class IndexHeap<KEY, VALUE extends Comparable<VALUE>>  {
    /*
            0(KEY)
         1     2
       3  4   5   6

     */
    // 实际的索引树    keyTree[下标i] = 索引
    private ArrayList<KEY> keyTree;
    
    // Map<索引，下标>
    private Map<KEY,Integer> keyIndexMap;

    // Map<索引，元素>
    private Map<KEY, VALUE>  keyDataMap;
    
    // 是否最小堆
    private boolean isMinHeap;

    // 堆的最大容量(不指定默认是无界堆)
    private Integer maxSize;

    public IndexHeap(boolean isMinHeap) {
        this.isMinHeap = isMinHeap;
        this.keyTree = new ArrayList<>();
        this.keyIndexMap = new HashMap<>();
        this.keyDataMap =new HashMap<>();
    }

    public IndexHeap(boolean isMinHeap, Integer maxSize) {
        this(isMinHeap);
        this.maxSize = maxSize;
    }

    public int size() {
        return keyTree.size();
    }

    public boolean isEmpty() {
        return keyTree.isEmpty();
    }

    public VALUE get(KEY key) {
        return keyDataMap.get(key);
    }

    public boolean containsKey(KEY key) {
        return keyDataMap.containsKey(key);
    }

    /**
     * 替换索引的值
     * @param key
     * @param e
     */
    public void replace(KEY key, VALUE e) {
        if (!keyDataMap.containsKey(key)){
            return;
        }
        keyDataMap.put(key, e);
        // 获取索引对应的节点
        int j = keyIndexMap.get(key);
        // 从节点j开始修复堆
        moveUp(j);
        moveDown(j);
    }

    /**
     * 查看堆顶元素
     */
    public Entry<KEY,VALUE> peek() {
        KEY key = keyTree.get(0);
        VALUE value = keyDataMap.get(key);
        return new Entry<>(key,value);
    }

    /**
     * 堆顶元素出堆
     */
    public Entry<KEY,VALUE> remove() {
        if (isEmpty()){
            return null;
        }

        Entry<KEY,VALUE> ret = peek();

        //1.把尾节点与堆头交换存放的索引值
        swap(keyTree,0,size()-1);

        // 更新
        keyIndexMap.put(keyTree.get(0),0);
        KEY lastKey = keyTree.get(size() - 1);
        keyIndexMap.remove(lastKey);
        keyDataMap.remove(lastKey);
        keyTree.remove(size() - 1);

        // 从堆顶节点开始下浮修复
        moveDown(0);
        return ret;
    }

    /**
     * 入堆 （索引KEY不存在时）
     * @param key       索引
     * @param e         实际值
     */
    public void add(KEY key, VALUE e) {
        if (!keyDataMap.containsKey(key)) {
            keyTree.add(key);
            keyDataMap.put(key, e);
            keyIndexMap.put(key, size() - 1);
            moveUp(size() - 1);
        }
    }

    /**
     * 入堆 （索引KEY存在时进行替换）
     * @param key       索引
     * @param e         实际值
     */
    public void addOrReplace(KEY key, VALUE e){
        if (!keyDataMap.containsKey(key)){
            add(key,e);
        }else{
            replace(key,e);
        }
    }

    /**
     *  从i开始向上浮动修复堆
     * @param i        索引树的节点下标
     */
    private void moveUp(int i) {
        while (i > 0){
            VALUE iValue = getValueByIndex(i);
            VALUE parentValue = getValueByIndex(parent(i));

            // 最小堆
            if (isMinHeap && iValue.compareTo(parentValue) > 0){
                break;
            }

            // 最大堆
            if (!isMinHeap && iValue.compareTo(parentValue) < 0){
                break;
            }

            int patentIndex = parent(i);
            swapAndRestKeyIndexMap(keyTree,i,patentIndex);
            i = parent(i);
        }
    }

    public VALUE getValueByIndex(int index){
        return keyDataMap.get(keyTree.get(index));
    }

    /**
     *  从i开始向下浮动修复堆
     * @param i          索引树的节点下标
     */
    private void moveDown(int i) {
        //是否有孩子节点，因为是完全二叉树，只判断左孩子即可
        while(leftChildren(i) < size()) {
            //tempMin保存较小孩子节点下标，初始化为左孩子节点下标
            int tempMin = leftChildren(i);

            //右节点存在并且比左孩子节点小
            int rightIndex = tempMin + 1;
            if (rightIndex < size()){
                if (isMinHeap && getValueByIndex(tempMin).compareTo(getValueByIndex(rightIndex)) > 0){
                    tempMin = rightIndex;
                }
                if (!isMinHeap && getValueByIndex(tempMin).compareTo(getValueByIndex(rightIndex)) < 0){
                    tempMin = rightIndex;
                }
            }

            //父节点比孩子节点较小者小表示，则不必交换，堆结构调整完毕。
            if (isMinHeap && getValueByIndex(i).compareTo(getValueByIndex(tempMin)) <= 0 ){
                break;
            }

            if (!isMinHeap && getValueByIndex(i).compareTo(getValueByIndex(tempMin)) >= 0 ){
                break;
            }

            //
            swapAndRestKeyIndexMap(keyTree,i,tempMin);

            //更新父节点
            i = tempMin;
        }
    }

    // 获取父节点
    private int parent(int index){
        return (index-1)/2;
    }

    // 获取左子节点
    private int leftChildren(int index){
        return index * 2 + 1;
    }

    //  获取➡又子节点
    private int rightChildren(int index){
        return index * 2 + 2;
    }


    // 交换索引堆中的索引i和j
    private void swap(ArrayList<KEY> arr, int i, int j) {
        KEY tmp = arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,tmp);
    }

    // 交换索引堆中的索引i和j 并更新keyIndexMap的关系
    private void swapAndRestKeyIndexMap(ArrayList<KEY> arr, int i, int j) {
        KEY tmp = arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,tmp);
        keyIndexMap.put(keyTree.get(i),i);
        keyIndexMap.put(keyTree.get(j),j);
    }

    /**
     * TopN计算封装
     * @param key
     * @param e
     */
    public void addForTopN(KEY key, VALUE e){
        if (maxSize == null || size() < maxSize){
            // 没指定阈值， 或者没达到阈值正常添加
            add(key,e);
            return;
        }

        // 指定了堆大小 并且 堆元素大小达到最大值需要，需要出堆一个
        Entry<KEY, VALUE> topEntry = peek();
        if (isMinHeap && e.compareTo(topEntry.getValue()) > 0){
            // 找到更大的数，添加到堆
            this.remove();
            this.add(key,e);
        }else if (!isMinHeap &&  e.compareTo(topEntry.getValue()) < 0){
            // 找到更小的数，添加到堆
            this.remove();
            this.add(key,e);
        }
    }

    /**
     *  查看堆情况
     */
    public void output(){
        List<Entry<KEY,VALUE>> ret = new ArrayList<>();
        for (KEY key : keyTree) {
            VALUE value = keyDataMap.get(key);
            ret.add(new Entry<>(key,value));
        }
        ret.sort(Comparator.comparing(Entry::getValue));
        ret.forEach(System.out::println);
    }
}
