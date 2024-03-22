package com.quaxantis.scratch;

import com.quaxantis.scratch.stats.Statistics;
import com.quaxantis.scratch.stats.StatisticsPoller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ThreadSpawnTest {

    @Test
    void test() {
        runTest(Thread::ofPlatform, 50000, () -> sleep(10000));
//        runTest(Thread::ofVirtual, 50000, () -> sleep(10000));
//        runTest(Thread::ofPlatform, 50000, () -> fibonacci(27));
//        runTest(Thread::ofVirtual, 50000, () -> fibonacci(27));
    }

    void runTest(Supplier<Thread.Builder> threadSupplier, int threadCount, Runnable task) {
        Stream.generate(threadSupplier)
                .limit(threadCount)
                .map(thread -> thread.start(counting(task)))
                .toList()
                .forEach(this::joinThread);
    }

    private Runnable counting(Runnable task) {
        return () -> {
            try {
                threadsStarted.incrementAndGet();
                task.run();
            } finally {
                threadsFinished.incrementAndGet();
            }
        };
    }

    private void joinThread(Thread thread) {
        try {
            threadTypes.add(threadType(thread));
            thread.join();
        } catch (InterruptedException exc) {
            System.err.println(exc);
        }
    }

    private static BigDecimal fibonacci(int i) {
        if (i == 0) {
            return BigDecimal.ZERO;
        } else if (i == 1) {
            return BigDecimal.ONE;
        } else {
            return fibonacci(i - 2).add(fibonacci(i - 1));
        }
    }

    private final Set<String> threadTypes = new HashSet<>();
    private final AtomicInteger threadsStarted = new AtomicInteger(0);
    private final AtomicInteger threadsFinished = new AtomicInteger(0);
    private final AtomicReference<Thread> loggingThread = new AtomicReference<>();
    private final StatisticsPoller poller = new StatisticsPoller();
    private final AtomicLong testStartMillis = new AtomicLong();
    private final boolean startJConsole = false;
    private final boolean pollStats = true;

    @BeforeEach
    void setupStats() throws IOException {
        long pid = ProcessHandle.current().pid();
        System.out.printf("Running test with PID=%s%n", pid);
        if (startJConsole) {
            new ProcessBuilder("cmd", "/C start /B /realtime %s\\bin\\jconsole.exe %s".formatted(System.getProperty("java.home"), pid)).start();
        }
        testStartMillis.set(System.currentTimeMillis());
        if (pollStats) {
            Thread.ofPlatform()
                    .start(() -> {
                        loggingThread.set(Thread.currentThread());
                        while (loggingThread.get() == Thread.currentThread()) {
                            sleep(1000);
                            int started = threadsStarted.get();
                            int finished = threadsFinished.get();
                            System.out.printf("Threads started: %5s - finished: %5s - active: %5s%n",
                                              started, finished, started - finished);
                        }
                    });
            poller.poll(Statistics.maxHeapMemory());
            poller.poll(Statistics.maxProcessMemoryOf(pid));
        }
    }

    @AfterEach
    void tearDownStats() throws InterruptedException {
        if (pollStats) {
            loggingThread.getAndSet(null).join();
            poller.closeAndLog(System.out::println);
            System.out.println("Available processors: " +         Runtime.getRuntime().availableProcessors());
        }
        long durationMillis = System.currentTimeMillis() - testStartMillis.get();
        System.out.printf("Finished %s %s threads in %ss%n",
                          threadsFinished.get(),
                          String.join(" or ", threadTypes),
                          new BigDecimal(durationMillis).movePointLeft(3));
    }

    static String threadType(Thread thread) {
        return (thread.isVirtual()) ? "virtual" : "platform";
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
