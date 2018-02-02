package com.strategies;

public class StrategyLRU<K> extends Strategy<K> {

    @Override
    public void put(K key) {
        objects.put(key, System.nanoTime());
    }
}
