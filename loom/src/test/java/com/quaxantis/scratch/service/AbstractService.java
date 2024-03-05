package com.quaxantis.scratch.service;

import com.quaxantis.scratch.stats.Statistics;
import com.quaxantis.scratch.stats.StatisticsPoller;
import com.quaxantis.scratch.zipkin.ZipkinProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.concurrent.*;
import java.util.function.Supplier;

public abstract class AbstractService implements Closeable {
    final Logger log = LoggerFactory.getLogger(getClass());
    final ExecutorService executorService;
    final StatisticsPoller poller;

    protected AbstractService(ExecutorService executorService) {
        this.executorService = executorService;
        this.poller = new StatisticsPoller();
        this.poller.poll(Statistics.maxActiveThreadCountOf(this.executorService));
    }

    protected final <T> CompletableFuture<T> callAsync(Supplier<T> call) {
        Supplier<T> callWithTrace = ZipkinProxy.propagateAsyncTrace(call);
        return CompletableFuture.supplyAsync(callWithTrace, executorService);
    }

    @Override
    public void close() {
        this.executorService.shutdown();
        this.poller.close();
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

    protected String toStringRepresentation(ExecutorService executorService) {
        return switch (executorService) {
            case ThreadPoolExecutor executor -> "with max %s threads".formatted(executor.getMaximumPoolSize());
            case ExecutorService executor -> "with %s threads".formatted(threadType(executor));
        };
    }

    private String threadType(ExecutorService executor) {
        Boolean[] isVirtualThread = new Boolean[1];
        executor.execute(() -> isVirtualThread[0] = Thread.currentThread().isVirtual());
        while (isVirtualThread[0] == null) {
        }
        return (isVirtualThread[0]) ? "virtual" : "platform";
    }
}
