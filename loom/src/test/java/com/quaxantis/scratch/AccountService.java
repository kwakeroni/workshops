package com.quaxantis.scratch;

public class AccountService extends AbstractService {
    @ZipkinProxy.Span
    public Object getAccount(Object request) {
        sleep(3000);
        return "R" + request;
    }
}
