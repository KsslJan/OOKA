package org.hbrs.ooka.lzu;

import org.hbrs.ooka.component.state.State;

import java.net.MalformedURLException;

public interface Deployable {
    void start() throws MalformedURLException;

    String getId();

    String getName();

    State getState();

    void stop();

    void setState(State state);
}
