package com.quaxantis.scratch;

import java.io.Closeable;
import java.util.concurrent.*;
import java.util.function.Supplier;


public abstract class AbstractService implements Closeable {
    final ExecutorService executorService;

    protected AbstractService() {
        this(Executors.newFixedThreadPool(5));
    }

    protected AbstractService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    protected final <T> CompletableFuture<T> callAsync(Supplier<T> call) {
        Supplier<T> callWithTrace = ZipkinProxy.propagateAsyncTrace(call);
        return CompletableFuture.supplyAsync(callWithTrace, executorService);
    }

    @Override
    public void close() {
        this.executorService.shutdown();
    }

    static Object waitFor(Future<?> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
