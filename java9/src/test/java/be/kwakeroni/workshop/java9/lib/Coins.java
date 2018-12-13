package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by kwakeroni on 07/11/17.
 */
public class Coins {

    @Test
    public void testSafeVarArgs(){
        use(
                List.of("A", "B"),
                List.of("C", "D"));
    }

    @SafeVarargs
    private List<String>[] use(List<String>... args){
        System.out.println(Arrays.toString(args));
        return args;
    }


    @Test
    public void tryWithResource(){
        List<String>[] args = use(
                List.of("A", "B"),
                List.of("C", "D"));

        try (Stream<String> stream = Arrays.stream(args).flatMap(List::stream)){
            stream.forEach(System.out::println);
        }

        Stream<String> stream2 = Arrays
                .stream(args)
                .flatMap(List::stream);

        try (stream2){
            stream2.forEach(System.out::println);
        }
    }

    @Test
    public void underscore(){
        if (true) {
//            String _ = "_";
        }
//        Consumer<Object> consumer = _ -> System.out.println("Hello");
    }

    @Test
    public <T extends Runnable & Serializable> void genericAnonymous(){
        Foo<?> foo = new Foo<>(){};
//        Bar<?> bar = new Bar<>(){};
    }

    interface Foo<X extends Runnable> {

    }

    interface Bar<X extends Runnable & Serializable>{

    }

    interface WithPrivate {

        String getLoginToken(String credentials);

        default void login(String username, String password){
            String token = getLoginToken(createCredential(username, password));
            if (token == null){
                throw new IllegalArgumentException("Login failed");
            }
        }


        private String createCredential(String username, String password){
            return Base64.getEncoder().encodeToString((username+":"+password).getBytes());
        }
    }
}
