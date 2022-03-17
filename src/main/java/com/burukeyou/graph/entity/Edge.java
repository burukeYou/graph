package com.burukeyou.graph.entity;

/**
 * 边
 * @author burukeyou
 * @param <W>
 */
public class Edge<T, W extends Comparable<W>> implements Comparable<Edge<T,W>>{

    //边的一端节点
    private int keyA;
    private T a;

    //边的另一端节点
    private int keyB;
    private T b;

    //边上权值
    private W weight;

    public Edge() { }

    public Edge(int a, int b, W weight) {
        this.keyA = a;
        this.keyB = b;
        this.weight = weight;
    }

    public Edge(int aKey, T a, int bKey, T b, W weight) {
        this.keyA = aKey;
        this.a = a;
        this.keyB = bKey;
        this.b = b;
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        int result = keyA;
        result = 31 * result + keyB;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }


    public int getKeyA() {
        return keyA;
    }


    public int getKeyB() {
        return keyB;
    }


    public W getWeight() {
        return weight;
    }

    public void setWeight(W weight) {
        this.weight = weight;
    }

    //获得顶点x的对端顶点
    public int getOther(int x){
        assert (x == keyA || x == keyB);
        return x == keyA ? keyB : keyA;
    }

    public T getOther(T x){
        assert (x.equals(a) || x.equals(b));
        return x.equals(a) ? b : a;
    }

    @Override
    public String toString() {
        return a +"-"+ b +"("+weight+")";
    }

    @Override
    public int compareTo(Edge<T, W> other) {
        //因为参数other的Edge没指定泛型，所以other.getWeight()的类型默认为Object
        return weight.compareTo(other.getWeight());
    }

    public T getA() {
        return a;
    }

    public T getB() {
        return b;
    }

    public void setKeyA(int keyA) {
        this.keyA = keyA;
    }

    public void setA(T a) {
        this.a = a;
    }

    public void setKeyB(int keyB) {
        this.keyB = keyB;
    }

    public void setB(T b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?, ?> edge = (Edge<?, ?>) o;

        if (keyA != edge.keyA && keyA != edge.keyB) return false;
        if (keyB != edge.keyB && keyB != edge.keyA) return false;

        if (!a.equals(edge.a) && !a.equals(edge.b)) return false;
        if (!b.equals(edge.b) && !b.equals(edge.a)) return false;

        return weight.equals(edge.weight);
    }
}




