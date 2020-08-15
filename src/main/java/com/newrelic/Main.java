package com.newrelic;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import java.util.Random;

/**
 * Hello world!
 */
public final class Main {

    /**
     * How long to wait.
     */
    public static final int MILLIS = 30000;

    private Main() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Main.doSomething();
    }

    /**
     * Does Something.
     */
    // instrumentation via annotation
    @Trace(dispatcher = true)
    public static void doSomething() {
        System.out.println("Do Something!");
        try {
            do {
                long millisToSleep  = new Random().nextInt();
                Thread.sleep(MILLIS);
                System.out.println("Did Something!");
                // record a response time in milliseconds for the given metric name
                NewRelic.recordResponseTimeMetric("Custom/RandomSleep", millisToSleep);
            } while (true);
        } catch (InterruptedException e) {
            // report a handled exception
            NewRelic.noticeError(e, false);
        }
    }

}
