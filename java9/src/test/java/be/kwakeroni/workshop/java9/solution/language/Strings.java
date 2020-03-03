package be.kwakeroni.workshop.java9.solution.language;

import be.kwakeroni.workshop.java9.solution.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.CompactNumberFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Strings {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Student student = new Student("Bart", "Simpson", 8);

    /**
     * @since 13-14 (Preview)
     */
    @Test
    public void testStudentSerialization() throws Exception {
        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(student),
                //language=json
                """
                        {
                            firstName: "Bart",
                            lastName: "Simpson",
                            age: 8
                        }""", false
        );
    }

    /**
     * @since 13-14 (Preview)
     */
    @Test
    public void testSerializationPretty() throws Exception {
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student))
                .isEqualTo(/* language=json */ """
                        {
                          "firstName" : "Bart",
                          "lastName" : "Simpson",
                          "age" : 8
                        }""");
    }

    /**
     * @since 12 (String.indent)
     * @since 13-14 (Preview)
     */
    @Test
    public void testIndent() throws Exception {
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student).indent(2))
                .isEqualTo(/* language=json */ """
                        {
                          "firstName" : "Bart",
                          "lastName" : "Simpson",
                          "age" : 8
                        }
                      """);
    }

    /**
     * @since 14 (preview)
     */
    @Test
    public void testSkipNewline() throws Exception {
        assertThat("""
                TODO Search for a piece of poetry with some very long lines that may need to be wrapped around \
                for readibility and that we would want to wrap over multiple lines but not really
                And with a second line to finish
                """).isEqualTo("TODO Search for a piece of poetry with some very long lines that may need to be wrapped around for readibility and that we would want to wrap over multiple lines but not really\nAnd with a second line to finish\n");
    }

    /**
     * @since 14 (preview)
     */
    @Test
    public void testTrailingSpace() throws Exception {
        assertThat("""
                Some text
                That is \s
                Layed Out
                Squarely\s
                """).isEqualTo("Some text\nThat is  \nLayed Out\nSquarely \n");
    }

    /**
     * @since 13-14 (Preview)
     */
    @Test
    public void testStripIndent() throws Exception {
        assertThat((/* language=json */ """
                        {
                          "firstName" : "Bart",
                          "lastName" : "Simpson",
                          "age" : 8
                        }
                        \
                      """.stripIndent()))
                .isEqualTo(/* language=json */ """
                        {
                          "firstName" : "Bart",
                          "lastName" : "Simpson",
                          "age" : 8
                        }
                        """);
    }

    /**
     * @since 13-14 (preview)
     */
    @Test
    public void testConcatenation() throws Exception {
        String name = "Tobania";
        assertThat("""
                This example
                is used by %s
                to show that
                concatenating
                text blocks
                is not
                an easy matter""".formatted(name))
                .isEqualTo("This example\nis used by Tobania\nto show that\nconcatenating\ntext blocks\nis not\nan easy matter");
    }

    /**
     * @since 11
     */
    @Test
    public void testWhiteSpace() throws Exception {
        String one = "   ";
        String two = "   ";

        assertThat(one).isBlank();
        assertThat(one).isJavaBlank();
        assertThat(one.isBlank()).isTrue();
        assertThat(two).isBlank();
        assertThat(two).isJavaBlank();
        assertThat(two.isBlank()).isTrue();

        String oneX = one + "X";
        String twoX = two + "X";

        assertThat(oneX.trim()).isEqualTo("X");
        assertThat(oneX.strip()).isEqualTo("X");

//        assertThat(twoX.trim()).isEqualTo("X");
        assertThat(twoX.strip()).isEqualTo("X");
    }

    /**
     * @since 11
     */
    @Test
    public void testLines() {
        //language=properties
        String properties = """
                jpa.datasource.url=jdbc:localhost:postgres/mydb
                jpa.datasource.username=postgres
                jpa.datasource.password=postgres
                """;

        assertThat(properties.lines()).containsExactly(
                "jpa.datasource.url=jdbc:localhost:postgres/mydb",
                "jpa.datasource.username=postgres",
                "jpa.datasource.password=postgres"
        );
    }

    /**
     * @since 11
     */
    @Test
    public void testStringRepeat() {
        assertThat("XY".repeat(3)).isEqualTo("XYXYXY");
    }

    /**
     * @since 12
     */
    @Test
    public void testStringTransform() {
        assertThat("get" + "student first name ".trim().transform(this::toCamelCase))
                .isEqualTo("getStudentFirstName");
    }

    /**
     * @since 13-14 (preview)
     */
    @Test
    public void testTranslateEscapes() {
        assertThat("\\\"XY\\\"\\r\\n".translateEscapes())
                .isEqualTo("\"XY\"\r\n");
    }

    @Test
    public void testCompactNumberFormat() {
        assertThat(NumberFormat.getCompactNumberInstance().format(1000)).isEqualTo("1K");
        assertThat(NumberFormat.getCompactNumberInstance().format(15_000)).isEqualTo("15K");
        assertThat(NumberFormat.getCompactNumberInstance().format(1024)).isEqualTo("1K");
        assertThat(NumberFormat.getCompactNumberInstance().format(1_000_000_000_000L)).isEqualTo("1T");
        assertThat(NumberFormat.getCompactNumberInstance().format(1500)).isEqualTo("2K");
        CompactNumberFormat format = (CompactNumberFormat) NumberFormat.getCompactNumberInstance();
        format.setMaximumFractionDigits(1);
        assertThat(format.format(1500)).isEqualTo("1.5K");
    }

    private String toCamelCase(String string){
        if (string == null){
            return null;
        }
        return Arrays.stream(string.split("\\s"))
            .map(this::capitalize)
            .collect(Collectors.joining());

    }

    private String capitalize(String string){
        return (string == null)? null :
                (string.isEmpty())? "" :
                        Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
