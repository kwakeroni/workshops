package be.kwakeroni.workshop.java9.exercise.language;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class VarTest {

    /**
     * Fetch the actions from {@link #getActions()} and execute them using {@link #runAndClose(Collection)} .
     * <p>
     * Intersection Types are types implementing multiple interfaces but without an explicit name.
     * These types are non-denotable, meaning they cannot be expressed in the source code explicitly.
     * </p>
     */
    private void getAndRunActions() {
        runAndClose(getActions());
    }

    /**
     * Local variable type inference can also be used in for loops.
     */
    private <CloseableRunnable extends Runnable & AutoCloseable> void runAndClose(Collection<? extends CloseableRunnable> actions) {
        for (CloseableRunnable action : actions) {
            try (CloseableRunnable act = action) {
                act.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressWarnings("unchecked")
    private <CloseableRunnable extends Runnable & AutoCloseable> List<CloseableRunnable> getActions() {
        return Arrays.asList((CloseableRunnable) action1, (CloseableRunnable) action2);
    }

    private final Object action1 = mock(Object.class, withSettings().extraInterfaces(Runnable.class, AutoCloseable.class));
    private final Object action2 = mock(Object.class, withSettings().extraInterfaces(Runnable.class, AutoCloseable.class));

    @Test
    public void testGetRunAndClose() throws Exception {
        getAndRunActions();
        InOrder inOrder = inOrder(action1, action2);
        inOrder.verify((Runnable) action1).run();
        inOrder.verify((AutoCloseable) action1).close();
        inOrder.verify((Runnable) action2).run();
        inOrder.verify((AutoCloseable) action2).close();
    }
}
