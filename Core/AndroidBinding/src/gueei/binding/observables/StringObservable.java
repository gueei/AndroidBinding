package gueei.binding.observables;

import gueei.binding.Observable;

import java.util.Collection;


public class StringObservable extends Observable<String> {
	public StringObservable() {
		super(String.class);
	}
	
	public StringObservable(String value){
		super(String.class, value);
	}

	@Override
	public void _setObject(Object newValue,
			Collection<Object> initiators) {
		if (newValue!=null)
			this.set(newValue.toString(), initiators);
	}
}
