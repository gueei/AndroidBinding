package gueei.binding.markupDemoICS.viewModels;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.observables.StringObservable;

public class Javascript {
	public final StringObservable Script = 
			new StringObservable();
	
	public final StringObservable EvalScript = 
			new StringObservable();
	
	public final Command Execute = 
			new Command(){
				@Override
				public void Invoke(View view, Object... args) {
					EvalScript.set(Script.get());
				}
	};
}
