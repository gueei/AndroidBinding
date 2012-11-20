package gueei.binding.converters;

import java.util.Locale;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class UPPER extends Converter<Object> {	
	public static Locale locale = null;

	public UPPER(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {
		if (args.length<1 || args[0] == null) return null;
		if(locale != null)
			return String.valueOf(args[0]).toUpperCase(locale);
		return String.valueOf(args[0]).toUpperCase();
	}
}
