package be.kwakeroni.workshop.java9.exercise.language;

import org.junit.Test;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class TryWithResources {

    /**
     * The method below uses the try-with-resources construct
     * to print each line in a file with its line number.
     * <p>
     * Move the definition of the 'reader' before the try
     * to improve readability.
     */
    @Test
    public void test() throws Exception {
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(
                        TryWithResources.class.getResource("/balcony.txt").openStream()))) {
            reader.setLineNumber(1);

            while (reader.ready()) {
                System.out.println(reader.getLineNumber() + ". " + reader.readLine());
            }
        }
    }

}
