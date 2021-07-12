package be.kwakeroni.workshop.java9.solution.library;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.*;

public class CollectionsTest {

    /**
     * The {@link List#of()} methods provide a concise way to create small Lists.
     * These lists cannot be modified and cannot contain {@code null} elements.
     *
     * @return An unmodifiable List containing the values "Bart", "Lisa" and "Maggie".
     */
    private List<String> createSimpsonsList() {
        return List.of("Bart", "Lisa", "Maggie");
    }

    /**
     * The {@link Set#of()} methods provide a concise way to create small Sets.
     * These sets cannot be modified and cannot contain {@code null} elements.
     * The methods will also throw an exception when there are duplicate elements.
     *
     * @return An unmodifiable Set containing the values "Bart", "Lisa" and "Maggie".
     */
    private Set<String> createSimpsonsSet() {
        return Set.of("Bart", "Lisa", "Maggie");
    }

    /**
     * The {@link Map#of()} methods provide a concise way to create small Maps.
     * The {@link Map#ofEntries(Map.Entry[])} method is an alternative.
     * {@link Map#entry(Object, Object)} can be used to create (unmodifiable) entries easily.
     * These maps cannot be modified and cannot contain {@code null} keys, nor {@code null} values.
     * The methods will also throw an exception when there are duplicate keys.
     *
     * @return An unmodifiable Map containing the mappings "Bart":10, "Lisa":8 and "Maggie":1.
     */
    private Map<String, Integer> createSimpsonsMap() {
        return Map.of(
                "Bart", 10,
                "Lisa", 8,
                "Maggie", 1
        );
    }

    /**
     * The {@code Family} class makes a defensive copy of its constructor argument {@code children}.
     * This can be done in a nicer way by using
     * {@link List#copyOf(Collection)}, {@link Set#copyOf(Collection)} and {@link Map#copyOf(Map)},
     * all of which create unmodifiable copies of a Collection or Map.
     *
     * @since 10
     */
    private static class Family {
        private final List<String> children;

        public Family(List<String> children) {
            this.children = List.copyOf(children);
        }

        public List<String> children() {
            return this.children;
        }
    }


    @Test
    @DisplayName("The Simpsons List is immutable")
    public void testListFactory() {
        List<String> list = createSimpsonsList();

        assertThat(list).containsExactly("Bart", "Lisa", "Maggie");

        assertThatThrownBy(() -> list.add("Homer"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list.set(2, "Marge"))
                .isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    @DisplayName("The Simpsons Set is immutable")
    public void testSetFactory() {
        Set<String> set = createSimpsonsSet();

        assertThat(set).containsExactlyInAnyOrder("Bart", "Lisa", "Maggie");

        assertThatThrownBy(() -> set.add("Homer"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("The Simpsons Map is immutable")
    public void testMapFactory() {
        Map<String, Integer> map = createSimpsonsMap();

        assertThat(map).containsOnly(entry("Bart", 10), entry("Lisa", 8), entry("Maggie", 1));

        assertThatThrownBy(() -> map.put("Marge", 34))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("The Simpsons Family makes a defensive copy of its children")
    public void testDefensiveCopy() {

        List<String> children = new ArrayList<String>();
        children.add("Bart");
        children.add("Lisa");

        Family family = new Family(children);

        // Family creates a defensive copy of the values list
        children.add("Maggie");
        assertThat(family.children())
                .describedAs("The Family class does not make a defensive copy of its input list")
                .containsExactly("Bart", "Lisa");

        // The list returned by Family cannot be modified externally
        assertThatCode(() -> family.children().add("Maggie"))
                .describedAs("Expected exception not thrown. (The Family class exposes a modifiable list)")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
