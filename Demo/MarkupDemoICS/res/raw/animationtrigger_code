package com.gueei.demos.markupDemo.viewModels;

import android.view.View;
import android.widget.Toast;
import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.app.Activity;

public class AnimationTrigger {
	private Activity mActivity;
	public AnimationTrigger(Activity activity){
		mActivity = activity;
	}
	public final StringObservable Password = new StringObservable("");
	public final StringObservable RealPassword = new StringObservable("1234");
	public final BooleanObservable PasswordCorrect = new BooleanObservable(false);
	public final Command CheckPassword = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			PasswordCorrect.set(Password.get().equals(RealPassword.get()));
			if (PasswordCorrect.get()){
				Toast.makeText(mActivity, "Password Accepted!", Toast.LENGTH_SHORT).show();
			}
		}
	};
}