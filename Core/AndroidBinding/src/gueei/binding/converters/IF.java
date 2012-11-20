package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.Undetermined;

/**
 * Test the condition, if the condition is true, return arg_if_true, or else return arg_if_false
 * 
 * @usage condition arg_if_true arg_if_false
 * 
 * @arg condition boolean
 * @arg arg_if_true Object
 * @arg arg_if_false Object
 * 
 * @return boolean
 */

public class IF extends Converter<Object> implements Undetermined {

	public IF(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<3) return null;		
		if (args[0] == null) return null;		
		if (Boolean.TRUE.equals(args[0])){
			return args[1];
		}else{
			return args[2];
		}
	}
}
