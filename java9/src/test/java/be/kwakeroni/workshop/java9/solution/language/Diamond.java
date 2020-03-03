package be.kwakeroni.workshop.java9.solution.language;

import java.io.Serializable;

// Examples
//    D1 - Use anonymous inner classes with diamond syntax
//    D2 - Some corner cases are still impossible

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

    public void testInnerFactories() {

        // Use the Diamond operator to reduce the boilerplate.
        var factory = new Factory<>() {
            @Override
            public StringBuffer newInstance() {
                return new StringBuffer();
            }
        };

        // This won't work in the following case.
        // Compare AnonymousFactory and AnonymousComparableFactory below to see why not.
        var comparableFactory = new ComparableFactory<StringBuffer>() {
            @Override
            public StringBuffer newInstance() {
                return new StringBuffer();
            }
        };
    }

    /*
     * The first case is valid in Java 9, because the anonymous class
     * can be compiled using the generic argument 'Serializable' for T
     */
    static class AnonymousFactory extends Factory<Serializable> {
        @Override
        public StringBuffer newInstance() {
            return new StringBuffer();
        }
    }
    // from bytecode: class be.kwakeroni.workshop.java9.solution.language.Diamond$1 extends be.kwakeroni.workshop.java9.solution.language.Diamond$Factory<java.io.Serializable>

    /*
     * The second case is still invalid, because there is no way to specify
     * the generic argument 'Serializable & Comparable<?>' for T.
     *
     * The internal type 'Serializable & Comparable<?>' is called an intersection type.
     * It is a non-denotable type, which means it can't be represented in source code.
     */
//    static class AnonymousComparableFactory extends ComparableFactory<Serializable & Comparable<?>> {
//        @Override
//        public Serializable & Comparable newInstance() {
//            return "";
//        }
//    }
}
