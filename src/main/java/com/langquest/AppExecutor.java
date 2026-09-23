package com.langquest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // background threads shouldn't keep the app alive on their own
        return thread;
    });

    public static void submit(Runnable task) {
        EXECUTOR.execute(task);
    }
}