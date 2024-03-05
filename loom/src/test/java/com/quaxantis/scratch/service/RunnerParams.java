package com.quaxantis.scratch.service;

public record RunnerParams(int runCount, int aggregateCount, boolean concurrentRequests) {

    public RunnerParams() {
        this(3, 3, true);
    }

    public RunnerParams withRunCount(int runCount) {
        return new RunnerParams(runCount, aggregateCount, concurrentRequests);
    }

    public RunnerParams withAggregateCount(int aggregateCount) {
        return new RunnerParams(runCount, aggregateCount, concurrentRequests);
    }

    public RunnerParams withConcurrentRequests(boolean concurrentRequests) {
        return new RunnerParams(runCount, aggregateCount, concurrentRequests);
    }
}
