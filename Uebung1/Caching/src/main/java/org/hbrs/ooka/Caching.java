package org.hbrs.ooka;

import java.util.List;

public interface Caching {
    void cacheResult(String key, List<String> value);

    List<String> get(String key);

    void clear();
}
