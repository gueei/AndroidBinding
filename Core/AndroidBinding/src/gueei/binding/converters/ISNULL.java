package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Test if the argument is null <br/>
 * 
 * @usage arg
 * 
 * @arg arg Object

 * @return boolean
 */

public class ISNULL extends Converter<Boolean> {

	public ISNULL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args[0]==null) return true;
		else return false;
	}

}
