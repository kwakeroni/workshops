package com.quaxantis.scratch.service;

import com.quaxantis.scratch.zipkin.ZipkinProxy;

import java.util.concurrent.Executors;

public class AccountService extends AbstractService {
    public AccountService() {
//        super(Executors.newFixedThreadPool(4));
        super(Executors.newVirtualThreadPerTaskExecutor());
    }

    @ZipkinProxy.Span
    public Object getAccount(Object request) {
        try {
            return executorService.submit(() -> {
                long start = System.currentTimeMillis();
                long result = (long) (Math.random() * Double.MAX_VALUE);
                while (System.currentTimeMillis() < start + 3000) {
                    result += (long) (Math.random() * 1000d);
                }
                return "R" + request + "=" + result;
            }).get();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
