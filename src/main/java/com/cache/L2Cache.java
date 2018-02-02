package com.cache;

import com.strategies.*;

import java.io.File;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class L2Cache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private MemoCache<K, V> firstLevel;
    private FileSystemCache<K, V> secondLevel;
    private final Strategy<K> strategy;
    private Logger log = Logger.getLogger(L2Cache.class.getName());

    L2Cache(final int size, StrategyTypes type) {
        this.firstLevel = new MemoCache<K, V>(size);
        this.secondLevel = new FileSystemCache<K, V>(size);
        this.strategy = getStrategy(type);

        String path = "cache";
        File dir = new File(path);
        if (!dir.exists()) {
            log.info(String.format("Creating directory %s...", path));
            if (dir.mkdir()) {
                log.info("Created directory");
            } else {
                log.info("Can't create directory");
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (!firstLevel.isFull() || firstLevel.containsKey(key)) {
            firstLevel.put(key, value);
            if (secondLevel.containsKey(key)) {
                secondLevel.remove(key);
            }
            log.info(String.format("Put key %s to level 1", key));
        } else if (!secondLevel.isFull() || secondLevel.containsKey(key)) {
            secondLevel.put(key, value);
            log.info(String.format("Put key %s to level 2", key));
        } else {
            replace(key, value);
        }

        strategy.put(key);

    }

    @Override
    public V get(K key) {
        try {
            if (firstLevel.containsKey(key)) {
                return firstLevel.get(key);
            } else if (secondLevel.containsKey(key)) {
                return secondLevel.get(key);
            } else {
                throw new NoSuchElementException(String.format("Can't get object with key %s: Not found", key));
            }
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public void remove(K key) {
        try {
            if (firstLevel.containsKey(key)) {
                firstLevel.remove(key);
                log.info(String.format("Removed key %s from level 1", key));
            } else if (secondLevel.containsKey(key)) {
                secondLevel.remove(key);
                log.info(String.format("Removed key %s from level 2", key));
            } else {
                throw new NoSuchElementException(String.format("Can't remove object with key %s: Not found", key));
            }
            strategy.remove(key);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
        }
    }

    @Override
    public void clear() {
        firstLevel.clear();
        secondLevel.clear();
        strategy.clear();
    }

    private Strategy<K> getStrategy(StrategyTypes type) {
        switch (type) {
            case LFU:
                return new StrategyLFU<K>();
            case LRU:
                return new StrategyLRU<K>();
            case MRU:
                return new StrategyMRU<K>();
            default:
                return new StrategyMRU<K>();
        }
    }

    @Override
    public boolean containsKey(K key) {
        return strategy.containsKey(key);
    }

    private void replace(K key, V value) {
        K repKey = strategy.getReplacedKey();
        if (firstLevel.containsKey(repKey)) {
            firstLevel.remove(repKey);
            firstLevel.put(key, value);
            log.info(String.format("Replaced key %s at level 1 to key %s", repKey, key));
        } else {
            secondLevel.remove(repKey);
            secondLevel.put(key, value);
            log.info(String.format("Replaced key %s at level 2 to key %s", repKey, key));
        }
    }

    @Override
    public boolean isFull() {
        return firstLevel.isFull() || secondLevel.isFull();
    }
}
