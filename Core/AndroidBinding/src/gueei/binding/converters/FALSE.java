package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Constant that always return false. Takes no argument
 *  
 * @usage
 * 
 * @return boolean
 */
public class FALSE extends Converter<Boolean> {

	public FALSE(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		return false;
	}

}
