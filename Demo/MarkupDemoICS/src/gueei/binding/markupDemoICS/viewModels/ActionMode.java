package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.Command;
import gueei.binding.ConstantObservable;
import gueei.binding.IObservable;
import gueei.binding.converters.FORMAT;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.v30.widget.ActionModeBinder;
import android.app.Activity;
import android.view.View;


public class ActionMode {
	private final Activity mActivity;

	public ActionMode(Activity activity){
		mActivity = activity;
	}
	
	public final Command StartActionMode = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ActionModeBinder binder = 
					ActionModeBinder
						.startActionMode(mActivity, 
								ActionModeMenuId.get(), ActionMode.this, Title);
		}
	};
	
	public final IntegerObservable ActionModeMenuId =
			new IntegerObservable(0);
	
	public final BooleanObservable ItemVisible = new BooleanObservable(true);

	public final FORMAT Title = new FORMAT(new IObservable<?>[]{ 
			new ConstantObservable<String>(String.class, "Item visible? %s"),
			ItemVisible });
	
	public final Command ToggleItemVisible = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ItemVisible.toggle();
		}
	};
}
