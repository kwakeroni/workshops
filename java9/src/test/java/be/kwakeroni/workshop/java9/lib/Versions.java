package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

/**
 * Created by kwakeroni on 07/11/17.
 */
public class Versions {

    @Test
    public void testVersion(){
        Runtime.Version version = Runtime.version();
        String empty = "\"\"";
        System.out.println(String.format("%s.%s security:%s pre:%s build:%s optional:%s",
            version.major(),
                version.minor(),
                version.security(),
                version.pre().orElse(empty),
                version.build().map(Object::toString).orElse(empty),
                version.optional().orElse(empty)
        ));
    }

}
