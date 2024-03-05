package com.quaxantis.scratch.service;

import com.quaxantis.scratch.zipkin.ZipkinProxy;

import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AggregatorService extends AbstractService {
    private final AccountService accountService;

    public AggregatorService(AccountService accountService) {
        super(Executors.newFixedThreadPool(4));
//        super(Executors.newVirtualThreadPerTaskExecutor());
        this.accountService = accountService;
        log.info("Using executor service {}", toStringRepresentation(executorService));
    }

    @ZipkinProxy.Span
    public Object aggregateAccounts(ServiceRequest request) {
        var futures = IntStream.range(0, request.aggregateCount())
                .mapToObj(i -> request + "/" + i)
                .map(ignored -> callAsync(() -> accountService.getAccount(request)))
                .toList();

        return futures.stream()
                .map(AbstractService::waitFor)
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
