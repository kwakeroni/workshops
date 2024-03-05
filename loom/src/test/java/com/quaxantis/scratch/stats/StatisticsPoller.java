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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StatisticsPoller implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(StatisticsPoller.class);
    private final List<StatisticResult<?>> statistics = new ArrayList<>();
    private volatile boolean closed = false;

    @Override
    public void close() {
        this.closed = true;
        for (StatisticResult<?> stat : this.statistics) {
            log.info(stat.toString());
        }
    }

    protected boolean isClosed() {
        return this.closed;
    }

    public <T> void poll(Statistic<T> statistic) {
        this.statistics.add(new StatisticResult<T>(statistic, poll(statistic.supplier(), statistic.reducer())));
    }

    private <T> Future<Optional<T>> poll(Supplier<? extends T> supplier, BinaryOperator<T> reducer) {
        CompletableFuture<Optional<T>> result = new CompletableFuture<>();

        Thread.ofPlatform().start(() -> {
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

    private record StatisticResult<T>(Statistic<T> statistic, Future<Optional<T>> future) {
        @Override
        public String toString() {
            try {
                return statistic().name() + ": " + future.get().map(statistic::toString).orElse("unknown");
            } catch (InterruptedException | ExecutionException exc) {
                throw new RuntimeException(exc);
            }
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
