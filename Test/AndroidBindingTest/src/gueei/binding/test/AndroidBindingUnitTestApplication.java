package gueei.binding.test;

import gueei.binding.Binder;
import android.app.Application;

public class AndroidBindingUnitTestApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this);
	}

}
