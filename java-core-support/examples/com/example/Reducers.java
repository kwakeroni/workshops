package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Reducers extends Lambda {
	
	public static void main(String[] args){
		codeExamples();
	}

	public static void codeExamples(){
		System.out.println(IntStream.of().sum() == 0);
		IntBinaryOperator product = (i,j) -> i * j;
		System.out.println(IntStream.of().reduce(1, product) == 1);
		System.out.println(IntStream.of().max());
		
		OptionalInt result = IntStream.of().max();
		
		if (result.isPresent()) { System.out.println(result.getAsInt()); }
		result.ifPresent( i -> System.out.println(i) );

		System.out.println(result.orElse(-1));
		System.out.println(result.orElseThrow(() -> new IllegalStateException("no value")));

		Stream<?> stream = Arrays.asList().stream();
		
		List<?> list = stream.collect(Collectors.toList());

	}
}
