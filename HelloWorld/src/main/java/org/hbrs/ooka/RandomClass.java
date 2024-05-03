package org.hbrs.ooka;

import java.util.Random;

public class RandomClass {
    public void random() {
        Random rand = new Random();
        int i = rand.nextInt();
        System.out.println("Random number is " + i);
    }
}
