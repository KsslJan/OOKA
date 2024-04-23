package org.hbrs.ooka.uebung1.ports;

import org.hbrs.ooka.uebung1.HotelRetrieval;
import org.hbrs.ooka.uebung1.HotelSearch;
import org.hbrs.ooka.uebung1.subsystem.Hotel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchPort implements HotelSearch {
    private final HotelSearch hotelSearch;
    private static final Logger LOG = Logger.getLogger(SearchPort.class.getName());
    private boolean sessionOpen;

    protected SearchPort(HotelRetrieval hotelRetrieval) {
        this.hotelSearch = hotelRetrieval;
    }

    @Override
    public Hotel[] getHotelByName(String name) {
        if (!sessionOpen) {
            LOG.log(Level.SEVERE, "Session needs to be opened first.");
            return new Hotel[0];
        }
        log(name);
        return hotelSearch.getHotelByName(name);
    }

    private static void log(String name) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        String formattedDateTime = now.format(formatter);
        LOG.log(Level.INFO, formattedDateTime + " Zugriff auf Buchungssystem über Methode getHotelByName. Suchwort: " + name);
    }

    @Override
    public void openSession() {
        LOG.log(Level.INFO, "Open session.");
        hotelSearch.openSession();
        sessionOpen = true;
    }

    @Override
    public void closeSession() {
        LOG.log(Level.INFO, "Close session.");
        hotelSearch.closeSession();
        sessionOpen = false;
    }
}
