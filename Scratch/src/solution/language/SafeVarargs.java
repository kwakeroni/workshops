package solution.language;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SafeVarargs {

    /**
     * Make the code below compile.
     *
     * "Underscore" is no longer a valid identifier.
     * It will make its reappearance soon in another role.
     */
    // Still valid in Java 8
    private static final String _ = "_";


    /**
     * The test below works perfectly fine, but it has some type-safety warnings.
     * We can prove that the code is type-safe, so we want to get rid of them.
     * Remove both warnings by adding a single annotation.
     *
     * The @SafeVarArgs annotation is now allowed on private methods.
     * It suffices to place the annotation on the getLengths method.
     */
    @Test
    public void testSafeVarargs(){
        List<String>[] lists = getLengths(
                Arrays.asList("The", "Quick", "Brown", "Fox"),
                Arrays.asList("The", "Lazy", "Dog")
        );

        Assertions.assertThat(lists[0].get(0)).isEqualTo("3");

    }

    // Not allowed on Java 8
    // @java.lang.SafeVarargs
    private List<String>[] getLengths(List<String>... lists){
        Object[] array = Objects.requireNonNull(lists);

        for (int i=0; i<lists.length; i++){
            List<String> lengths = new ArrayList<>();
            List<String> strings = lists[i];

            for (String string : strings){
                lengths.add(String.valueOf(string.length()));
            }

            array[i] = lengths;
        }

        return lists;
    }

}
