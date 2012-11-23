package gueei.binding.markupDemoICS;

import gueei.binding.Binder;
import gueei.binding.v30.DefaultKernelV30;
import android.app.Application;

public class MarkupDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Binder.init(this, new DefaultKernelV30());
	}

}
