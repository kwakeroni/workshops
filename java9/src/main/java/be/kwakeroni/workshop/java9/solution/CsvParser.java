package be.kwakeroni.workshop.java9.solution;

import java.util.Arrays;

interface CsvParser<T> {
    void handleHeaderLine(Object[] headers);

    T handleDataLine(Object[] data);

    default void handleHeaderLine(String line) {
        handleHeaderLine(parseLine(line));
    }

    default T handleDataLine(String line) {
        return handleDataLine(parseLine(line));
    }

    private Object[] parseLine(String line) {
        // Start with a failure
        //return line.split(",");
        return Arrays.stream(line.split(","))
                .map(str -> str.matches("\\d+")? Integer.valueOf(str): str)
                .toArray(Object[]::new);
    }
}
