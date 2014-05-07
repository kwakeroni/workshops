package com.example;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DefaultMethods {

	@Test
	public void testDefaultMethods(){
		// Add a browseInternet(String url) method to the interface below,
		// without breaking the following implementations:
		
		class GrannyPhone implements Phone {
			public void call(String phoneNumber){
				// use twisted pair cable
			}
		}
		
		class SmartPhone implements Phone {
			public void call(String phoneNumber){
				// use wireless network
			}
			public void browseInternet(String url){
				// use wireless network
			}
		}
	}
	
	
	public static interface Phone {
		
		void call(String phoneNumber);
		
//		default void browseInternet(String url){
//			throw new UnsupportedOperationException();
//		}
	}
	
	

	
}
