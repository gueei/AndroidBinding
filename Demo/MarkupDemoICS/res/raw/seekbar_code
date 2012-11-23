package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.DependentObservable;
import gueei.binding.observables.FloatObservable;
import android.app.Activity;
import android.os.Handler;

public class SeekBar {
	Handler handler = new Handler();
	Activity mActivity;
	
	public SeekBar(Activity activity){
		mActivity = activity;
	}
	
	public final FloatObservable PrimaryProgress = new FloatObservable(0f);
	public final DependentObservable<Float> SecondaryProgress 
		= new DependentObservable<Float>(Float.class, PrimaryProgress){
			@Override
			public Float calculateValue(Object... args) throws Exception {
				return PrimaryProgress.get() * 1.3f;
			}
	};
}