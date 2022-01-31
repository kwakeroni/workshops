package be.kwakeroni.workshop.java9.exercise.language;

import be.kwakeroni.workshop.java9.exercise.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONTest {

    /**
     * Verify that the serialization of the {@code student} object corresponds to the expected JSON.
     */
    @Test
    public void testSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = new Student("Bart", "Simpson", 8);

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);

        assertThat(json).isEqualTo(
                "{\n" +
                        "  \"firstName\" : \"Bart\",\n" +
                        "  \"lastName\" : \"Simpson\",\n" +
                        "  \"age\" : 8\n" +
                        "}");
    }
}
