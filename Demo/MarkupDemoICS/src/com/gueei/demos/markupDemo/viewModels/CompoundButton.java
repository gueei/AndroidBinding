package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import android.os.Handler;
import android.view.View;

public class CompoundButton {
	public final BooleanObservable A = new BooleanObservable(true);
	public final BooleanObservable Changed = new BooleanObservable(false);
	Handler handler = new Handler();
	public final Command AChanged = new Command(){
		private Thread resetChange;
		public void Invoke(View view, Object... args) {
			Changed.set(true);
			if ((resetChange!=null)&&(resetChange.isAlive()))
				resetChange.interrupt();
			resetChange = new Thread(){
				@Override
				public void run() {
					try{
						handler.postDelayed(new Runnable(){
							public void run(){
								Changed.set(false);								
							}
						}, 1000);
					}catch(Exception e){
						return;
					}
				}
			};
			resetChange.start();
		}
	};
}
