package com.strategies;

public class StrategyMRU<K> extends Strategy<K> {

    @Override
    public void put(K key) {
        objects.put(key, System.nanoTime());
    }

    @Override
    public K getReplacedKey(){
        sortedObjects.putAll(objects);
        return sortedObjects.lastKey();
    }
}