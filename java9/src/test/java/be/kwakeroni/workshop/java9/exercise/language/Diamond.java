package be.kwakeroni.workshop.java9.exercise.language;

import java.io.Serializable;

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

    /**
     * Use the Diamond operator to reduce the boilerplate.
     */
    static final Factory<?> FACTORY =
            new Factory<StringBuffer>() {
                @Override
                public StringBuffer newInstance() {
                    return new StringBuffer();
                }
            };

    /**
     * Attempt to do the same with the following example.
     */
    static final ComparableFactory<?> COMPARABLE_FACTORY =
            new ComparableFactory<String>() {
                @Override
                public String newInstance() {
                    return "";
                }
            };

}
