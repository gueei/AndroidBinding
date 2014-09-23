package gueei.binding.binablelayout.test;

import gueei.binding.Binder;
import android.app.Application;

public class AndroidBindingBindableLayoutTestApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
