package org.hbrs.ooka.uebung1.ports;

import org.hbrs.ooka.impl.CacheImpl;
import org.hbrs.ooka.uebung1.HotelRetrieval;

public class Port {

    public static CachingPort createCachingPort() {
        return new CachingPort(CacheImpl.getInstance());
    }

    public static SearchPort createSearchPort() {
        return new SearchPort(new HotelRetrieval(createCachingPort()));
    }
}
