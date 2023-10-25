package com.quaxantis.scratch;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class ServiceRunner extends AbstractService {
    public static void main(String[] args) {
        ZipkinProxy zipkinProxy = new ZipkinProxy();

        try (AccountService accSvc = zipkinProxy.proxy(new AccountService());
             AggregatorService aggSvc = zipkinProxy.proxy(new AggregatorService(accSvc));
             ServiceRunner serviceRunner = zipkinProxy.proxy(new ServiceRunner(aggSvc))) {
            serviceRunner.run(3);
        }
    }

    private final AggregatorService aggregatorService;

    public ServiceRunner(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @ZipkinProxy.Span
    public void run(int count) {
        var futures =
                IntStream.range(0, count)
                        .peek(_ -> sleep(200))
                        .mapToObj(this::aggregateAccounts)
                        .toList();

        var results =
                futures.stream()
                        .map(AbstractService::waitFor)
                        .toList();

        System.out.println(results.size() + " Results: " + results);
    }

    private CompletableFuture<?> aggregateAccounts(int i) {
        return callAsync(() -> aggregatorService.aggregateAccounts(i));
    }
}
