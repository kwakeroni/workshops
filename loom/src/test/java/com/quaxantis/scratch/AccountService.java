package com.quaxantis.scratch;

import java.util.concurrent.Executors;

public class AccountService extends AbstractService {
    public AccountService() {
        super(Executors.newFixedThreadPool(40));
    }

    @ZipkinProxy.Span
    public Object getAccount(Object request) {
        try {
            return executorService.submit(() -> {
                sleep(3000);
                return "R" + request;
            }).get();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
