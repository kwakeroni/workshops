package com.example;

import java.util.Calendar;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.Test;

import static org.junit.Assert.*;

public class LambdaExpressions {

	@Test
	public void testFunctions(){
		
		// Write a lambda that returns the length of a String
		Function<String, Integer> length = null;
		
		// Write a lambda that returns the year of a Calendar
		Function<Calendar, Integer> year = null;
		
		// Write a lambda that returns the character at a specific position in a String
		BiFunction<String, Integer, Character> charAt = null;
		
		// Write a lambda that parses a String to an Integer according to a specific radix
		BiFunction<String, Integer, Integer> parse = null;
		
		{
		// Solutions
		length = (String s) -> s.length();
		year   = (Calendar s) -> s.get(Calendar.YEAR);
		charAt = (String s, Integer i) -> s.charAt(i);
		parse =  (String s, Integer i) -> Integer.parseInt(s, i);
		
		// Or
		length = s -> s.length();
		year   = s -> s.get(Calendar.YEAR);
		charAt = (s, i) -> s.charAt(i);
		parse =  (s, i) -> Integer.parseInt(s, i);
		}
		
		// Verify
		assertEquals(5, length.apply("Hello").intValue());
		assertEquals('e', charAt.apply("Hello", 1).charValue());
		assertEquals(12, parse.apply("C", 16).intValue());
		
	}
	
	@Test
	public void testOperators(){
		
		// Write a lambda that replaces all spaces in a String with underscores
		UnaryOperator<String> escaper = null;

		// Write a lambda that concatenates two Strings
		BinaryOperator<String> concat = null;
		
		// Write a lambda that returns the sum of two ints
		IntBinaryOperator intSum = null;
		
		{
		// Solutions
		escaper = (String s) -> s.replace(' ', '_');
		concat = (String s1, String s2) -> s1 + s2;
		intSum = (int i1, int i2) -> i1 + i2;
		
		// Or
		concat = (s1, s2) -> s1 + s2;
		intSum = (i1, i2) -> i1 + i2;
		}
		
		// Verify
		assertEquals("No_Spaces_In_Answer", escaper.apply("No Spaces In Answer"));
		assertEquals("Complete Answer", concat.apply("Complete ", "Answer"));
		assertEquals(18, intSum.applyAsInt(11, 7));
	}
	
	@Test
	public void testSupplier(){
		// Write a lambda that supplies an infinite stream of dates
		Supplier<Date> dates = null;
		
		{
		// Solutions
		dates = () -> new Date();
		}
		
		assertNotNull(dates.get());
		assertNotSame(dates.get(), dates.get());
	}
	
	/**
	 * @see String#length()
	 * @see Integer#parseInt(String)
	 * @see String#indexOf(String)
	 * @see Integer#Integer(String)
	 */
	@Test
	public void testMethodLiterals(){
		// Write the following lambdas as method literals
		
		Function<String, Integer> 				length_lambda  = s      -> s.length();
		Function<String, Integer>				length_literal = null;
		
		BiFunction<String, Integer, Character> 	charAt_lambda  = (s, i) -> s.charAt(i);
		BiFunction<String, Integer, Character> 	charAt_literal = null;
		
		Function<String, Integer> 				parse1_lambda  = s      -> Integer.parseInt(s);
		Function<String, Integer> 				parse1_literal = null;
		
		BiFunction<String, Integer, Integer> 	parse2_lambda  = (s, i) -> Integer.parseInt(s, i);
		BiFunction<String, Integer, Integer> 	parse2_literal = null;

		Function<String, Integer> 				indexOf_lambda  = s      -> "Hello".indexOf(s);
		Function<String, Integer> 				indexOf_literal = null;

		Function<String, Integer> 				newInt_lambda  = s      -> new Integer(s);
		Function<String, Integer> 				newInt_literal = null;
		
		// Understand why the following lambdas cannot be written as a method literal
		Function<String, Integer> ofIndex = s -> s.indexOf("Hello");
		BiFunction<Integer, String, Character> atChar = (i, s) -> s.charAt(i);
		
		{
		// Solution
		length_literal = String::length;      // length()
		charAt_literal = String::charAt; // charAt(int)
		parse1_literal = Integer::parseInt;    // parseInt( String )
		parse2_literal = Integer::parseInt;	// parseInt( String, int )
		indexOf_literal = "Hello"::indexOf;   // indexOf( String )
		newInt_literal = Integer::new;        // new Integer( String )
		}
		
		// Verify
		assertEquals(length_lambda.apply("Hello"), length_literal.apply("Hello"));
		assertEquals(charAt_lambda.apply("Hello", 1), charAt_literal.apply("Hello", 1));
		assertEquals(parse1_lambda.apply("12"), parse1_literal.apply("12"));
		assertEquals(parse2_lambda.apply("C", 16), parse2_literal.apply("C", 16));
		assertEquals(indexOf_lambda.apply("e"), indexOf_literal.apply("e"));
		Consumer<Object> c = e -> System.out.println(e);
		
	}
	
}
