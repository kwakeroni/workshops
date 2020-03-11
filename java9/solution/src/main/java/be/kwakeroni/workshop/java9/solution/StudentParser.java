package be.kwakeroni.workshop.java9.solution;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class StudentParser {

    public List<Student> parseStudents(Path path) throws IOException {
        CsvParser<Student> parser = new CsvParser<>() {
            EnumMap<StudentProperty, Integer> columns = new EnumMap<StudentProperty, Integer>(StudentProperty.class);

            @Override
            public void handleHeaderLine(Object[] headers) {
                for (int i = 0; i < headers.length; i++) {
                    columns.put(StudentProperty.ofHeader(headers[i].toString()), i);
                }
            }

            @Override
            public Student handleDataLine(Object[] data) {
                return new Student(
                        (String) data[columns.get(StudentProperty.FIRST_NAME)],
                        (String) data[columns.get(StudentProperty.LAST_NAME)],
                        (Integer) data[columns.get(StudentProperty.AGE)]
                        );
            }
        };

        var reader = new LineNumberReader(Files.newBufferedReader(path));

        try (reader) {
            parser.handleHeaderLine(reader.readLine());
            return reader.lines()
                    .map(parser::handleDataLine)
                    .collect(Collectors.toList());
        } catch (Exception exc) {
            System.err.println(String.format("Parsing error on line %s", reader.getLineNumber()));
            throw exc;
        }
    }


    //    interface Logger {
//        void debug(String message, Throwable error);
//        void error(String message, Throwable error);
//
//        default void debug(String message){
//            debug(message, (Throwable) null);
//        }
//
//        default void error(String message){
//            error(message, (Throwable) null);
//        }
//
//        default void debug(String message, Object... params){
//            debug(formatted(message, params), throwableFrom(params));
//        }
//
//        default void error(String message, Object... params){
//            debug(formatted(message, params), throwableFrom(params));
//        }
//
//        private String formatted(String message, Object... params){
//            return String.format(message, params);
//        }
//
//        private Throwable throwableFrom(Object... params){
//            Object lastParam = params[params.length-1];
//            if (lastParam instanceof Throwable){
//                return (Throwable) lastParam;
//            } else {
//                return null;
//            }
//        }
//    }

}

