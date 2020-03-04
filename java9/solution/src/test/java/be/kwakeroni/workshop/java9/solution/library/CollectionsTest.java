package be.kwakeroni.workshop.java9.solution.library;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CollectionsTest {

    /**
     * Use another way to create a small immutable Set of non-null values.
     */
    @Test
    public void testSetFactory() {
        Set<String> set = Set.of("one", "two", "three");

        assertThat(set).containsExactlyInAnyOrder("one", "two", "three");

        // Immutable
        assertThatThrownBy(() -> set.add("four"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null values
        assertThatThrownBy(() -> Set.of(null, "two", "three"))
                .isInstanceOf(NullPointerException.class);

        // No duplicate values in Set
        assertThatThrownBy(() -> Set.of("one", "two", "one"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Use another way to create a small immutable List of non-null values.
     */
    @Test
    public void testListFactory() {
        List<String> list = List.of("one", "two", "three");

        assertThat(list).containsExactly("one", "two", "three");

        // Immutable
        assertThatThrownBy(() -> list.add("four"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list.set(2, "TWO"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null values
        assertThatThrownBy(() -> List.of(null, "two", "three"))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Use another way to create a small immutable Map with non-null keys and values.
     */
    @Test
    public void testMapFactory() {
        Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
        Map<String, Integer> largeMap = Map.ofEntries(entry("one", 1), entry("ten", 10), entry("twenty", 20));

        assertThat(map).containsOnly(entry("one", 1), entry("two", 2), entry("three", 3));

        // Immutable
        assertThatThrownBy(() -> map.put("four", 4))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null keys
        assertThatThrownBy(() -> Map.of(null, 0, "two", 2, "three", 3))
                .isInstanceOf(NullPointerException.class);

        // No null values
        assertThatThrownBy(() -> Map.of("null", null, "two", 2, "three", 3))
                .isInstanceOf(NullPointerException.class);

        // No duplicate keys
        assertThatThrownBy(() -> Map.of("one", 1, "two", 2, "one", 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Making a defensive copy can be done nicer by using
     * {@link List#copyOf(Collection)}, {@link Set#copyOf(Collection)} and {@link Map#copyOf(Map)},
     * all of which create immutable copies of a Collection or Map.
     * @since 10
     */
    @Test
    public void testDefensiveCopy() {

        /*
         * MyBean provides a List of values.
         */
        class MyBean {

            private final List<String> values;

            public MyBean(List<String> values){
                this.values = List.copyOf(values);
            }

            public List<String> getValues(){
                return this.values;
            }

        }

        List myValues = new ArrayList();
        myValues.add("one");
        myValues.add("two");

        MyBean myBean = new MyBean(myValues);

        // MyBean creates a defensive copy of the values list
        myValues.add("three");
        assertThat(myBean.getValues()).containsExactly("one", "two");

        // The list returned by MyBean cannot be modified externally
        assertThatThrownBy(() -> myBean.getValues().add("three"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
