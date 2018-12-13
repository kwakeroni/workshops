package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Collections {

    public static Set<String> NAMES_OLD = java.util.Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Tom", "Dick", "Harry")));
    public static Set<String> NAMES = Set.of("Tom", "Dick", "Harry");
    public static List<String> NUMBERS = List.of("one", "two", "three");
    public static Map<Integer, String> NUMBERMAP = Map.of(
            1, "one",
            2, "two",
            3, "three");

    public static void main(String[] args) {

        Collections coll = new Collections();
        coll.testSet();
        coll.testList();
        coll.testMap();
    }

    @Test
    public void testSet() {

        System.out.println(NAMES.getClass() + "" + NAMES);

        // Immutable
        assertThatThrownBy(() -> NAMES.add("Hello"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No Nulls
        assertThatThrownBy(() -> Set.of("one", "two", null, "three"))
                .isInstanceOf(NullPointerException.class);

        // No Duplicates
        assertThatThrownBy(() -> Set.of("left", "right", "left"))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testList() {

        System.out.println(NUMBERS.getClass() + "" + NUMBERS);

        // Immutable
        assertThatThrownBy(() -> NUMBERS.add("four"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No Nulls
        assertThatThrownBy(() -> List.of("Tom", "Dick", null, "Harry"))
                .isInstanceOf(NullPointerException.class);

        // Yes duplicates
        List.of("left", "right", "left");
    }

    @Test
    public void testMap() {

        System.out.println(NUMBERMAP.getClass() + "" + NUMBERMAP);

        // Immutable
        assertThatThrownBy(() -> NUMBERMAP.put(4, "four"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No Null keys
        assertThatThrownBy(() -> Map.of(1, "one", null, "zero", 2, "two"))
                .isInstanceOf(NullPointerException.class);

        // No Null values
        assertThatThrownBy(() -> Map.of(1, "one", 0, null, 2, "two"))
                .isInstanceOf(NullPointerException.class);

        // No duplicate keys
        assertThatThrownBy(() -> Map.of(1, "one", 2, "two", 1, "one again"))
                .isInstanceOf(IllegalArgumentException.class);

        // Yes duplicate values
        Map.of(1, "one", 2, "two", 3, "one");

        // Alternative
        Map.ofEntries(entry(1, "one"), entry(2, "two"));
    }
}
