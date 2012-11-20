package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Length of the given String, if provided is an object, object.toString().length() will be evaluated
 * 
 * @usage str
 * @arg str String
 * 
 * @return integer
 * 
 * @author andy
 *
 */
public class LEN extends Converter<Integer> {
	public LEN(IObservable<?>[] dependents) {
		super(Integer.class, dependents);
	}

	@Override
	public Integer calculateValue(Object... args) throws Exception {
		return args[0].toString().length();
	}
}
