package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Converter to perform EQUAL operation. <br/>
 * It accepts 2 arguments, and perform Object.equals() on them <br/>
 * Note if both arguments are null, it returns true
 * 
 * @usage arg1 arg2
 * 
 * @arg arg1 Object
 * @arg arg2 Object
 * 
 * @return boolean
 */
public class EQUAL extends Converter<Boolean> {

	public EQUAL(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<2) return false;
		if (args[0]==null && args[1]==null) return true;
		if (args[0]==null || args[1]==null)
			return false;

		return args[0].equals(args[1]);
	}
}
