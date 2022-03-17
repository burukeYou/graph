package com.burukeyou.heap;

/**
 * @author burukeyou
 */
public class Entry<KEY,VALUE> {

    private KEY key;
    private VALUE value;

    public Entry(KEY key, VALUE value) {
        this.key = key;
        this.value = value;
    }

    public KEY getKey() {
        return key;
    }

    public VALUE getValue() {
        return value;
    }

    public void setKey(KEY key) {
        this.key = key;
    }

    public void setValue(VALUE value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "'key':" + key +
                ", 'value':" + value +
                '}';
    }
}
