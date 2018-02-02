package com.cache;

import com.strategies.StrategyTypes;
import org.junit.After;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;


public class L2CacheTest {
    private L2Cache<Integer, Integer> cache = new L2Cache<Integer, Integer>(3, StrategyTypes.LFU);

    @After
    public void after() {
        cache.clear();
    }

    @Test
    public void test() {

        cache.put(1, 1);
        cache.put(1, 1);
        cache.put(1, 1);
        cache.put(2, 2);

        assertTrue(!cache.isFull());

        cache.put(3, 3);
        cache.put(4, 4);
        cache.put(3, 3);
        cache.put(5, 5);
        cache.put(6, 6);

        cache.remove(100);

        assertTrue(1 == cache.get(1));
        assertTrue(1 != cache.get(2));
        assertTrue(cache.containsKey(1));

        assertTrue(cache.isFull());

        get();
    }

    @Test
    public void get() {
        assertNull(cache.get(100));
    }

    @Test
    public void isFull() {
        assertTrue(!cache.isFull());
    }
}
