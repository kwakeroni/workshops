package com.quaxantis.scratch.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StatisticsPoller implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(StatisticsPoller.class);
    private final List<StatisticFuture<?>> statistics = new ArrayList<>();
    private volatile boolean closed = false;

    @Override
    public void close() {
        closeAndLog(stat -> log.info(stat.toString()));
    }

    public void closeAndLog(Consumer<StatisticResult<?>> logger) {
        this.closed = true;
        for (StatisticFuture<?> stat : this.statistics) {
            logger.accept(stat.toResult());
        }
    }

    protected boolean isClosed() {
        return this.closed;
    }

    public <T> void poll(Statistic<T> statistic) {
        this.statistics.add(new StatisticFuture<>(statistic, poll(statistic.supplier(), statistic.reducer())));
    }

    private <T> Future<Optional<T>> poll(Supplier<? extends T> supplier, BinaryOperator<T> reducer) {
        CompletableFuture<Optional<T>> result = new CompletableFuture<>();

        Thread.ofPlatform()
                .priority(Thread.MAX_PRIORITY)
                .start(() -> {
                    Optional<T> stat = Stream.<T>generate(supplier)
                            .peek(ignored -> sleep(1000))
                            .takeWhile(ignored -> !isClosed())
                            .reduce(reducer);
                    result.complete(stat);
                });

        return result;
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private record StatisticFuture<T>(Statistic<T> statistic, Future<Optional<T>> future) {
        StatisticResult<T> toResult() {
            try {
                return new StatisticResult<>(statistic, future.get());
            } catch (InterruptedException | ExecutionException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

    public record StatisticResult<T>(Statistic<T> statistic, Optional<T> value) {
        @Override
        public String toString() {
            return statistic().name() + ": " + value.map(statistic::toString).orElse("unknown");
        }
    }

    public record Statistic<T>(
            String name,
            Supplier<? extends T> supplier,
            BinaryOperator<T> reducer,
            Function<? super T, String> stringifier
    ) {

        public Statistic(String name, Supplier<? extends T> supplier, BinaryOperator<T> reducer) {
            this(name, supplier, reducer, String::valueOf);
        }

        public String toString(T t) {
            return stringifier().apply(t);
        }
    }
}
