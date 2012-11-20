package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Inverted result of ISNULL <br/>
 * return true if the argument is NOT null 
 * 
 * @usage arg
 * 
 * @arg arg Object

 * @return boolean
 */
public class IS_NOT_NULL extends Converter<Boolean> {

	public IS_NOT_NULL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args[0]!=null) return true;
		else return false;
	}

}
