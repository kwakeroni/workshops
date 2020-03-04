package be.kwakeroni.workshop.java9.solution;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Student(
        @JsonProperty String firstName,
        @JsonProperty String lastName,
        @JsonProperty Integer age) {

}
