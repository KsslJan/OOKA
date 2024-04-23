package org.hbrs.ooka;

import org.hbrs.ooka.uebung1.HotelSearch;
import org.hbrs.ooka.uebung1.ports.Port;
import org.hbrs.ooka.uebung1.subsystem.Hotel;

import java.util.Arrays;

public class ExternalClient {
    public static void main(String[] args) {
        HotelSearch hotelSearch = Port.createSearchPort();
        hotelSearch.openSession();
        Hotel[] hotels = hotelSearch.getHotelByName("Jahr");
        System.out.println(Arrays.toString(hotels));
    }
}