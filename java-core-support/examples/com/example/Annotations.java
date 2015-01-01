package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

//import com.example.AnnotationsDefinition.A;
//import com.example.AnnotationsDefinition.B;

public class Annotations {

	public static void main(String[] args) throws Exception {
		new Annotations().multipleB();
		main2(args);
	}
	
	@Test
	@A("A1")
	@A("A2")
	public void multipleA() throws Exception {
		String method = "multipleA";
		System.out.println("--- " + method);
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotations()));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(A.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(B.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(C.class)));	
		
	}
	
	@Test
//	@A("A1")
	@B(@A("BA1"))
	@B(@A("BA2"))
	public void multipleB() throws Exception{
		String method = "multipleB";
		System.out.println("--- " + method);
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotations()));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(A.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(B.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod(method).getAnnotationsByType(C.class)));	
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Repeatable(B.class)
	public @interface A {
		String value();
	}
	
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Repeatable(C.class)
	public @interface B {
		A[] value();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface C {
		B[] value();
	}
	
	
	public void processor(){
		
	}
	
    public static void main2(String[] args) {
			Function<Object, Object> f = Key -> "Value";
            final Method m;
            try {
                m = f.getClass().getDeclaredMethod("apply", Object.class);
            } catch (NoSuchMethodException nsme ) { throw new RuntimeException(nsme); }
            final Parameter p = m.getParameters()[0];
            final String key = p.getName();
            System.out.println(key);
    }
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Aliases.class)
    public @interface Alias {
        String value();
    }
    
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Aliases {
        Alias[] value();
    }
    
    @Aliases({ @Alias("One"), @Alias("Two")})
    class Test1 {
        
    }
    
    @Alias("One")
    @Alias("Two")
    class Test2 {
        
    }
    
}
