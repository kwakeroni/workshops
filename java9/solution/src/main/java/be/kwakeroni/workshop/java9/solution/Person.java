package be.kwakeroni.workshop.java9.solution;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public class Person {
    private String firstName;
    private String lastName;
    private String middle;
    private String homePhone;
    private String cellPhone;
    private String email;
    private String address;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = "homeless";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getMiddle() {
        return Optional.ofNullable(this.middle);
    }

    public Optional<String> getHomePhoneNumber() {
        return Optional.ofNullable(homePhone);
    }

    public Optional<String> getCellPhoneNumber() {
        return Optional.ofNullable(cellPhone);
    }

    public Optional<String> requestPhoneNumber() {
        if(GraphicsEnvironment.isHeadless()){
            return Optional.empty();
        } else {
            return Optional.of(JOptionPane.showInputDialog("Please provide the phone number of %s %s".formatted(firstName, lastName)))
                    .filter(string -> !string.isBlank());
        }
    }

    public Person withHomePhone(String homePhone){
        this.homePhone = homePhone;
        return this;
    };

    public Person withCellPhone(String cellPhone){
        this.cellPhone = cellPhone;
        return this;
    };

    public Person withEmail(String email){
        this.email = email;
        return this;
    };

    public Person withAddress(String address){
        this.address = Objects.requireNonNullElse(address, "homeless");
        return this;
    }
}
