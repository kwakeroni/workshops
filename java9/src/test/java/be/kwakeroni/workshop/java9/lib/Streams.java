package be.kwakeroni.workshop.java9.lib;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * New methods in Java 9:
 * <ul>
 *     <li>{@link java.util.stream.Stream}
 *      <ul>
 *          <li>{@link Stream#takeWhile(Predicate)}</li>
 *          <li>{@link Stream#dropWhile(Predicate)}</li>
 *          <li>{@link Stream#iterate(Object, Predicate, UnaryOperator)}</li>
 *          <li>{@link Stream#ofNullable(Object)}</li>
 *      </ul>
 *     </li>
 *     <li>{@link java.util.stream.Collectors}
 *     <ul>
 *         <li>{@link Collectors#flatMapping(Function, Collector)}</li>
 *         <li>{@link Collectors#filtering(Predicate, Collector)}</li>
 *     </ul>
 *     </li>
 * </ul>
 */
public class Streams {

    @Test
    public void takeWhileWithElement(){
        AtomicInteger last = new AtomicInteger();
        System.out.print("1-9: ");

        Stream.iterate(1, i->i+1)
                .takeWhile(i -> (i*i + 2 * i - 120) < 0 )
                .forEach(i -> {
                    last.set(i);
                    System.out.print(i + ", ");
                });
        System.out.println();

        Assertions.assertThat(last.get()).isEqualTo(9);
    }

    @Test @Ignore
    public void takeWhileWithoutElement(){
        System.out.print("10 seconds: ");

        long start = System.currentTimeMillis();
        Stream.iterate(1, i->i+1)
                .takeWhile(i -> System.currentTimeMillis()-start < 10000)
                .forEach(i -> {
                    System.out.print(i + ", ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                });
        System.out.println();
    }

    @Test
    public void dropWhile(){
        AtomicInteger last = new AtomicInteger();
        System.out.print("9-1: ");

        IntStream.range(0, 15).map(i -> 15-i)
                .dropWhile(i -> i > 10)
                .forEach(i -> {
                    last.set(i);
                    System.out.print(i + ", ");
                });
        System.out.println();

        Assertions.assertThat(last.get()).isEqualTo(1);

    }

    @Test
    public void iterateWithPredicate(){
        AtomicInteger last = new AtomicInteger();
        System.out.print("1-9: ");

        Stream.iterate(1, i -> (i*i + 2 * i - 120) < 0, i->i+1)
                .forEach(i -> {
                    last.set(i);
                    System.out.print(i + ", ");
                });
        System.out.println();

        Assertions.assertThat(last.get()).isEqualTo(9);
    }

    @Test
    public void ofNullable() {
        println("of(1)", ()-> Stream.of(1).collect(Collectors.toList()));
        println("of(null)", ()->  Stream.of((Object) null).collect(Collectors.toList()));
        println("ofNullable(1)", ()->  Stream.ofNullable(1).collect(Collectors.toList()));
        println("ofNullable(null)", ()->  Stream.ofNullable(null).collect(Collectors.toList()));
    }

    @Test
    public void flatMapping(){
        println("flatMapping-Set",() -> Stream.of("ABC", "ABCDEF", "DEFGHI", "ABCGHIJKL")
                .map(string -> IntStream.range(0, string.length()).mapToObj(string::charAt).collect(Collectors.toList()))
                .collect(Collectors.flatMapping(List::stream, Collectors.toSet())));

        println("flatMap-Set",() -> Stream.of("ABC", "ABCDEF", "DEFGHI", "ABCGHIJKL")
                .map(string -> IntStream.range(0, string.length()).mapToObj(string::charAt).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toSet())
        );

        Map<String, Set<String>> boys = Map.of("R", Set.of("Robert", "Robin", "Rupert"), "S", Set.of("Stephen", "Stewart"));
        Map<String, Set<String>> girls = Map.of("C", Set.of("Cindy", "Chelsea"), "R", Set.of("Rowena", "Robin", "Roberta"));

        println("flatMapping-Map", ()-> Stream.of(boys, girls)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                            Collectors.mapping(Map.Entry::getValue,
                                Collectors.flatMapping(Set::stream, Collectors.toSet())))));

    }

    @Test
    public void filtering(){

        Map<String, Set<String>> boys = Map.of("R", Set.of("Robert", "Robin", "Rupert"), "S", Set.of("Stephen", "Stewart"));
        Map<String, Set<String>> girls = Map.of("C", Set.of("Cindy", "Chelsea"), "R", Set.of("Rowena", "Robin", "Roberta"));

        println("filtering-Map", ()-> Stream.of(boys, girls)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,
                                Collectors.flatMapping(Set::stream, Collectors.filtering(name -> name.contains("e"), Collectors.toSet()))
                        ))));
    }


    private <T> T println(String name, Supplier<T> supplier){
        Object result;
        T tResult;
        try {
            tResult = supplier.get();
            result = tResult;
        } catch (Exception exc){
            tResult = null;
            result = exc;
        }
        System.out.println(name + " : " + result);
        return tResult;
    }
}
