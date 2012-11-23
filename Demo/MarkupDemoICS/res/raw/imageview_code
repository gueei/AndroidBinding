package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Observable;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import gueei.binding.markupDemoICS.R;

public class ImageView {
	public IntegerObservable ImageFromResourceId = new IntegerObservable(R.drawable.ic_launcher);
	public Observable<Drawable> ImageFromDrawable = new Observable<Drawable>(Drawable.class);
	
	public ImageView(Activity activity){
		ImageFromDrawable.set(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
				new int[]{Color.argb(0, 0, 0, 0), Color.rgb(220, 100, 100), Color.argb(0, 0, 0, 0)}));
	}
}
