package gueei.binding.markupDemoICS.viewModels;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;


public class ActionBar {
	public final BooleanObservable ShowHomeAsUp =
			new BooleanObservable(false);
	
	public final Command ToggleShowHomeAsUp = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ShowHomeAsUp.toggle();
		}
	};
	
	public final BooleanObservable ShowHome =
			new BooleanObservable(true);
	
	public final Command ToggleShowHome = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ShowHome.toggle();
		}
	};
	
	public final BooleanObservable ShowTitle =
			new BooleanObservable(true);
	
	public final Command ToggleShowTitle = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ShowTitle.toggle();
		}
	};
}
