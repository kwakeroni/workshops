package be.kwakeroni.workshop.java9.solution;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().handleFiles(args);
    }

    private final StudentParser parser = new StudentParser();

    private void handleFiles(String[] args) throws Exception {
        if (args.length == 0) {
            handleGroups(
                    parser.parseStudents(Paths.get("group1.csv")),
                    parser.parseStudents(Paths.get("group2.csv")));
        } else {
            handleGroups(parse(args));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Student>[] parse(String[] paths) {
        return Arrays.stream(paths)
                .map(Paths::get)
                .map(this::parse)
                .toArray(List[]::new);
    }

    private List<Student> parse(Path path) {
        try {
            return parser.parseStudents(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @java.lang.SafeVarargs
    private void handleGroups(List<Student>... groups) {
        for (int i = 0; i < groups.length; i++) {
            System.out.println("- Group #%s".formatted(i));
            groups[i].forEach(student -> System.out.println("-- %s %s (aged %s)".formatted(student.lastName(), student.firstName(), student.age())));
        }
    }

}
