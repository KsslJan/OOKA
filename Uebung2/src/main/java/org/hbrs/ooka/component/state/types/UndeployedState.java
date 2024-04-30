package org.hbrs.ooka.component.state.types;

import org.hbrs.ooka.component.state.State;
import org.hbrs.ooka.lzu.Deployable;

public class UndeployedState implements State {
    @Override
    public void deploy(Deployable d) {

    }

    @Override
    public void undeploy(Deployable d) {

    }

    @Override
    public void run(Deployable d) {

    }

    @Override
    public void stop(Deployable d) {

    }
}
