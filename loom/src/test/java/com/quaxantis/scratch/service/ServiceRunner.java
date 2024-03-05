package com.quaxantis.scratch.service;

import com.quaxantis.scratch.ProgressPrinter;
import com.quaxantis.scratch.zipkin.ZipkinProxy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.quaxantis.scratch.ProgressPrinter.numberPattern;

public class ServiceRunner extends AbstractService {
    public static void runWith(RunnerParams params) {
        ZipkinProxy zipkinProxy = new ZipkinProxy();

        try (AccountService accSvc = zipkinProxy.proxy(new AccountService());
             AggregatorService aggSvc = zipkinProxy.proxy(new AggregatorService(accSvc));
             ServiceRunner serviceRunner = zipkinProxy.proxy(new ServiceRunner(aggSvc, params))) {
            serviceRunner.run();
        }
    }

    private final AggregatorService aggregatorService;
    private final RunnerParams params;

    private ServiceRunner(AggregatorService aggregatorService, RunnerParams params) {
        super(Executors.newFixedThreadPool(params.concurrentRequests() ? 10 : 1));
        this.aggregatorService = aggregatorService;
        this.params = params;
    }

    public void run() {
        run(this.params.runCount());
    }

    @ZipkinProxy.Span
    public void run(int count) {
        var futures =
                IntStream.range(0, count)
                        .mapToObj(this::aggregateAccounts)
                        .collect(logAndCount(
                                "Sent %s/%s requests".formatted(numberPattern(count), count),
                                Collectors.toList()));

        var results =
                futures.stream()
                        .map(ServiceRunner::waitFor)
                        .collect(logAndCount(
                                "Received %s/%s responses".formatted(numberPattern(futures.size()), futures.size()),
                                Collectors.toList()));

        log.debug(results.size() + " Results: " + results);
    }

    private CompletableFuture<?> aggregateAccounts(int i) {
        return callAsync(() -> aggregatorService.aggregateAccounts(new ServiceRequest(i, params.aggregateCount())));
    }

    private <T, A, R> Collector<T, ?, R> logAndCount(String pattern, Collector<T, A, R> downStream) {
        AtomicLong counter = new AtomicLong();
        ProgressPrinter progress = new ProgressPrinter(pattern, false);
        progress.update(counter.get());
        return Collector.of(
                downStream.supplier(),
                (accu, t) -> {
                    counter.updateAndGet(c -> progress.update(c + 1));
                    downStream.accumulator().accept(accu, t);
                },
                downStream.combiner(),
                accu -> {
                    progress.finish();
                    return downStream.finisher().apply(accu);
                });
    }
}
