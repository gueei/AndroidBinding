package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import android.view.View;

public class CustomView {
	public StringObservable Mask = new StringObservable("*");
	public Command Clear = new Command(){
		public void Invoke(View view, Object... args) {
			Mask.set("");
		}
	};
}