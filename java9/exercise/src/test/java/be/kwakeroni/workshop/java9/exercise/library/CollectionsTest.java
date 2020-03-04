package be.kwakeroni.workshop.java9.exercise.library;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class CollectionsTest {

    /**
     * Use a nicer way to create a small immutable Set of non-null values.
     */
    @Test
    public void testSetFactory() {
        Set<String> set = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("one", "two", "three")));

        assertThat(set).containsExactlyInAnyOrder("one", "two", "three");

        // Immutable
        assertThatThrownBy(() -> set.add("four"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null values
//        assertThatThrownBy(() -> new HashSet<>(Arrays.asList(null, "two", "three")))
//                .isInstanceOf(NullPointerException.class);

        // No duplicate values in Set
//        assertThatThrownBy(() -> new HashSet<>(Arrays.asList("one", "two", "one")))
//                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Use a nicer way to create a small immutable List of non-null values.
     */
    @Test
    public void testListFactory() {
        List<String> list = Collections.unmodifiableList(Arrays.asList("one", "two", "three"));

        assertThat(list).containsExactly("one", "two", "three");

        // Immutable
        assertThatThrownBy(() -> list.add("four"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list.set(2, "TWO"))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null values
//        assertThatThrownBy(() -> Arrays.asList(null, "two", "three"))
//                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Use a nicer way to create a small immutable Map with non-null keys and values.
     */
    @Test
    public void testMapFactory() {
        Map<String, Integer> mmap = new HashMap<>();
        mmap.put("one", 1);
        mmap.put("two", 2);
        mmap.put("three", 3);
        Map<String, Integer> map = Collections.unmodifiableMap(mmap);

        assertThat(map).containsOnly(entry("one", 1), entry("two", 2), entry("three", 3));

        // Immutable
        assertThatThrownBy(() -> map.put("four", 4))
                .isInstanceOf(UnsupportedOperationException.class);

        // No null keys
//        assertThatThrownBy(() -> {
//            Map<String, Integer> m = new HashMap<>();
//            m.put(null, 0);
//            m.put("two", 2);
//            m.put("three", 3);
//        }).isInstanceOf(NullPointerException.class);

        // No null values
//        assertThatThrownBy(() -> {
//            Map<String, Integer> m = new HashMap<>();
//            m.put("null", null);
//            m.put("two", 2);
//            m.put("three", 3);
//        }).isInstanceOf(NullPointerException.class);

        // No duplicate keys
//        assertThatThrownBy(() -> {
//            Map<String, Integer> m = new HashMap<>();
//            m.put("one", 1);
//            m.put("two", 2);
//            m.put("one", 1);
//        }).isInstanceOf(IllegalArgumentException.class);
    }


    /**
     * Find a nicer way to make a defensive copy of a given collection.
     */
    @Test
    public void testDefensiveCopy() {

        /*
         * MyBean provides a List of values.
         */
        class MyBean {

            private final List<String> values;

            public MyBean(List<String> values){
                this.values = Collections.unmodifiableList(new ArrayList<>(values));
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
