package com.example;

import java.util.Optional;
import java.util.OptionalInt;

public class Optionals {

	public static void main(String[] argss) {
		
		Optional<String> optional = getOptionalValue();
		
		System.out.println(optional.orElse("default"));
		
		optional.orElseThrow(() -> new IllegalStateException("no value"));
		
		optional.orElseGet(() -> requestUserInput());
		
		optional.ifPresent(value -> doSomethingWith(value));
		
		Optional<Integer> length = optional.map(String::length);
		
		Optional<String> punctuation = optional.flatMap( string -> findPunctuationMarks(string) );
		
		Optional<String> notEmptyString = optional.filter(string -> string.length() > 0);
		
		
	}
	
	
	private static Optional<String> getOptionalValue(){
		return null;
	}
	
	private static String requestUserInput(){
		return null;
	}
	
	private static void doSomethingWith(String value){
		
	}
	
	private static Optional<String> findPunctuationMarks(String string){
		return null;		
	}
}
