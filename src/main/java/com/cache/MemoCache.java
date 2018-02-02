package com.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MemoCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private Map<K, V> storage;
    private int size;

    MemoCache(int size) {
        this.size = size;
        storage = new HashMap<K, V>(size);
    }

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }

    @Override
    public V get(K key) {
        return storage.get(key);
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    @Override
    public boolean isFull() {
        return storage.size() == this.size;
    }
}