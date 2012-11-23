package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class ContextMenu {
	private Activity mActivity;
	public ContextMenu(Activity activity){
		mActivity = activity;
	}
	
	public final StringObservable FirstItem = new StringObservable("First: ");
	public final StringObservable SecondItem = new StringObservable("Second: ");
	public final Command Selected = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(mActivity, "Selected: " + args[0], Toast.LENGTH_SHORT).show();
		}
	};
}