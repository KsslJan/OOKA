package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.uebung1.subsystem.Hotel;

public interface HotelSearch {
    Hotel[] getHotelByName(String name);

    void openSession();

    void closeSession();
}
