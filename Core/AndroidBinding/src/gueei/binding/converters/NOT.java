package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Converter to perform logical NOT operation.
 * It allows 1 argument only, and it should be in type boolean
 * 
 * @usage arg
 * 
 * @arg arg boolean
 * 
 * @return boolean
 *  
 */
public class NOT extends Converter<Boolean> {

	public NOT(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public boolean ConvertBack(Object value, Object[] outResult) {
		if ((!(value instanceof Boolean)) || (outResult.length<1)) return false;
		outResult[0] = !((Boolean)value);
		return true;
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<1) return true;
		if (args[0] instanceof Boolean){
			return !((Boolean)args[0]);
		}
		if (args[0] instanceof Number){
			return !args[0].equals(0);
		}
		if (args[0] != null)
			return false;
		
		return true;
	}
}
