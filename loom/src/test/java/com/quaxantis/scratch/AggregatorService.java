package com.quaxantis.scratch;

import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AggregatorService extends AbstractService {
    private final AccountService accountService;

    public AggregatorService(AccountService accountService) {
        super(Executors.newFixedThreadPool(2));
//        super(Executors.newVirtualThreadPerTaskExecutor());
        this.accountService = accountService;
    }

    @ZipkinProxy.Span
    public Object aggregateAccounts(Object request) {
        var futures = IntStream.range(0, 3)
                .mapToObj(i -> request + "/" + i)
                .map(_ -> callAsync(() -> accountService.getAccount(request)))
                .toList();

        return futures.stream()
                .map(AbstractService::waitFor)
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
