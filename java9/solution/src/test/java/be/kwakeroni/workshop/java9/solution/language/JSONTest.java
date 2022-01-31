package be.kwakeroni.workshop.java9.solution.language;

import be.kwakeroni.workshop.java9.solution.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static be.kwakeroni.assertj.json.JSONAssertions.assertThatJSON;

public class JSONTest {
    //lxanguage=json
    private static final String JSON = """
            {
              "firstName" : "Bart", \s
              "lastName" : "Simpson",
              "age" : 8
            }
            """;

    private static final String JSON2 = """
            {
              "firstName" : "Pablo Diego José \
            Francisco de Paula \
            Juan Nepomuceno María \
            de los Remedios Cipriano \
            de la Santísima Trinidad",
              "lastName" : "Ruiz y Picasso",
              "age" : 8
            }
            """;
    /**
     * Verify that the serialization of the {@code student} object corresponds to the expected JSON.
     * String blocks can make code more readable because it can eliminate String concatenation, line-breaks and escaped quotes.
     */
    @Test
    public void testSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = new Student("Bart", "Simpson", 8);

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        //language=json
        assertThat(json).isEqualTo("""
                                       {
                                         "firstName" : "Bart",
                                         "lastName" : "Simpson",
                                         "age" : 8
                                       }""");

        System.out.println(JSON2.replaceAll("\\r", "\\\\r").replaceAll("\\n", "\\\\n"));
        System.out.println(JSON2);
        System.out.println("   ".isBlank());
        System.out.println("""
                              \s
                           
                           """.strip());
    }
}
