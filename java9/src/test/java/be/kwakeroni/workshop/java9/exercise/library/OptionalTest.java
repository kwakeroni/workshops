package be.kwakeroni.workshop.java9.exercise.library;

import be.kwakeroni.workshop.java9.Person;
import org.junit.Test;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptionalTest {

    private static final Person person = new Person();
    private static final Optional<String> OPTIONAL = Optional.of("value");
    private static final Optional<String> PRESENT = Optional.of("value");
    private static final Optional<String> ABSENT = Optional.empty();

    /**
     * Use a nicer way to write the if-then-else below
     */
    @Test
    public void ifPresentOrElse() {
        if (person.getEmail().isPresent()) {
            sendEmail(person.getEmail().get());
        } else {
            sendLetter(person.getAddress());
        }
    }

    /**
     * When Optional values can come from different sources,
     * it's not so easy to take the first one that is present.
     * Use a nicer way to write the optional chain below.
     */
    @Test
    public void or() {
        Optional<String> phone = person.getCellPhoneNumber();
        if (! phone.isPresent()) {
            phone = person.getHomePhoneNumber();
        }
        if (!phone.isPresent()) {
            phone = askUser("Please provide phone number");
        }
        String result = phone.orElse("no phone number");
        assertThat(result).isEqualTo("no phone number");
    }


    /**
     * Use a nicer way to find all 'home' phone numbers.
     */
    @Test
    public void stream(){
        List<String> middleNames = Stream.<Person> of()
                .map(Person::getHomePhoneNumber)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * The {@link Optional#get()} method is disliked because it
     * hides the fact that it may throw an exception.
     * What method can you use to communicate this more clearly ?
     * @since 10
     */
    @Test
    // Since Java 10
    public void orElseThrow() {
        assertThat(PRESENT.get()).isEqualTo("value");
        assertThatThrownBy(() -> ABSENT.get())
                .isInstanceOf(NoSuchElementException.class);
    }

    private void sendEmail(String address) {
        System.out.println("Sending email to: " + address);
    }

    private void sendLetter(String address) {
        System.out.println("Sending letter to: " + address);
    }

    private Optional<String> askUser(String question){
        return Optional.of(JOptionPane.showInputDialog(question))
                .filter(string -> ! string.isEmpty());
    }
}
