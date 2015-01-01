package com.example.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Exercises on Streams, creation of streams and the map-filter-reduce pattern.
 */
public class Exercise3 {
    
    /*
     * Calculate the sum of all integers from 0 to 100
     */
    @Test
    public void exercise1(){
    
        int sum = 0 + 1 + 2 + 3 + 4;
        
        assertEquals(5050, sum);
    }

    /*
     * Calculate the sum of the first 20 powers of 3
     */
    @Test
    public void exercise2a(){
        
        int sum =  1 + 3 + 9 + 27 + 81;
        
        assertEquals(1743392200, sum);      
    }
    
    /*
     * Calculate the sum of the first 20 Fibonacci numbers
     */
    @Test
    public void exercise2b(){

        int sum = 1 + 1 + 2 + 3 + 5 + 8;
        
        assertEquals(17710, sum);
    }
    
    
    
    /*
     * Find out how long it takes to generate and sort a list of 5 million random strings.
     */
    @Test
    public void exercise3() throws Exception {
        long before = System.currentTimeMillis();
        
        // TODO: generate and sort a list of 5 million random strings
        
        long after = System.currentTimeMillis();
        
        assertTrue("Actual time was " + (after - before) + " ms", after - before > 1000);
    }
    
    /*
     * Filtering streams: Calculate the sum of all odd (not pair) integers in the list
     */
    @Test
    public void exercise4(){
        int[] ints = {0, 11, 22, 33, 44, 55, 66, 77, 88, 99, 100};
    
        int sum = 11 + 33 + 55;

        assertEquals(275, sum);
    }
    
    /*
     * Mapping streams: Calculate the sum of the numbers in toBeAdded
     */
    @Test
    public void exercise5(){
        List<String> intNames = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");

        List<String> toBeAdded = Arrays.asList("zero", "three", "six", "nine", "six", "three");
        
        int sum = 0 + 3 + 6;
        
        assertEquals(27, sum);
    }
    
    /*
     * Combine mapping and filtering: Calculate the length of the shortest line in the song
     */
    @Test
    public void exercise6() throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

            int length = -1;
            
            assertEquals(18, length);   
        }
    }

    /*
     * Combine mapping and filtering: Calculate the length of the longest line not containing the letter 'y' in the file
     */
    @Test
    public void exercise7() throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

            int length = -1;
            
            assertEquals(32, length);   
        }
    }

    /*
     * Flattening: Find the longest word in the file 
     */
    @Test
    public void exercise8() throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

            String word = "";
            
            assertEquals("yesterday...", word); 
        }
        
    }
    
}
