package org.hbrs.ooka.uebung1.ports;


import org.hbrs.ooka.Caching;

import java.util.ArrayList;
import java.util.List;

public class CachingPort implements Caching {
    Caching cache;

    protected CachingPort(Caching cache) {
        this.cache = cache;
    }

    @Override
    public void cacheResult(String key, List<String> value) {
        if (cache != null)
            cache.cacheResult(key, value);
    }

    @Override
    public List<String> get(String key) {
        if (cache != null)
            return cache.get(key);
        else return new ArrayList<>();
    }

    @Override
    public void clear() {
        if (cache != null)
            cache.clear();
    }
}
