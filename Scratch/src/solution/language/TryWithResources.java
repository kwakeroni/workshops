package solution.language;

import org.junit.Test;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;

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

        URL resource = TryWithResources.class.getResource("/balcony.txt");
        InputStreamReader streamReader = new InputStreamReader(resource.openStream());
        LineNumberReader lnReader = new LineNumberReader(streamReader);
        lnReader.setLineNumber(1);

//          Not allowed on Java 8
//        try (lnReader) {
        try (LineNumberReader reader = lnReader) {
            while (reader.ready()) {
                System.out.println(reader.getLineNumber() + ". " + reader.readLine());
            }
        }
    }

}
