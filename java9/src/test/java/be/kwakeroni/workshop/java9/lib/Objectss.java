package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by kwakeroni on 24/05/17.
 */
public class Objectss {

    private static final Object NULL = null;
    private static final String[] ARRAY = {"One", "Two", "Three"};

    @Test
    public void requireNonNullElse(){
        System.out.println(Optional.ofNullable(NULL).orElse("absent"));
        System.out.println(Optional.ofNullable(NULL).orElseGet(this::getDefault));

        System.out.println(java.util.Objects.requireNonNullElse(NULL, "absent"));
        System.out.println(java.util.Objects.requireNonNullElseGet(NULL, this::getDefault));
    }

    @Test
    public void checkIndex(){

        java.util.Objects.checkIndex(2, ARRAY.length);
        assertThatThrownBy(() -> java.util.Objects.checkIndex(3, ARRAY.length))
                .isInstanceOf(IndexOutOfBoundsException.class);

    }

    @Test
    public void checkFromToIndex(){
        java.util.Objects.checkFromToIndex(1, 3, ARRAY.length);

        assertThatThrownBy(() -> java.util.Objects.checkFromToIndex(2,4, ARRAY.length))
                .isInstanceOf(IndexOutOfBoundsException.class);

    }

    @Test
    public void checkFromIndexSize(){
        java.util.Objects.checkFromIndexSize(0, 3, ARRAY.length);
        java.util.Objects.checkFromIndexSize(1, 2, ARRAY.length);

        assertThatThrownBy(() -> java.util.Objects.checkFromIndexSize(1, 3, ARRAY.length))
                .isInstanceOf(IndexOutOfBoundsException.class);


    }


    private String getDefault(){
        return "default";
    }
}
