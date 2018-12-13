package solution.language;

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
            // This was impossible in Java 8.
            // new Factory<>() {
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
            // This is still impossible in Java 9.
            // new ComparableFactory<>() {
            new ComparableFactory<String>() {
                @Override
                public String newInstance() {
                    return "";
                }
            };


    /*
     * The first case is valid in Java 9, because the anonymous class
     * can be compiled using the generic argument 'Serializable' for T
     */
    static class AnonymousFactory extends Factory<Serializable> {
        @Override
        public Serializable newInstance() {
            return new StringBuffer();
        }
    }

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
