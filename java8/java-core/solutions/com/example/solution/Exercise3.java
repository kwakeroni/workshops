package com.example.solution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions to exercises on Streams, creation of streams and the map-filter-reduce pattern.
 */
public class Exercise3 {
	
	/*
	 * Calculate the sum of all integers from 0 to 100
	 */
	@Test
	public void exercise1(){
	
		int sum = IntStream.range(0, 101)
					       .sum();
		
		assertEquals(5050, sum);
	}

	/*
	 * Calculate the sum of the first 20 squares of 3
	 */
	@Test
	public void exercise2a(){
		
		int sum =  IntStream.iterate(1, i -> i*3)
				             .limit(20)
				             .sum();
		
		assertEquals(1743392200, sum);		
	}
	
	/*
	 * Calculate the sum of the first 20 Fibonacci numbers
	 */
	@Test
	public void exercise2b(){
		IntUnaryOperator op = new IntUnaryOperator(){
			int previous = 0;
			@Override
			public int applyAsInt(int operand) {
				int result = previous + operand;
				previous = operand;
				return result;
			}
		};
		
		int sum = IntStream.iterate(1, op)
				             .limit(20)
				             .sum();
		
		assertEquals(17710, sum);
	}
	
	
	
	/*
	 * Find out how long it takes to generate and sort a list of 5 million random strings.
	 */
	@Test
	public void exercise3() throws Exception {
		long before = System.currentTimeMillis();
		
		Stream<String> stream = ThreadLocalRandom.current()
									.longs().parallel()
									.mapToObj(Long::toHexString)
									.limit(5_000_000)
									.sorted();
		
		Object[] array = stream.toArray();

		long after = System.currentTimeMillis();
		
		assertTrue("Actual time was " + (after - before) + " ms", after - before > 1000);
	}
	
	/*
	 * Filtering streams: Calculate the sum of all odd (not pair) integers in the list
	 */
	@Test
	public void exercise4(){
		int[] ints = {0, 11, 22, 33, 44, 55, 66, 77, 88, 99, 100};
	
		int sum = Arrays.stream(ints)
					.filter(i -> i%2 != 0)
					.sum();

		assertEquals(275, sum);
	}
	
	/*
	 * Mapping streams: Calculate the sum of the numbers in toBeAdded
	 */
	@Test
	public void exercise5(){
		List<String> intNames = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");

		List<String> toBeAdded = Arrays.asList("zero", "three", "six", "nine", "six", "three");
		
		int sum = toBeAdded.stream()
					.mapToInt(intNames::indexOf)
					.sum();
		
		assertEquals(27, sum);
	}
	
	/*
	 * Combine mapping and filtering: Calculate the length of the shortest line in the song
	 */
	@Test
	public void exercise6() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			int length = reader.lines()
							.mapToInt(String::length)
							.filter(l -> l > 0)
							.min().getAsInt();
			
			assertEquals(18, length);	
		}
	}

	/*
	 * Combine mapping and filtering: Calculate the length of the longest line not containing the letter 'y' in the file
	 */
	@Test
	public void exercise7() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			int length = reader.lines()
							.filter(s -> ! s.contains("y"))
							.mapToInt(String::length)
							.max().orElse(0);
			
			assertEquals(32, length);	
		}
	}

	/*
	 * Flattening: Find the longest word in the file 
	 */
	@Test
	public void exercise8() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			String word = reader.lines()
							.flatMap(s -> Arrays.stream(s.split("\\s+")))
							.reduce( BinaryOperator.maxBy(Comparator.comparing(String::length)) )
							.get();
			assertEquals("yesterday...", word);	
		}
		
	}
	
}
