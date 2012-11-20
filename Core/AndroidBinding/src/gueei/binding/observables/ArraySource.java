package gueei.binding.observables;

import gueei.binding.Observable;

@Deprecated
public class ArraySource<T> extends Observable<Object[]> {
	public ArraySource() {
		super(Object[].class);
	}
	
	public ArraySource(T[] value){
		super(Object[].class, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] get() {
		return (T[])super.get();
	}
	
	public void setArray(T[] value){
		this.set(value);
	}
}
