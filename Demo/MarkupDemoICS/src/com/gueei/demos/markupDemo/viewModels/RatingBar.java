package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.FloatObservable;
import android.view.View;

public class RatingBar {
	public final FloatObservable Rating = new FloatObservable(3f);
	public final BooleanObservable Changed = new BooleanObservable(false);
	public final Command RatingChangedCommand = new Command(){
		private Thread resetChange;
		public void Invoke(View view, Object... args) {
			Changed.set(true);
			if ((resetChange!=null)&&(resetChange.isAlive()))
				resetChange.interrupt();
			resetChange = new Thread(){
				@Override
				public void run() {
					try{
						sleep(1000);
						Changed.set(false);
					}catch(Exception e){
						return;
					}
				}
			};
			resetChange.start();
		}
	};
}
