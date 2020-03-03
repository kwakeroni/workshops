package be.kwakeroni.workshop.java9.solution.language;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @since 14 (preview)
 */
public class TypePatterns {

    private final Collection<String> collection = Set.of("A", "B", "C");

    @Test
    public void testIfThenElse() {
        if (collection instanceof List<String> list){
            assertThat(list.get(0)).isEqualTo("A");
        } else {
            assertThat(collection.iterator().next()).isSubstringOf("ABC");
        }
    }

    @Test
    public void testIfNotThenElse() {
        if (! (collection instanceof List<String> list)){
            assertThat(collection.iterator().next()).isSubstringOf("ABC");
        } else {
            assertThat(list.get(0)).isEqualTo("A");
        }
    }

    @Test
    public void testIfWithoutElse() {
        String first;
        if (! (collection instanceof List<String> list)){
            assertThat(collection.iterator().next()).isSubstringOf("ABC");
            return;
        }

        assertThat(list.get(0)).isEqualTo("A");
    }

    @Test
    public void testTernary() {
        String first = (collection instanceof List<String> list)? list.get(0) : collection.iterator().next();
        assertThat(first).isSubstringOf("ABC");
    }

    @Test
    public void testCondition() {
        if (collection instanceof List<String> list
                && "A".equals(list.get(0))
                || "A".equals(collection.iterator().next())){
            System.out.println("First element is A");
        }
    }
}
