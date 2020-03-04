package be.kwakeroni.workshop.java9.exercise;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class SmallFeatures {

    public void treatGroups() throws IOException {
        treatGroups(
                parseStudents(Paths.get("group1.csv")),
                parseStudents(Paths.get("group2.csv")));
    }

    private List<Student> parseStudents(Path path) throws IOException {
        CsvParser<Student> parser = new CsvParser<Student>() {
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

        LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(path));
        try (LineNumberReader r = reader) {
            parser.handleHeaderLine(reader.readLine());
            return reader.lines()
                    .map(parser::handleDataLine)
                    .collect(Collectors.toList());
        } catch (Exception exc) {
            System.err.println(String.format("Parsing error on line %s", reader.getLineNumber()));
            throw exc;
        }
    }

    @SafeVarargs
    private final void treatGroups(List<Student>... groups) {
        for (int i=0; i<groups.length; i++){
            System.out.println(String.format("- Group #%s", i));
            groups[i].forEach(student -> System.out.println(String.format("-- %s %s (aged %s)", student.getLastName(), student.getFirstName(), student.getAge())));
        }
    }

}

