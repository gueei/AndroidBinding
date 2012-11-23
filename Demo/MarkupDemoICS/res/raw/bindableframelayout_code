package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.markupDemoICS.R;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.widgets.ILayoutLoadEvent;
import android.view.View;

public class BindableFrameLayout {

	public final IntegerObservable LayoutId = new IntegerObservable(0);
	
	public final Command ToggleLayout = new Command(){
		public void Invoke(View view, Object... args) {
			
			if( LayoutId.get() == null || LayoutId.get() == 0) {
				LayoutId.set(R.layout.bindableframelayout_frame1);
			} else {
				if (LayoutId.get() == R.layout.bindableframelayout_frame1)
					LayoutId.set(R.layout.bindableframelayout_frame2);
				else if (LayoutId.get() == R.layout.bindableframelayout_frame2)
					LayoutId.set(R.layout.bindableframelayout_frame3);
				else
					LayoutId.set(null);
			}
		}
	};
	
	public final Command OnLoad = new Command(){
		public void Invoke(View view, Object... args) {		
			if( args == null || args.length < 1 )
				return;
			
			ILayoutLoadEvent loader = (ILayoutLoadEvent)args[0];			
			loader.setLayoutId(R.layout.bindableframelayout_frame_on_load);
		}
	};

}