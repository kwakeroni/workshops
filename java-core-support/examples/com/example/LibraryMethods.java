package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryMethods {

	/**
	 * 
	 * @see java.lang.Iterable#forEach(java.util.function.Consumer)
	 */
	@Test
	public void testForEach(){
		StringBuilder builder = new StringBuilder();
		
		// Append all items in the following list to the String builder;
		List<String> strings = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");

		strings.forEach(s -> { builder.append(s); });
		
		assertEquals("AlphaBetaGammaDelta", builder.toString());
	}
	
	
	/**
	 * @see List#replaceAll(java.util.function.UnaryOperator)
	 */
	@Test
	public void testReplace(){
		List<String> strings = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");
		
		// Replace all Strings with uppercase
		strings.replaceAll(s -> s.toUpperCase());
		
		assertEquals(Arrays.asList("ALPHA", "BETA", "GAMMA", "DELTA"), strings);
	}

	
	/**
	 * @see java.util.Collection#removeIf(java.util.function.Predicate)
	 */
	@Test
	public void testRemoveIf(){
		// Print all items in the following list to System.out
		List<String> strings = new java.util.ArrayList<>(Arrays.asList("Alpha", "Beta", "Gamma", "Delta"));

		strings.removeIf(s -> s.length() < 5);
		
		assertEquals("Alpha", strings.get(0));
		assertEquals("Gamma", strings.get(1));
		assertEquals("Delta", strings.get(2));
	}

	/**
	 * @see Map#forEach(java.util.function.BiConsumer)
	 */
	@Test
	public void testForEachMap(){
		List<Integer> keys = new java.util.ArrayList<>(5);
		List<String> values = new java.util.ArrayList<>(5);
		
		// Produce two lists where each key-value pair matches by index.
		Map<Integer, String> map = new java.util.HashMap<>(5);
		map.put(1, "one");
		map.put(2, "two");
		map.put(3, "three");
		
		map.forEach((key, value) -> {
			keys.add(key);
			values.add(value);
		});
		
		assertEquals(1, keys.get(0).intValue());
		assertEquals("one", values.get(0));
		assertEquals(2, keys.get(1).intValue());
		assertEquals("two", values.get(1));
		assertEquals(3, keys.get(2).intValue());
		assertEquals("three", values.get(2));
	}
	
	@Test
	public void computeIfAbsent(){
		Map<Integer, List<Integer>> modulo = new java.util.HashMap<>();
		
		// Fill the map with all integers between 0 and 100 modulo 10
		for (int i=0; i<=100; i++){
			modulo.computeIfAbsent(i%10, n -> new ArrayList<>()).add(i);
		}
		
		System.out.println(modulo);
	}


	/**
	 * @see List#sort(Comparator)
	 * @see Comparator#comparing(Function)
	 */
	@Test
	public void testSortSimple(){
		String[] intNames = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
		List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		// Sort the integers alphabetically by name
		//ints.sort(Comparator.comparing(i -> intNames[i]));
		ints.sort(Comparator.<Integer, String> comparing(i -> intNames[i]));
		
		System.out.println(ints);
		
		assertEquals(Arrays.asList(8, 5, 4, 9, 1, 7, 6, 10, 3, 2, 0), ints);
	}
	
	/**
	 * @see Comparator#thenComparing(Function)
	 */
	@Test
	public void testSortComposed(){
		List<String> strings = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");
		
		// Sort the strings first according to their length, then alphabetically
		strings.sort(Comparator.comparing(String::length)
				               .thenComparing(Function.identity()));
		
		System.out.println(strings);
		
		assertEquals(Arrays.asList("one", "six", "ten", "two", "five", "four", "nine", "zero", "eight", "seven", "three"), strings);
	}
	
	
	/**
	 * @see Comparator#nullsFirst(Comparator)
	 * @see Comparator#nullsLast(Comparator)
	 */
	@Test
	public void testSortWithNulls(){
		String[] intNames = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
		List<Integer> ints = Arrays.asList(null, 1, 2, 3, null, 5, 6, 7, null, 9, 10);
		
		// Sort the integers alphabetically by name, with nulls coming at the end
		//ints.sort(Comparator.nullsLast(Comparator.comparing(i -> intNames[i])));
		ints.sort(Comparator.nullsLast(Comparator.<Integer, String> comparing(i -> intNames[i])));
		
		System.out.println(ints);
		
		assertEquals(Arrays.asList(5, 9, 1, 7, 6, 10, 3, 2, null, null, null), ints);
	}

}

