package be.kwakeroni.workshop.java9.exercise;

import java.util.Arrays;

interface CsvParser<T> {
    void handleHeaderLine(Object[] headers);

    T handleDataLine(Object[] data);

    default void handleHeaderLine(String line) {
        Object[] data = Arrays.stream(line.split(","))
                .map(str -> str.matches("\\d+")? Integer.valueOf(str): str)
                .toArray(Object[]::new);
        handleHeaderLine(data);
    }

    default T handleDataLine(String line) {
        Object[] data = Arrays.stream(line.split(","))
                .map(str -> str.matches("\\d+")? Integer.valueOf(str): str)
                .toArray(Object[]::new);
        return handleDataLine(data);
    }
}
