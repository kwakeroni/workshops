package be.kwakeroni.workshop.java9.exercise.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PoetryTest {

    /**
     * Fill in a name, an item and a smell in this classic piece of poetry.
     *
     * @param name  The name of a beloved
     * @param item  An object that we call by a name
     * @param smell The smell of said object
     * @return a piece of poetry
     */
    private String makePoetry(String name, String item, String smell) {
        return "What’s in a name? that which we call a " + item + "\n" +
               "By any other name would smell as " + smell + ";\n" +
               "So " + name + " would, were he not " + name + " call’d,\n" +
               "Retain that dear perfection which he owes\n" +
               "Without that title.\n";
    }

    /**
     * Return a piece of cryptic poetry.
     */
    private String getCrypticPoetry() {
        return "4oCZVHdhcyBicmlsbGlnLCBhbmQgdGhlIHNsaXRoeSB0b3ZlcwpEaWQgZ3lyZSBh" +
               "bmQgZ2ltYmxlIGluIHRoZSB3YWJlOgpBbGwgbWltc3kgd2VyZSB0aGUgYm9yb2dv" +
               "dmVzLApBbmQgdGhlIG1vbWUgcmF0aHMgb3V0Z3JhYmUu";
    }

    /**
     * Frame the given {@code poem} in a nice rectangle of {@code -} and {@code |} characters.
     */
    private String framePoetry(String poem) {
        String newline = System.lineSeparator();
        String[] lines = poem.split("\\v");

        int longestLineLength = Arrays.stream(lines).mapToInt(String::length).max().orElse(0);
        StringBuilder horizontalBorder = new StringBuilder("----");
        for (int i = 0; i < longestLineLength; i++) {
            horizontalBorder.append('-');
        }

        return Arrays.stream(lines)
                .map(line -> padWithSpaces(line, longestLineLength))
                .map(line -> "| " + line + " |")
                .collect(Collectors.joining(
                        newline,
                        horizontalBorder + newline,
                        newline + horizontalBorder));
    }

    /**
     * Pads the given {@code line} with space characters at the end to obtain a String with the given {@code length}.
     */
    private String padWithSpaces(String line, int length) {
        StringBuilder padded = new StringBuilder(line);
        for (int i = line.length(); i < length; i++) {
            padded.append(' ');
        }
        return padded.toString();
    }

    @Test
    @DisplayName("Fills in parameters in template to create poetry")
    public void testMakePoetry() {
        String poem = makePoetry("Romeo", "rose", "sweet");
        System.out.println(poem);
        assertThat(poem)
                .isEqualTo("What’s in a name? that which we call a rose\nBy any other name would smell as sweet;\nSo Romeo would, were he not Romeo call’d,\nRetain that dear perfection which he owes\nWithout that title.\n");
    }

    @Test
    @DisplayName("Decodes cryptic poetry")
    public void testDecodeCrypticPoetry() {
        String crypticPoetry = getCrypticPoetry();
        String decodedCrypticPoetry = new String(Base64.getDecoder().decode(crypticPoetry.getBytes()));
        System.out.println(decodedCrypticPoetry);

        assertThat(decodedCrypticPoetry)
                .isEqualTo("’Twas brillig, and the slithy toves\nDid gyre and gimble in the wabe:\nAll mimsy were the borogoves,\nAnd the mome raths outgrabe.");
    }

    @Test
    @DisplayName("Can frame a poem")
    public void testFramePoem() {
        String poem = "the first cold shower\neven the monkey seems to want\na little coat of straw";

        String framed = framePoetry(poem);
        System.out.println(framed);

        assertThat(framed).isEqualTo("---------------------------------\n| the first cold shower         |\n| even the monkey seems to want |\n| a little coat of straw        |\n---------------------------------");
    }
}
