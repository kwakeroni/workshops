package be.kwakeroni.workshop.java9.solution.library;

import be.kwakeroni.workshop.java9.Person;
import org.junit.Test;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptionalTest {

    private static final Person person = new Person();
    private static final Optional<String> OPTIONAL = Optional.of("value");
    private static final Optional<String> PRESENT = Optional.of("value");
    private static final Optional<String> ABSENT = Optional.empty();

    @Test
    public void ifPresentOrElse() {
        person.getEmail().ifPresentOrElse(this::sendEmail, () -> this.sendLetter(person.getAddress()));
    }

    /**
     * When Optional values can come from different sources,
     * it's not so easy to take the first one that is present.
     * the {@link Optional#or(Supplier)} method can
     */
    @Test
    public void or() {
        Optional<String> phone =
                person.getCellPhoneNumber()
                        .or(person::getHomePhoneNumber)
                        .or(() -> askUser("please provide phone number"));

        String result = phone.orElse("no phone number");
        assertThat(result).isEqualTo("no phone number");
    }

    /**
     * The {@link Optional#stream()} provides an easy way to convert
     * Optionals to empty or singleton streams.
     */
    @Test
    public void stream() {
        System.out.println("present stream: " + PRESENT.stream().count());
        System.out.println("absent stream: " + ABSENT.stream().count());

        List<String> middleNames = Stream.<Person>of()
                .map(Person::getHomePhoneNumber)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());


    }

    /**
     * The {@link Optional#orElseThrow()} method communicates very
     * clearly that an Exception will be thrown when there is no value.
     */
    @Test
    // Since Java 10
    public void orElseThrow() {
        assertThat(PRESENT.orElseThrow()).isEqualTo("value");
        assertThatThrownBy(() -> ABSENT.orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }


    private void sendEmail(String address) {
        System.out.println("Sending email to: " + address);
    }

    private void sendLetter(String address) {
        System.out.println("Sending letter to: " + address);
    }

    private Optional<String> askUser(String question) {
        return Optional.of(JOptionPane.showInputDialog(question))
                .filter(string -> !string.isEmpty());
    }

}
