package be.kwakeroni.workshop.java9.exercise.language;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SafeVarArgs {

    /**
     * Make the code below compile.
     */
    // private static final String _ = "_";

    /**
     * The test below works perfectly fine, but it has some type-safety warnings.
     * We can prove that the code is type-safe, so we want to get rid of them.
     * Remove both warnings by adding a single annotation.
     */
    @Test
    public void testSafeVarargs(){
        List<String>[] lists = getLengths(
                Arrays.asList("The", "Quick", "Brown", "Fox"),
                Arrays.asList("The", "Lazy", "Dog")
        );

        Assertions.assertThat(lists[0].get(0)).isEqualTo("3");

    }

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
