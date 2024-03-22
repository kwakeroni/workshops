package com.quaxantis.scratch.stats;

import com.quaxantis.scratch.stats.StatisticsPoller.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Method;
import java.text.CompactNumberFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Supplier;

public class Statistics {
    private static final Logger log = LoggerFactory.getLogger(Statistics.class);
    private static final NumberFormat COMPACT_FORMAT = NumberFormat.getCompactNumberInstance(Locale.ROOT, NumberFormat.Style.LONG);

    private Statistics() {
        throw new UnsupportedOperationException();
    }

    public static Statistic<Long> maxHeapMemory() {
        return new Statistic<>("Max heap memory",
                               heapMemory(),
                               Long::max,
                               COMPACT_FORMAT::format);
    }

    public static Statistic<Long> maxProcessMemoryOf(long pid) {
        return new Statistic<>("Max process memory",
                               processMemoryOf(pid),
                               Long::max,
                               COMPACT_FORMAT::format);
    }

    public static Statistic<Long> maxActiveThreadCountOf(ExecutorService executorService) {
        return new Statistic<>(
                "Max active threads on " + executorService.getClass().getSimpleName(),
                activeThreadCountSupplier(executorService),
                Long::max
        );
    }

    static Supplier<Long> heapMemory() {
        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        return () -> mxBean.getHeapMemoryUsage().getUsed();
    }

    static Supplier<Long> processMemoryOf(long pid) {
        return () -> getProcessMemoryOf(pid);
    }

    static long getProcessMemoryOf(long pid) {
        String info = getProcessInfo(pid);
        int lastQuote = info.lastIndexOf('"');
        int penultimateQuote = info.lastIndexOf('"', lastQuote - 1);
        String mem = info.substring(penultimateQuote + 1, lastQuote).replaceAll("[, ]", "");
        try {
            return CompactNumberFormat.getCompactNumberInstance().parse(mem).longValue();
        } catch (Exception exc) {
            return -1;
        }
    }

    static String getProcessInfo(long pid) {
        try {
            var process = new ProcessBuilder()
                    .command("tasklist", "/FO", "csv", "/nh", "/fi", "PID eq " + pid)
                    .start();
            process.waitFor();
            try (
                    StringWriter stringWriter = new StringWriter();
                    Reader reader = process.inputReader()) {
                reader.transferTo(stringWriter);
                return stringWriter.toString().strip();
            }
        } catch (Exception exc) {
            System.err.println("Error reading process memory: " + exc);
            return "\"-1\"";
        }
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
