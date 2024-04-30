package org.hbrs.componentassembler;

import org.hbrs.ooka.lzu.Component;
import org.hbrs.ooka.lzu.Lzu;

import java.net.MalformedURLException;

public class Main {

    public static synchronized void main(String[] args) throws MalformedURLException, InterruptedException {
        Lzu lzu = Lzu.getInstance();
        lzu.startLzu();
        synchronized (lzu) {
            try {
                Component testComponent = new Component("1", "Test Component", "C:/Workspaces/OOKA/Uebungen/ComponentAssembler/src/main/resources/HelloWorld-1.0-SNAPSHOT.jar");
                lzu.deploy(testComponent);
                lzu.wait(3000);
                testComponent.start();
            } catch (Exception e) {
                e.printStackTrace();
                //nothing
            }
//            lzu.deploy(new Component("2", "Test Component2", ""));
            lzu.wait(3000);
            lzu.stopLzu();
        }
    }
}