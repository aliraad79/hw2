package mobile.sharif.hw2;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private final ExecutorService executorService;
    private static ThreadPool threadPool = null;

    private ThreadPool() {
        executorService = Executors.newFixedThreadPool(5);
    }

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    public void end() {
        try {
            Log.i("ThreadPool", "end: attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.i("ThreadPool", "end: tasks interrupted");
        } finally {
            if (!executorService.isTerminated()) {
                Log.i("ThreadPool", "end: cancel non-finished tasks");
            }
            executorService.shutdownNow();
            Log.i("ThreadPool", "end: shutdown finished");
        }
    }
}
