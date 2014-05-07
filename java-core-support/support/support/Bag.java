package support;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Bag<E> implements com.example.exercise.Exercise1.Collection<E>, 
							   com.example.solution.Exercise1.Collection<E> {

	private List<E> backing = new java.util.ArrayList<E>();
	
	public Bag(E... values){
		addAll(Arrays.asList(values));
	}

	public int size() {
		return backing.size();
	}

	public Iterator<E> iterator() {
		return backing.iterator();
	}

	public boolean add(E e) {
		return backing.add(e);
	}

	public boolean addAll(java.util.Collection<? extends E> c) {
		return backing.addAll(c);
	}
	
	

}
