package be.kwakeroni.workshop.java9.exercise.library;

import org.junit.Test;

public class StackWalkingTest {

    private static final Logger LOG = new Logger("test");

    /**
     * Rewrite the Logger.info method to use the new Stackwalker API.
     * This stream-based API is more flexible and has better performance.
     */
    @Test
    public void testLogging() {
            LOG.info("Creating Service");
            Service service = new Service();
            service.doSomething();
            LOG.info("Test Finished");
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

    }

}
