package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Constant that always return true. Takes no argument
 *  
 * @usage
 * 
 * @return boolean
 */
public class TRUE extends Converter<Boolean> {

	public TRUE(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		return true;
	}

}
