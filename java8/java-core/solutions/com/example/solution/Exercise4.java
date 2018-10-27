package com.example.solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions to exercises on collectors.
 */
public class Exercise4 {

	/*
	 * Grouping: group all words by length
	 */
	@Test
	public void exercise1() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			Map<Integer, List<String>> map = reader.lines()
							.flatMap(s -> Arrays.stream(s.split("\\s+")))
							.collect(Collectors.groupingBy(String::length));

			map.forEach((i, list) -> System.out.println(i + " -> " + list));
		}
		
	}
	
	/*
	 * Grouping: group all words first by first character then by length
	 */
	@Test
	public void exercise2() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			Map<Character, Map<Integer, List<String>>> map = reader.lines()
							.flatMap(s -> Arrays.stream(s.split("\\s+")))
							.filter(s -> s.length() > 0)
							.collect(Collectors.groupingBy(s -> s.charAt(0),
									 Collectors.groupingBy(String::length)));
			
			map.forEach((i, listmap) -> System.out.println(i + " -> " + listmap));
				
		}		
	}
	
	
	/*
	 * Grouping: get statistics about the average length of words by their first character
	 */
	@Test
	public void exercise3() throws Exception {
		try(BufferedReader reader = new BufferedReader(new FileReader("./data/yesterday.txt"))){

			Map<Character, IntSummaryStatistics> map = reader.lines()
							.flatMap(s -> Arrays.stream(s.split("\\s+")))
							.filter(s -> s.length() > 0)
							.collect(Collectors.groupingBy(s -> s.charAt(0),
									 Collectors.summarizingInt(String::length)));
				
			map.forEach((i, stats) -> System.out.println(i + " -> " + stats));
		}		
	}
	
}
