package be.kwakeroni.workshop.java9.exercise.library;

import be.kwakeroni.workshop.java9.exercise.Person;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

public class OptionalTest {

    private static final Person person = new Person("Bart", "Simpson")
            .withEmail("bart.simpson@springfield.com")
            .withAddress("Springfield");
    private static final Person granny = new Person("Grand", "mother")
            .withHomePhone("034567891")
            .withAddress("Home");
    private static final Person me = new Person("Me", "")
            .withCellPhone("0498765432")
            .withEmail("me@me.be");

    /**
     * The {@link Optional#ifPresent(Consumer)} method is useful in a functional-programming style,
     * but cannot be used in an if-else scenario.
     * The {@link Optional#ifPresentOrElse(Consumer, Runnable)} method can be used in that case.
     */
    @Test
    public void ifPresentOrElse() {
        EmailService emailService = Mockito.mock(EmailService.class);
        PostOfficeService postOfficeService = Mockito.mock(PostOfficeService.class);

        for (Person p : Arrays.asList(person, granny, me)) {
            Optional<String> email = p.getEmail();
            if (email.isPresent()){
                emailService.sendEmail(email.get());
            } else {
                postOfficeService.sendLetter(p.getAddress());
            }
        }

        verify(emailService).sendEmail("bart.simpson@springfield.com");
        verify(emailService).sendEmail("me@me.be");
        verify(postOfficeService).sendLetter("Home");
    }

    /**
     * When Optional values can come from different sources,
     * it's not so easy to take the first one that is present.
     * The {@link Optional#or(Supplier)} method can help by chaining optional values.
     */
    @Test
    public void or() {
        assertThat(getPhoneNumber(granny)).isEqualTo("034567891");
        assertThat(getPhoneNumber(me)).isEqualTo("0498765432");
        assertThat(getPhoneNumber(person)).isNotNull();
    }

    /*
     * Provides the phone number of the given person.
     * Prefers the home phone number.
     * Selects the cell phone number if that is absent.
     * Requests the phone number to the user if both numbers are unknown.
     */
    private String getPhoneNumber(Person person) {
        Optional<String> number = person.getHomePhoneNumber();
        if (!number.isPresent()){
            number = person.getCellPhoneNumber();
        }
        if (!number.isPresent()){
            number = person.requestPhoneNumber();
        }
        return number.orElse("no phone number");
    }

    /**
     * {@link Optional#stream()} provides an easy way to convert
     * Optionals to empty or singleton streams.
     */
    @Test
    public void stream() {
        List<String> emails = Stream.of(person, granny, me)
                .map(Person::getEmail)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertThat(emails).containsExactlyInAnyOrder("bart.simpson@springfield.com", "me@me.be");
    }

    /**
     * {@link Optional#get} is semantically a bit unclear,
     * because it will throw an exception when there is no value.
     * The {@link Optional#orElseThrow()} method communicates very
     * clearly that an Exception will be thrown when there is no value.
     *
     * @since 10
     */
    @Test
    public void orElseThrow() {
        assertThat(me.getCellPhoneNumber().get()).isEqualTo("0498765432");
        assertThatThrownBy(() -> granny.getCellPhoneNumber().get())
                .isInstanceOf(NoSuchElementException.class);
    }

    private interface EmailService {
        void sendEmail(String emailAddress);
    }

    private interface PostOfficeService {
        void sendLetter(String address);
    }
}
