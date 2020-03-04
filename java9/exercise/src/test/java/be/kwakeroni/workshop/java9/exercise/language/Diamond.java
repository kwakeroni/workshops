package be.kwakeroni.workshop.java9.exercise.language;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The diamond operator <> in Java 8 cannot be used with anonymous inner classes.
 * It can since Java 9.
 */
public class Diamond {

    /**
     * Creates instances of the Serializable type T
     */
    static abstract class Factory<T extends Serializable> {
        public abstract T newInstance();
    }

    /**
     * Creates instances of the type T, which is both Serializable and Comparable
     */
    static abstract class ComparableFactory<T extends Serializable & Comparable<?>> extends Factory<T> {
    }

    // Try to use the diamond operator in the following cases
    public void testInnerFactories() {

        Factory<?> factory = new Factory<Calendar>() {
            @Override
            public Calendar newInstance() {
                return new GregorianCalendar();
            }
        };

        ComparableFactory<?> comparableFactory = new ComparableFactory<Calendar>() {
            @Override
            public Calendar newInstance() {
                return new GregorianCalendar();
            }
        };
    }
}
