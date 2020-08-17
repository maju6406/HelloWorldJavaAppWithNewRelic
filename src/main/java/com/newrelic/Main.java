package com.newrelic;

import com.newrelic.api.agent.Trace;

/**
 * Hello world!
 */
public final class Main {

    private Main() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    @Trace(dispatcher = true)
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook is running...");
            }
        });

        Main.doSomething();
    }

    /**
     * Does Something.
     */
    @Trace(dispatcher = true)
    public static void doSomething() {
        Foo f = new Foo();
        do {
            f.beginTransaction();
            f.process();
            f.endTransaction();
        } while (true);

    }

}
