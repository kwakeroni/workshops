package com.quaxantis.scratch.stats;

import com.quaxantis.scratch.stats.StatisticsPoller.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Supplier;

public class Statistics {
    private static final Logger log = LoggerFactory.getLogger(Statistics.class);

    private Statistics() {
        throw new UnsupportedOperationException();
    }

    public static Statistic<Long> maxActiveThreadCountOf(ExecutorService executorService) {
        return new Statistic<>(
                "Max active threads on " + executorService.getClass().getSimpleName(),
                activeThreadCountSupplier(executorService),
                Long::max
        );
    }

    private static Supplier<Long> activeThreadCountSupplier(ExecutorService executorService) {
        return switch (executorService) {
            case ThreadPoolExecutor executor -> () -> (long) executor.getActiveCount();
            case ExecutorService executor
                    when CLASS_NAME_ThreadPerTaskExecutor.equals(executor.getClass().getName()) ->
                    () -> threadsPerTaskCount.apply(executor);
            default -> () -> -1L;
        };
    }

    private static final String CLASS_NAME_ThreadPerTaskExecutor = "java.util.concurrent.ThreadPerTaskExecutor";

    private static final Function<ExecutorService, Long> threadsPerTaskCount;

    static {
        // --add-opens java.base/java.util.concurrent=ALL-UNNAMED
        Function<ExecutorService, Long> function;
        try {
            Class<?> ThreadPerTaskExecutorClass = Class.forName(CLASS_NAME_ThreadPerTaskExecutor);
            Method threadCount = ThreadPerTaskExecutorClass.getMethod("threadCount");
            threadCount.setAccessible(true);
            function = executor -> threadCountOf(executor, threadCount);
        } catch (Exception exc) {
            log.error("Unable to create statistics for ThreadPerTaskExecutor", exc);
            function = ignored -> -1L;
        }
        threadsPerTaskCount = function;
    }

    private static long threadCountOf(ExecutorService executor, Method threadCountMethod) {
        try {
            return (long) threadCountMethod.invoke(executor);
        } catch (Exception exc) {
            log.error("Unable to obtain threadCount statistic", exc);
            return -1L;
        }
    }
}
