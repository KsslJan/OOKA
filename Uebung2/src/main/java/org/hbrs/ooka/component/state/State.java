package org.hbrs.ooka.component.state;

import org.hbrs.ooka.lzu.Deployable;

public interface State {
    void deploy(Deployable d);

    void undeploy(Deployable d);

    void run(Deployable d);

    void stop(Deployable d);
}
