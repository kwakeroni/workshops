package com.example.exercise;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import support.Bag;

/**
 * Exercises on Default Methods and new Java core API methods.
 */
public class Exercise2 {

	
	/*
	 * Add a forEach method to the Collection interface (defined below) and
	 * rewrite the following piece of code to use internal iteration instead of external iteration.
	 */
	@Test
	public void exercise1(){
		Collection<String> strings = new Bag<>("one", "two", "three");
		
		Iterator<String> iter = strings.iterator();
		while (iter.hasNext()){
			System.out.println(iter.next());
		}
		
	}
	
	public interface Collection<E> {

	    int size();

	    Iterator<E> iterator();

	    boolean add(E e);
	    
	    boolean addAll(java.util.Collection<? extends E> c);
		
	}

	@Test
	public void exercise2(){
		List<String> strings = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");

		StringBuilder builder = new StringBuilder();
		// Append all items in "strings" to "builder";
		
		assertEquals("AlphaBetaGammaDelta", builder.toString());
	}
	
	
	@Test
	public void exercise3(){
		List<String> strings = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");
		
		// Replace all items in "strings" with their uppercase value
		
		assertEquals(Arrays.asList("ALPHA", "BETA", "GAMMA", "DELTA"), strings);
	}

	@Test
	public void exercise4(){
		List<String> strings = new java.util.ArrayList<>(Arrays.asList("Alpha", "Beta", "Gamma", "Delta"));

		// Remove all items from the list that have less than 5 characters
		
		assertEquals("Alpha", strings.get(0));
		assertEquals("Gamma", strings.get(1));
		assertEquals("Delta", strings.get(2));
	}

	@Test
	public void exercise5(){
		Map<Integer, String> map = new java.util.HashMap<>(5);
		map.put(1, "one");
		map.put(2, "two");
		map.put(3, "three");

		List<Integer> keys = new java.util.ArrayList<>(5);
		List<String> values = new java.util.ArrayList<>(5);
		// Add all keys to the keys list and all values to the values list, with items corresponding by index

		
		assertEquals(1, keys.get(0).intValue());
		assertEquals("one", values.get(0));
		assertEquals(2, keys.get(1).intValue());
		assertEquals("two", values.get(1));
		assertEquals(3, keys.get(2).intValue());
		assertEquals("three", values.get(2));
	}
	
	@Test
	public void exercise6(){
		Map<Integer, List<Integer>> modulo = new java.util.HashMap<>();
		
		// Group all integers between 0 and 100 in lists according to their value modulo 10
		// and put them in the map with the rest of the division as a key
		// So 1 goes into the list at 1, 12 into the list at 2, and so on... 
		
		assertEquals(Arrays.asList(3, 13, 23, 33,43, 53, 63, 73, 83, 93), modulo.get(3));
	}


	@Test
	public void exercise7(){
		String[] intNames = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
		List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		// Sort the numbers in "ints" alphabetically by name
		
		assertEquals(Arrays.asList(8, 5, 4, 9, 1, 7, 6, 10, 3, 2, 0), ints);
	}
	
	@Test
	public void exercise8(){
		List<String> strings = Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");
		
		// Sort the items in "strings" first according to their length, then alphabetically
		
		assertEquals(Arrays.asList("one", "six", "ten", "two", "five", "four", "nine", "zero", "eight", "seven", "three"), strings);
	}
	
	
	@Test
	public void exercise9(){
		String[] intNames = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
		List<Integer> ints = Arrays.asList(null, 1, 2, 3, null, 5, 6, 7, null, 9, 10);
		
		// Sort the numbers in "ints" alphabetically by name. Keep null values at the end.
		
		assertEquals(Arrays.asList(5, 9, 1, 7, 6, 10, 3, 2, null, null, null), ints);
	}


	
}
