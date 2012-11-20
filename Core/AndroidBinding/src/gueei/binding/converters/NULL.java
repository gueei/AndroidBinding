package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Constant converter that always returns null
 * @usage
 * 
 * @return null
 * 
 * @author andy
 *
 */
public class NULL extends Converter<Object> {

	public NULL(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		return null;
	}

}
