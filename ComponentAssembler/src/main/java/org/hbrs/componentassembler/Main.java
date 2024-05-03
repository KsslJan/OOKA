package org.hbrs.componentassembler;

import org.hbrs.ooka.lzu.Lzu;

public class Main {

    public static synchronized void main(String[] args) {
        /*
        C:/Workspaces/OOKA/Uebungen/ComponentAssembler/src/main/resources/HelloWorld-1.0-SNAPSHOT.jar
        C:/Workspaces/OOKA/Uebungen/ComponentAssembler/src/main/resources/component-1.0-SNAPSHOT.jar
        */
        Lzu lzu = Lzu.getInstance();
        lzu.start();
    }
}