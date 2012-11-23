package com.gueei.demos.markupDemo.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class LENTH_LARGER_THAN extends Converter<Boolean> {

	public LENTH_LARGER_THAN(IObservable<?>[] dependents) {
		super(Boolean.class, dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		if (args.length<2) return true;
		String arg0 = args[0].toString();
		Integer arg1 = (Integer)args[1];
		return (arg0.length() >= arg1);
	}

}
