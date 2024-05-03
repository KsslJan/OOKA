package org.hbrs.ooka;

public class HelloWorld {
    @StartComponent
    public void start() {
        System.out.println("Hello component assembler!");
    }

    @StopComponent
    public void stop() {
        System.out.println("Component stopped");
    }
}