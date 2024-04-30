package org.hbrs.ooka.lzu;

public interface LzuPort {
    void startLzu();

    void deploy(Deployable d);

    void undeploy(Deployable d);

    void stopLzu();
}
