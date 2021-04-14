package be.kwakeroni.workshop.java9.exercise.library;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class CollectionsTest {

    /**
     * @return An unmodifiable List containing the values "Bart", "Lisa" and "Maggie".
     */
    private List<String> createSimpsonsList() {
        return Collections.unmodifiableList(Arrays.asList("Bart", "Lisa", "Maggie"));
    }

    /**
     * @return An unmodifiable Set containing the values "Bart", "Lisa" and "Maggie".
     */
    private Set<String> createSimpsonsSet() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Bart", "Lisa", "Maggie")));
    }

    /**
     * @return An unmodifiable Map containing the mappings "Bart":10, "Lisa":8 and "Maggie":1.
     */
    private Map<String, Integer> createSimpsonsMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Bart", 10);
        map.put("Lisa", 8);
        map.put("Maggie", 1);
        return Collections.unmodifiableMap(map);
    }

    /**
     * The {@code Family} class makes a defensive copy of its constructor argument {@code children}.
     */
    private static class Family {
        private final List<String> children;

        public Family(List<String> children) {
            this.children = new ArrayList<>(children);
        }

        public List<String> children() {
            return Collections.unmodifiableList(this.children);
        }
    }

    @Test
    public void testListFactory() {
        List<String> list = createSimpsonsList();

        assertThat(list).containsExactly("Bart", "Lisa", "Maggie");

        assertThatThrownBy(() -> list.add("Homer"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list.set(2, "Marge"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testSetFactory() {
        Set<String> set = createSimpsonsSet();

        assertThat(set).containsExactlyInAnyOrder("Bart", "Lisa", "Maggie");

        assertThatThrownBy(() -> set.add("Homer"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testMapFactory() {
        Map<String, Integer> map = createSimpsonsMap();

        assertThat(map).containsOnly(entry("Bart", 10), entry("Lisa", 8), entry("Maggie", 1));

        assertThatThrownBy(() -> map.put("Marge", 34))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
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
