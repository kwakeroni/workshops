package com.example.solution;

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
 * Solutions to exercises on Lambda Expressions, Functional Interfaces and Method References.
 */
public class Exercise1 {

	/*
	 * Rewrite the following function assignments using lambda expressions.
	 */
	@Test
	public void exercise1(){
		
		Function<String, Integer> length = s -> s.length();
		
		Function<Calendar, Integer> year = cal -> cal.get(Calendar.YEAR);
		
		BiFunction<String, Integer, Character> charAt = (s, i) -> s.charAt(i);
		
		// 
		BiFunction<String, Integer, Integer> parse = (s, i) -> Integer.parseInt(s, i);
		
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
		UnaryOperator<String> escaper = s -> s.replace(' ', '_');

		// Operator that concatenates two Strings
		BinaryOperator<String> concat = (s1, s2) -> s1 + s2;
		
		// Operator that returns the sum of two ints
		IntBinaryOperator intSum = (s1, s2) -> s1 + s2;
		
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
		Supplier<Date> dates = () -> new Date();
		
		assertNotNull(dates.get());
		assertNotSame(dates.get(), dates.get());
	}
	
	/*
	 * Rewrite the following lambda expressions as method references
	 */
	@Test
	public void exercise4(){
		// Write the following lambdas as method literals
		
		Function<String, Integer> 				length  = String::length;
		BiFunction<String, Integer, Character> 	charAt  = String::charAt;
		Function<String, Integer> 				parse1  = Integer::parseInt;
		BiFunction<String, Integer, Integer> 	parse2  = Integer::parseInt;
		Function<String, Integer> 				indexOf = "Hello"::indexOf;
		Function<String, Integer> 				newInt  = Integer::new;

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
		
		// Impossible to write as a method reference
		// Because the invocation target (s) needs to be the first parameter
		BiFunction<Integer, String, Character> atChar  = (i, s) -> s.charAt(i);
		
		// Impossible to write as a method reference
		// Because method arguments cannot be specified
		Function<String, Integer>              ofIndex = s      -> s.indexOf("Hello");
		
		// Verify
		assertEquals('l', atChar.apply(2, "Hello").charValue());
		assertEquals(5, ofIndex.apply("Why, Hello World!").intValue());
	}
	
}
