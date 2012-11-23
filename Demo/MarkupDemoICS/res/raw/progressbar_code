package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.observables.IntegerObservable;
import android.os.Handler;

public class ProgressBar {
	Handler handler = new Handler();
	public ProgressBar(){
		Thread setProgress = new Thread(){
			@Override
			public void run() {
				super.run();
				while(PrimaryProgress.get() < Max.get()){
					handler.post(new Runnable(){
						public void run(){
							PrimaryProgress.set(PrimaryProgress.get() + 1);
							SecondaryProgress.set((int)(PrimaryProgress.get() * 1.5));							
						}
					});
					try{
						sleep(50);
					}catch(Exception e){
						return;
					}
				}
			}
		};
		setProgress.start();
	}
	
	public final IntegerObservable Max = new IntegerObservable(500);
	public final IntegerObservable PrimaryProgress = new IntegerObservable(0);
	public final IntegerObservable SecondaryProgress = new IntegerObservable(0);
}
