package com.example.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Calendar;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.Test;

/**
 * Exercises on Lambda Expressions, Functional Interfaces and Method References.
 */
public class Exercise1 {

	/*
	 * Rewrite the following function assignments using lambda expressions.
	 */
	@Test
	public void exercise1(){
		
		Function<String, Integer> length = new Function<String, Integer>() {

			@Override
			public Integer apply(String t) {
				return t.length();
			}
			
		};
		
		Function<Calendar, Integer> year =  new Function<Calendar, Integer>() {

			@Override
			public Integer apply(Calendar t) {
				return t.get(Calendar.YEAR);
			}
			
		};
		
		BiFunction<String, Integer, Character> charAt = new BiFunction<String, Integer, Character>() {
			
			@Override
			public Character apply(String t, Integer u) {
				return t.charAt(u);
			}
		};
		
		// 
		BiFunction<String, Integer, Integer> parse = new BiFunction<String, Integer, Integer>() {
			
			@Override
			public Integer apply(String t, Integer u) {
				return Integer.parseInt(t, u);
			}
		};
		
		// Verify
		assertEquals(5, length.apply("Hello").intValue());
		assertEquals(new Date().getYear()+1900, year.apply(Calendar.getInstance()).intValue());
		assertEquals('e', charAt.apply("Hello", 1).charValue());
		assertEquals(12, parse.apply("C", 16).intValue());
		
	}
	
	/*
	 * Implement the following operators with a lambda expression.
	 */
	@Test
	public void exercise2(){
		
		// Operator that replaces all spaces in a String with underscores
		UnaryOperator<String> escaper = null;

		// Operator that concatenates two Strings
		BinaryOperator<String> concat = null;
		
		// Operator that returns the sum of two ints
		IntBinaryOperator intSum = null;
		
		// Verify
		assertEquals("No_Spaces_In_Answer", escaper.apply("No Spaces In Answer"));
		assertEquals("Complete Answer", concat.apply("Complete ", "Answer"));
		assertEquals(18, intSum.applyAsInt(11, 7));
	}
	
	/*
	 * Implement the following date "factory".
	 */
	@Test
	public void exercise3(){
		// A supplier that produces an infinite stream of dates
		Supplier<Date> dates = null;
		
		assertNotNull(dates.get());
		assertNotSame(dates.get(), dates.get());
	}
	
	/*
	 * Rewrite the following lambda expressions as method references
	 */
	@Test
	public void exercise4(){
		// Write the following lambdas as method literals
		
		Function<String, Integer> 				length  =      s -> s.length();
		BiFunction<String, Integer, Character> 	charAt  = (s, i) -> s.charAt(i);
		Function<String, Integer> 				parse1  =      s -> Integer.parseInt(s);
		BiFunction<String, Integer, Integer> 	parse2  = (s, i) -> Integer.parseInt(s, i);
		Function<String, Integer> 				indexOf =      s -> "Hello".indexOf(s);
		Function<String, Integer> 				newInt  =      s -> new Integer(s);

		// Verify
		assertEquals(5, length.apply("Hello").intValue());
		assertEquals('e', charAt.apply("Hello", 1).charValue());
		assertEquals(12, parse1.apply("12").intValue());
		assertEquals(12, parse2.apply("C", 16).intValue());
		assertEquals(4, indexOf.apply("o").intValue());
		assertEquals(12, newInt.apply("12").intValue());
		
	}
	
	/*
	 * Try to rewrite the following lambda expressions as method references
	 */
	@Test
	public void exercise5(){
		
		BiFunction<Integer, String, Character> atChar  = (i, s) -> s.charAt(i);
		Function<String, Integer>              ofIndex = s      -> s.indexOf("Hello");
		
		// Verify
		assertEquals('l', atChar.apply(2, "Hello").charValue());
		assertEquals(5, ofIndex.apply("Why, Hello World!").intValue());
	}

	
	
}
