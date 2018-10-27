package com.example;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import com.example.TypeAnnotations.TypeParam;
import com.example.TypeAnnotations.Use;



public class TypeAnnotations<@TypeParam @Use T> {
	
	public static void main(String[] args) throws Exception {
		
		printAnnotations(TypeAnnotations.class);
		printAnnotations(TypeAnnotations.class.getTypeParameters()[0]);
		printAnnotations(getMethod("test"));
		printAnnotations(getMethod("test").getReturnType()); // Class
		printAnnotations(getMethod("test").getAnnotatedReturnType()); // Class use
		printAnnotations(getMethod("testMethod"));
		
		
		printAnnotations(getMethod("testReceiver", new Class<?>[]{}));
		printAnnotations(getMethod("testReceiver", new Class<?>[]{}).getAnnotatedReceiverType());
		
	}

	public @Meth  <@TypeParam @Use S> @Mixed @Use TypeAnnotations<@Use ?>  test(@Param @Use @Mixed String arg) throws @Use Exception {
		/*@Local*/ @Use @Mixed String var = "";
		
		
		
		
		return new @Use TypeAnnotations<@Use String>();
	}
	
	public @Meth @Use void testMethod(String string){
		
	}
	
	public void testReceiver(@Use TypeAnnotations<T> this){
		if (false){
			testReceiver();
		}
	}
	
	
	@Target(ElementType.LOCAL_VARIABLE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Local {
		
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Use {
		
	}

	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Param {
		
	}

	@Target(ElementType.TYPE_PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TypeParam {
		
	}

	@Target({ElementType.METHOD, ElementType.TYPE_USE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Mixed {
		
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Meth{
		
	}

	private static void printAnnotations(AnnotatedElement element){
		System.out.println("--- " + element);
		for (Annotation annotation : element.getAnnotations()){
			System.out.println(annotation.annotationType().getSimpleName());
		}
		System.out.println();
	}
	
	private static Method getMethod(String name) throws Exception {
		return getMethod(name, String.class);
	}
	
	private static Method getMethod(String name, Class<?>... types) throws Exception {
		return TypeAnnotations.class.getMethod(name, types);
	}

	   @Target({ElementType.TYPE, ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
	    @Retention(RetentionPolicy.RUNTIME)
	    public @interface Here {
	        
	    }
	   
	   @Here
	   class Test<@Here T> extends @Here Object {
	       public @Here String getValues(@Here List<@Here String> List) throws @Here IllegalArgumentException {
	           return new @Here String("Hello");
	       }
	   }
}
