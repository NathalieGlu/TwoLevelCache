package com.strategies;

public class StrategyLFU<K> extends Strategy<K> {

    @Override
    public void put(K key) {
        long frequency = 1;
        if (objects.containsKey(key)) {
            frequency = objects.get(key) + 1;
        }
        objects.put(key, frequency);
    }
}
