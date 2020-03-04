package be.kwakeroni.workshop.java9.solution.library;

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

        // Thread-safe
        private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

        private String category;

        public Logger(String category) {
            this.category = category;
        }

        public void info(String message) {

            StackWalker.StackFrame frame = STACK_WALKER.walk(frames -> frames.skip(1).findFirst()).get();

            String className = frame.getClassName();
            String methodName = frame.getMethodName();
            int lineNumber = frame.getLineNumber();

            System.out.println(String.format("[%7s] [%82s:%4s] %s", this.category, className+'.'+methodName, lineNumber, message));
        }
        {
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                    .walk(frames -> frames
                            .filter(frame -> ! frame.getDeclaringClass().getName().startsWith("org.log4j"))
                            .findFirst());
        }

    }

}
