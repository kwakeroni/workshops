package be.kwakeroni.workshop.java9.solution.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {

    /**
     * A Fermat number is calculated as: {@code F(i) = 2^(2^i) + 1}.
     * The first few Fermat numbers are prime, the rest are not:
     * (prime), (prime), ..., (non-prime), (non-prime), ...
     */
    private BigInteger getFermatNumber(int index) {
        var pow2i = TWO.pow(index).intValueExact();
        var pow2pow2i = TWO.pow(pow2i);
        return pow2pow2i.add(ONE);
    }

    /**
     * @return The first part of the Fermat number series, all of which are prime.
     */
    private Stream<BigInteger> getPrimeFermatNumbers() {
        return IntStream.iterate(0, i -> i + 1)
                .peek(i -> System.out.printf("F(%s)%n", i))
                .mapToObj(this::getFermatNumber)
                .takeWhile(this::isPrime);
    }

    /**
     * @return The remaining part of the Fermat number series (after the prime numbers), which are not prime.
     */
    private Stream<BigInteger> getNonPrimeFermatNumbers() {
        return IntStream.range(0, 16)
                .peek(i -> System.out.printf("Checking F(%s)%n", i))
                .mapToObj(this::getFermatNumber)
                .dropWhile(this::isPrime);
    }

    private boolean isPrime(BigInteger value) {
        System.out.print("Checking if prime: ");
        boolean result = value.isProbablePrime(16);
        System.out.println(result);
        return result;
    }

    /**
     * Maps a Stream of Integers to a Stream of Strings,
     * so that each integer {@code i} is replaced with the following Strings: <ul>
     * <li>if {@code i} is a multiple of {@code 3}: {@code "Fizz"} and a String representing {@code i}</li>
     * <li>if {@code i} is a multiple of {@code 5}: {@code "Buzz"} and a String representing {@code i}</li>
     * <li>if {@code i} is a multiple of both {@code 3} and {@code 5}: {@code "Fizz"}, {@code "Buzz"} and a String representing {@code i}</li>
     * <li>otherwise: a single String representing {@code i}</li>
     * </ul>
     * <p>
     * Example: a Stream containing {@code [1, 3, 5, 7]}  would be mapped to a Stream with
     * {@code ["1", "Fizz", "3", "Buzz", "5", "7"]}.
     * </p>
     *
     * @param intStream a Stream of Integers
     * @return a Stream of Strings, with {@code "Fizz"} and {@code "Buzz"} in the appropriate places
     */
    private Stream<String> mapFizzBuzzStream(Stream<Integer> intStream) {
        return intStream.mapMulti((i, downstream) -> {
            if (i % 3 == 0) {
                downstream.accept("Fizz");
            }
            if (i % 5 == 0) {
                downstream.accept("Buzz");
            }
            downstream.accept(String.valueOf(i));
        });
    }

    @Test
    @DisplayName("Retrieves the initial, prime series of Fermat numbers")
    public void testPrimeFermatNumbers() {
        assertThat(getPrimeFermatNumbers()
                .map(BigInteger::toString))
                .containsExactly("3", "5", "17", "257", "65537");
    }

    @Test
    @DisplayName("Retrieves the remaining, non-prime series of Fermat numbers")
    public void testNonPrimeFermatNumbers() {
        assertThat(getNonPrimeFermatNumbers()
                .map(BigInteger::toString)
                .toList()
                .subList(0, 5))
                .containsExactly(
                        "4294967297",
                        "18446744073709551617",
                        "340282366920938463463374607431768211457",
                        "115792089237316195423570985008687907853269984665640564039457584007913129639937",
                        "13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084097");
    }

    @Test
    @DisplayName("Inserts Fizz and Buzz in the appropriate places")
    public void testMapMulti() {
        List<String> l = this.mapFizzBuzzStream(Stream.iterate(1, i -> i <= 20, i -> i + 1)).toList();
        assertThat(l).containsExactly("1", "2", "Fizz", "3", "4", "Buzz", "5",
                "Fizz", "6", "7", "8", "Fizz", "9", "Buzz", "10",
                "11", "Fizz", "12", "13", "14", "Fizz", "Buzz", "15",
                "16", "17", "Fizz", "18", "19", "Buzz", "20");
    }
}
