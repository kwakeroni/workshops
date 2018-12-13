package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

import java.lang.StackWalker.StackFrame;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.StackWalker.Option.*;

/**
 * Created by kwakeroni on 18/07/17.
 */
public class StackWalkers {

    private static final StackWalker BARE = StackWalker.getInstance();
    private static final StackWalker RETAIN = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
    private static final StackWalker REFLECT = StackWalker.getInstance(EnumSet.of(RETAIN_CLASS_REFERENCE, SHOW_REFLECT_FRAMES));
    // Hidden includes reflect frames
    private static final StackWalker HIDDEN = StackWalker.getInstance(EnumSet.of(RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES));


    @Test
    public void getCallerClass(){
        System.out.println("getCallerClass: " + RETAIN.getCallerClass());
        System.out.println("getCallerClass (reflect): " + REFLECT.getCallerClass());
        System.out.println("getCallerClass (hidden): " + HIDDEN.getCallerClass());
    }

    @Test
    public void walk(){
        doWalk("default", RETAIN);
        doWalk("reflect", REFLECT);
        doWalk("hidden", HIDDEN);
    }

    private void doWalk(String kind, StackWalker stackWalker){
        System.out.println("walk " + kind + " --");
        Consumer<StackWalker> action = this::printStackTrace;
        action.accept(stackWalker);
        System.out.println("--");

    }

    private void printStackTrace(StackWalker stackWalker){
        AtomicBoolean finish = new AtomicBoolean(false);
        stackWalker.walk(asFunction(stream -> stream
                .takeWhile(frame -> ! (frame.toString().startsWith("org.junit") && finish.getAndSet(true)))
                .forEach(System.out::println))
        );
    }

    @Test
    public void stackFrames(){
        printFrame("retaining", RETAIN);
        printFrame("bare", BARE);
    }

    private void printFrame(String title, StackWalker walker){
        StackFrame frame = walker.walk(Stream::findFirst).orElse(null);
        System.out.println("StackFrame (" + title + ") --");
        safePrintln(frame, "className", StackFrame::getClassName);
        safePrintln(frame, "declaringClass", StackFrame::getDeclaringClass);
        safePrintln(frame, "methodName", StackFrame::getMethodName);
        safePrintln(frame, "fileName", StackFrame::getFileName);
        safePrintln(frame, "lineNumber", StackFrame::getLineNumber);
        safePrintln(frame, "stackTraceElement", StackFrame::toStackTraceElement);
        System.out.println("--");
    }

    private static void safePrintln(StackFrame frame, String field, Function<StackFrame, ?> function){
        try {
            System.out.println(field + ": " + function.apply(frame));
        } catch (Exception exc){
            System.out.println(field + ": " + exc);
        }
    }

    private static <T> Function<T, Void> asFunction(Consumer<? super T> consumer){
        return t -> {
            consumer.accept(t);
            return null;
        };
    }

}
