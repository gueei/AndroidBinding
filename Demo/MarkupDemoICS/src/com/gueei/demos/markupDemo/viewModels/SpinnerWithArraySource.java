package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.collections.ArrayListObservable;

public class SpinnerWithArraySource {

	public final ArrayListObservable<String> ContinentNames =
			new ArrayListObservable<String>(String.class);

	public SpinnerWithArraySource() {
		ContinentNames.setArray(
				new String[]{"Asia", "Africa", "Antarctica", "Australia", "Europe", "North America", "South America"});
	}
}
