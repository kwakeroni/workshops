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
		new Annotations().multiple();
		main2(args);
	}
	
	@Test
//	@A("A1")
	@B(@A("BA1"))
	@B(@A("BA2"))
	public void multiple() throws Exception{
		System.out.println( Arrays.asList(Annotations.class.getMethod("multiple").getAnnotations()));
		System.out.println( Arrays.asList(Annotations.class.getMethod("multiple").getAnnotationsByType(A.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod("multiple").getAnnotationsByType(B.class)));
		System.out.println( Arrays.asList(Annotations.class.getMethod("multiple").getAnnotationsByType(C.class)));
		
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
}
