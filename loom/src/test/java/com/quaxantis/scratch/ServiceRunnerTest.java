package com.quaxantis.scratch;

import com.quaxantis.scratch.service.RunnerParams;
import com.quaxantis.scratch.service.ServiceRunner;
import org.junit.jupiter.api.Test;

public class ServiceRunnerTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.showShortLogName", "true");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    @Test
    void test() {
        ServiceRunner.runWith(
                new RunnerParams()
                        .withRunCount(3)
                        .withAggregateCount(3)
                        .withConcurrentRequests(false));
    }
}
