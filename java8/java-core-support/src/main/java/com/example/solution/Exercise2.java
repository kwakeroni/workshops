package com.example.solution;

import java.util.Iterator;
import java.util.function.Consumer;

public class Exercise2 {

    public interface Collection<E> {

        int size();

        Iterator<E> iterator();

        boolean add(E e);

        boolean addAll(java.util.Collection<? extends E> c);

        default void for_each(Consumer<E> action) {
            iterator().forEachRemaining(action);
        }

    }
}
