package com.example.exercise;

import java.util.Iterator;

public class Exercise2 {

    public interface Collection<E> {

        int size();

        Iterator<E> iterator();

        boolean add(E e);

        boolean addAll(java.util.Collection<? extends E> c);

    }

}
