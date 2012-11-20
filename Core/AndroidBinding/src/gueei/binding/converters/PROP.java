package gueei.binding.converters;

import gueei.binding.Binder;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.Undetermined;

/**
 * Dynamically evaluate the statement for the given view_model
 * 
 * @usage view_model statement
 * 
 * @arg view_model Object the Context to evaluate the statement
 * @arg statement String
 * 
 * @return Object
 */
public class PROP extends Converter<Object> implements Undetermined {

	public PROP(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<2) return null;
		if (args[0] == null) return null;
		IObservable<?> childObs = 
				Binder.getSyntaxResolver()
					.constructObservableFromStatement(this.getContext(), args[1].toString(), args[0]);
		if (childObs!=null)
			return childObs.get();
		
		return null;
	}
}
