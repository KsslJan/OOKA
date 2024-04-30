package org.hbrs.ooka.lzu;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Lzu extends Thread implements LzuPort {
//    private volatile boolean running;

    private final ConcurrentMap<String, Thread> map = new ConcurrentHashMap<>();

    private static Lzu INSTANCE;

    private Lzu() {
    }

    public static Lzu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Lzu();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        //TODO maybe replace with cli system.in?

//        running = true;
        while (true) {
            System.out.println("Checking for components");
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    return;
                }
                if (isInterrupted()) {
                    return;
                }
            }
        }
    }

    @Override
    public void startLzu() {
        start();
    }

    @Override
    public void deploy(Deployable d) {
        //TODO throw the stack overflow stuff in here ===> I dont think so... I do not see a valid way to do that: What to initialize and save?

        //TODO_done create a new Thread for every component

//        if (!d.getClass().isAnnotationPresent(ComponentStart.class) && !d.getClass().isAnnotationPresent(ComponentStop.class)) {
//        }

        Thread thread = new Thread() {
            public void run() {
                try {
                    wait();
                    if (d instanceof Component component)
                        component.invokeStart();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        map.put(d.getId(), thread);
    }


    public void undeploy(Deployable d) {
        System.out.println("Undeploying " + d);
    }

    @Override
    public void stopLzu() {
//        running = false;
        System.out.println("Stopping Lzu");
        synchronized (this) {
            interrupt();
        }
    }

    protected Thread getThread(String id) {
        return map.get(id);
    }
}
