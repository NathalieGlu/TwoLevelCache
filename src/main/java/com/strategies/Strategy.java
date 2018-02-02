package com.strategies;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public abstract class Strategy<K> {
    final Map<K, Long> objects;
    final TreeMap<K, Long> sortedObjects;

    Strategy() {
        this.objects = new TreeMap<K, Long>();
        this.sortedObjects = new TreeMap<K, Long>(new ObjectsComparator<K>(objects));
    }

    public abstract void put(K key);

    public void remove(K key) {
        if (objects.containsKey(key)) {
            objects.remove(key);
        }
    }

    public boolean containsKey(K key) {
        return objects.containsKey(key);
    }

    public K getReplacedKey() {
        sortedObjects.putAll(objects);
        return sortedObjects.firstKey();
    }

    public void clear() {
        sortedObjects.clear();
    }
}

class ObjectsComparator<K> implements Comparator<K> {
    private final Map<K, Long> objects;

    ObjectsComparator(Map<K, Long> val) {
        this.objects = val;
    }

    public int compare(K x, K y) {
        return objects.get(x).compareTo(objects.get(y));
    }
}
