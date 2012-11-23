package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import gueei.binding.observables.BooleanObservable;

public class Converters {
	public final BooleanObservable A = new BooleanObservable(true);
	public final BooleanObservable B = new BooleanObservable(false);
	
	/**
	 * Custom TwoWayDependentObservable:
	 * Custom TwoWayDependentObservable can be extends from DependentObservable (One-way) or TwoWayDependentObservable (Two-way)
	 * It MUST have the CONSTRUCTOR(IObservable<?>[] dependents) defined
	 * To reference custom converter, full package.class name must be provided; 
	 * and becoz the following is an inner class, so it is referenced with a dollar sign 
	 *        'com.gueei.demos.markupDemo.viewModels.Converters$CustomConverter'
	 */
	public static class CustomConverter extends DependentObservable<String>{
		public CustomConverter(IObservable<?>[] dependents) {
			super(String.class, dependents);
		}

		@Override
		public String calculateValue(Object... args) throws Exception {
			if (args[0].equals(Boolean.TRUE)){
				return "Response from CustomConverter: args[0] is TRUE";
			}
			
			return "Response from CustomConverter: args[0] is FALSE";
		}
	}
}
