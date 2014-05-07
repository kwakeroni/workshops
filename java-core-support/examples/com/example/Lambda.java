package com.example;

import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;

public class Lambda {

	public static void codeExamples(){
		
		IntBinaryOperator bo;
		IntSupplier su;
		Consumer<String> co;
		
		co = (String s) -> { System.out.println(s); } ;
		co = (String s) -> System.out.println(s) ;
		co = s -> System.out.println(s) ;
		
		bo = (int x, int y) -> x + y ;
		bo = (x, y) -> x + y ;
		su = () -> 42 ;
		
	}
	
	public static void functionalInterfaces(){
		
		ActionListener listener;
		
	}
	
	public static void methodReferences(){

		Function<String, Integer> parse1 = (String s) -> Integer.parseInt(s);
		Function<String, Integer> parse2 = Integer::parseInt;

		Function<String, String> toUpperCase1 = (String s) -> s.toUpperCase();
		Function<String, String> toUpperCase2 = String::toUpperCase;

		BiFunction<String, Integer, Character> charAt1 = (String s, Integer i) -> s.charAt(i);
		BiFunction<String, Integer, Character> charAt2 = String::charAt;


		
		Supplier<Set<String>> setFactory1 = () -> new TreeSet<String>();
		Supplier<Set<String>> setFactory2 = TreeSet::new;
		
		// Parameter manipulation with method references
		//Test t1 = (Number n) -> String.valueOf(n) ;
		Test t2 = Lambda::toString;

	}
	
	public static void fluentAPI(){
		Comparator<Person> comparator = 
				Comparator.comparing(Person::getLastName)
						  .thenComparing(Person::getFirstName)
						  .thenComparingInt(Person::getAge);
	}
	
	public static void together(){
		/* private static */ final Comparator<Person> PERSON_COMPARATOR = new Comparator<Person>() {
			
			@Override
			public int compare(Person o1, Person o2) {
				return o1.getLastName().compareTo(o2.getLastName());
			}
		};
		
		List<Person> people = null; /* ... */
		Collections.sort(people, new Comparator<Person>() {
			
			@Override
			public int compare(Person x, Person y) {
				return y.getLastName().compareTo(x.getLastName());
			}
		});
		
		Collections.sort(people, (Person x, Person y) -> 
		x.getLastName().compareTo(y.getLastName()));

		Collections.sort(people, 
				Comparator.comparing((Person p) -> p.getLastName()));

		Collections.sort(people, comparing(p -> p.getLastName()));
	}
	
	
	public static String toString(Number n){
		return String.valueOf(n);
	}
	
	
	public interface Test {
		Object test(Integer i)  ;
	}
	
	public class Person {
		String firstName;
		String lastName;
		int age;
		public String getFirstName() {
			return firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public int getAge() {
			return age;
		}
		
		
	}
	
}
