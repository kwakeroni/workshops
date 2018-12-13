package be.kwakeroni.workshop.java9.lib;

import java.lang.reflect.Field;

public class Encapsulations {

    public static void main(String[] args) throws Exception {

        // Prints "Hobania" in Java 8

        Field value = String.class.getDeclaredField("value");
        value.setAccessible(true);
        byte[] array = (byte[]) value.get("Tobania");
        array[0] = 'H';

        System.out.println("Tobania");

        System.out.println(Class.forName("sun.misc.Unsafe").newInstance());
    }

}
