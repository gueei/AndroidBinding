package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Converter to perform logical OR operation.
 * It allows 1 or more arguments, if any argument is true, then it will be true. <br/>
 * Return false only if all arguments are false
 * 
 * @usage arg arg ...
 * 
 * @arg arg boolean
 * 
 * @return boolean
 */
public class OR extends Converter<Boolean> {

	public OR(IObservable<?>[] dependents) {
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

		int argLen = args.length;
		for(int i=0; i<argLen; i++){
			if (isTrue(args[i])) return true;
		}
		
		return false;
	}
	
	private boolean isTrue(Object item){
		if (item instanceof Boolean)
			 return ((Boolean)item);
		else if (item instanceof Number)
			return item.equals(0);
		else return item == null;
	}
}
