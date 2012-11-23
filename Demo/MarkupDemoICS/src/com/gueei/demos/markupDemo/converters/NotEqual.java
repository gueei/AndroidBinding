package com.gueei.demos.markupDemo.converters;

import gueei.binding.IObservable;
import gueei.binding.converters.EQUAL;

public class NotEqual extends EQUAL {

	public NotEqual(IObservable<?>[] dependents) {
		super(dependents);
	}

	@Override
	public Boolean calculateValue(Object... args) throws Exception {
		return !super.calculateValue(args);
	}

}
