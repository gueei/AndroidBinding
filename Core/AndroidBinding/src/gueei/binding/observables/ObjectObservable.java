package gueei.binding.observables;

import gueei.binding.Observable;

public class ObjectObservable extends Observable<Object> {
	public ObjectObservable() {
		super(Object.class);
	}
	
	public ObjectObservable(Object value){
		super(Object.class, value);
	}
}
