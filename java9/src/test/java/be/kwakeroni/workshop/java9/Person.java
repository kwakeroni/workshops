package be.kwakeroni.workshop.java9;

import java.util.Optional;

public class Person {
    private String firstName;
    private String lastName;
    private String middle;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return null;
    }

    public Optional<String> getEmail() {
        return Optional.empty();
    }

    public Optional<String> getMiddle() {
        return Optional.ofNullable(this.middle);
    }

    public Optional<String> getHomePhoneNumber() {
        return Optional.empty();
    }

    public Optional<String> getCellPhoneNumber() {
        return Optional.empty();
    }

}
