package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.Caching;
import org.hbrs.ooka.uebung1.db.DBAccess;
import org.hbrs.ooka.uebung1.ports.CachingPort;
import org.hbrs.ooka.uebung1.subsystem.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelRetrieval implements HotelSearch {
    private final DBAccess dbAccess = new DBAccess();
    private final Caching cache;

    public HotelRetrieval(CachingPort caching) {
        this.cache = caching;
    }

    @Override
    public Hotel[] getHotelByName(String name) {
        List<String> objects;
        if (cache.get(name) != null) {
            objects = cache.get(name);
        } else {
            objects = dbAccess.getObjects(DBAccess.HOTEL, name);
        }
        cache.cacheResult(name, objects);
        List<Hotel> hotels = new ArrayList<>();
        for (int i = 0; i < objects.size(); i += 3) {
            String id = objects.get(i);
            String hotelName = objects.get(i + 1);
            String location = objects.get(i + 2);
            hotels.add(new Hotel(id, hotelName, location));
        }

        return hotels.toArray(new Hotel[0]);
    }

    @Override
    public void openSession() {
        dbAccess.openConnection();
    }

    @Override
    public void closeSession() {
        dbAccess.closeConnection();
    }
}
