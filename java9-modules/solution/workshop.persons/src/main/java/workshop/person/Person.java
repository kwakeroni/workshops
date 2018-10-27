package workshop.person;

import java.time.LocalDate;
import java.util.Set;

public class Person {

    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;

    public Person(String firstName, String lastName, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public static Person BART = new Person("Bart", "Simpson", LocalDate.of(1980, 2, 23));
    public static Person LISA = new Person("Lisa", "Simpson", LocalDate.of(1982, 5, 9));
    public static Person MAGGIE = new Person("Maggie", "Simpson", LocalDate.of(1989, 1, 12));
    public static Person MARGE = new Person("Marge", "Simpson", LocalDate.of(1956, 10, 1));
    public static Person HOMER = new Person("Homer", "Simpson", LocalDate.of(1956, 5, 12));

    // Make sure we're compiling on Java9
    static final Set<Person> PERSONS = Set.of(BART, LISA, MAGGIE, MARGE, HOMER);
}
