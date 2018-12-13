package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by kwakeroni on 24/05/17.
 */
public class Arrayss {

    private static final int[] ODDS = {1, 3, 5, 7, 9};
    private static final int[] PRIMES = {2, 3, 5, 7, 11};
    private static final int[] DIGITS_2_5 = {2, 3, 4, 5};
    private static final int[] DIGITS_2_7 = {2, 3, 4, 5, 6, 7};


    @Test
    public void equals(){
        System.out.println("[3, 5, 7]: " + java.util.Arrays.equals(ODDS, 1, 4, PRIMES, 1, 4));
        System.out.println("[3, 5, 7, 9!=11]: " + java.util.Arrays.equals(ODDS, 1, 5, PRIMES, 1, 5));
    }

    @Test
    public void mismatch(){
        printMismatch(PRIMES, DIGITS_2_5);
        printMismatch(DIGITS_2_5, DIGITS_2_7);
        printMismatch(DIGITS_2_7, DIGITS_2_7);
    }

    private void printMismatch(int[] one, int[] two){
        int mismatch = java.util.Arrays.mismatch(one, two);

        if (mismatch < 0){
            System.out.println(String.format("Matching: %s = %s", java.util.Arrays.toString(one), java.util.Arrays.toString(two)));
        } else {
            String intOne = (mismatch < one.length) ? "" + one[mismatch] : "-";
            String intTwo = (mismatch < two.length) ? "" + two[mismatch] : "-";

            System.out.println(String.format("Mismatch(%s, %s) at %s (%s <> %s)",
                    java.util.Arrays.toString(one), java.util.Arrays.toString(two), mismatch, intOne, intTwo));
        }
    }

    @Test
    public void compare(){
        printCompare(ODDS, PRIMES);
        printCompare(PRIMES, DIGITS_2_5);
        printCompare(DIGITS_2_5, DIGITS_2_7);
    }

    private void printCompare(int[] one, int[] two){
        int mismatch = java.util.Arrays.mismatch(one, two);
        String intOne = (mismatch < one.length)? "" + one[mismatch] : "-";
        String intTwo = (mismatch < two.length)? "" + two[mismatch] : "-";

        System.out.println(String.format("%s %s %s because %s %s %s",
                java.util.Arrays.toString(one), signCompare(one, two), java.util.Arrays.toString(two), intOne, sign(intOne.compareTo(intTwo)), intTwo));
    }

    private String signCompare(int[] one, int[] two) {
        return sign(java.util.Arrays.compare(one, two));
    }
    private String sign(int cmp){
        if (cmp == 0){
            return "==";
        } else if (cmp < 0){
            return "<";
        } else if (cmp > 0){
            return ">";
        }
        throw new RuntimeException("Unexpected result: " + cmp);
    }
}
