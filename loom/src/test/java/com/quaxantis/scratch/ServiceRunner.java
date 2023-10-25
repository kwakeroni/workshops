package com.quaxantis.scratch;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class ServiceRunner extends AbstractService {
    public static void main(String[] args) {
        ZipkinProxy zipkinProxy = new ZipkinProxy();

        try (AccountService accSvc = zipkinProxy.proxy(new AccountService());
             AggregatorService aggSvc = zipkinProxy.proxy(new AggregatorService(accSvc));
             ServiceRunner serviceRunner = zipkinProxy.proxy(new ServiceRunner(aggSvc))) {
            serviceRunner.run();
        }
    }

    private final AggregatorService aggregatorService;

    public ServiceRunner(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @ZipkinProxy.Span
    public void run() {
        var futures =
                IntStream.range(0, 3)
                        .peek(_ -> sleep(100))
                        .mapToObj(this::aggregateAccounts)
                        .toList();

        var results =
                futures.stream()
                        .map(AbstractService::waitFor)
                        .toList();

        System.out.println(results.size() + " Results");
    }

    private CompletableFuture<?> aggregateAccounts(int i) {
        return callAsync(() -> aggregatorService.aggregateAccounts(i));
    }
}
