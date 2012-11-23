package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.observables.StringObservable;

public class TextViewCustomFont {
	public final StringObservable DefaultFont = new StringObservable("Default");
	public final StringObservable CustomFont = new StringObservable("Custom");
}