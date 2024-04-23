package org.hbrs.ooka.impl;


import org.hbrs.ooka.Caching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheImpl implements Caching {
    Map<String, List<String>> cache;
    private static Caching INSTANCE;

    private CacheImpl() {
        cache = new HashMap<>();
    }

    @Override
    public void cacheResult(String key, List<String> value) {
        cache.put(key, value);
    }

    @Override
    public List<String> get(String key) {
        return cache.get(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    public static Caching getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CacheImpl();
        return INSTANCE;
    }
}
