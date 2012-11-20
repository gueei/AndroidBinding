package gueei.binding.observables;

public class FloatObservable extends gueei.binding.Observable<Float> {

	public FloatObservable() {
		super(Float.class);
	}

	public FloatObservable(Float initValue) {
		super(Float.class, initValue);
	}
}
