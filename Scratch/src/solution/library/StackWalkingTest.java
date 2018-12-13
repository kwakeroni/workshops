package solution.library;

import org.junit.Test;

public class StackWalkingTest {

    private static final Logger LOG = new Logger("test");

    @Test
    public void testLogging() {
        Runnable runnable = () -> {
            LOG.info("Creating Service");
            Service service = new Service();
            service.doSomething();
            LOG.info("Test Finished");
        };

        long count = 50000L;

        long start = System.currentTimeMillis();
        for (long i=0; i<10000; i++){
            runnable.run();
        }
        long total = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        for (long i=0; i<count; i++){
            runnable.run();
        }
        total = System.currentTimeMillis() - start;
        for (long i=0; i<10000; i++){
            runnable.run();
        }

        System.out.println("Total time: " + total + "ms");
        System.out.println("Per run: " + (count * 1000 / total) + "/second = " + ((double) total / (double) count) + "s");
    }

    public static class Service {
        private static final Logger LOG = new Logger("service");

        public void doSomething(){
            LOG.info("Doing Something");
        }

    }


    public static class Logger {

        private String category;

        public Logger(String category) {
            this.category = category;
        }

        public void info(String message) {

            StackTraceElement stackTrace = new Exception().getStackTrace()[1];
            String className = stackTrace.getClassName();
            String methodName = stackTrace.getMethodName();
            int lineNumber = stackTrace.getLineNumber();

            System.out.println(String.format("[%7s] [%82s:%4s] %s", this.category, className+'.'+methodName, lineNumber, message));
        }

//        Stacktrace
//        Total time: 3792ms
//        Per run: 13185/second = 0.07584s


//        Stackwalker
//        Total time: 1806ms
//        Per run: 27685/second = 0.03612s
    }

}
