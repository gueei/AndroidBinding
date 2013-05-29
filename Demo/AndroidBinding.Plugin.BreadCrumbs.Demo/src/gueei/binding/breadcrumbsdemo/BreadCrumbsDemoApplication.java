package gueei.binding.breadcrumbsdemo;

import gueei.binding.Binder;
import gueei.binding.labs.BinderBreadCrumbs;
import gueei.binding.v30.DefaultKernelV30;
import android.app.Application;

public class BreadCrumbsDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
		Binder.init(this, new DefaultKernelV30());
		BinderBreadCrumbs.init(this);
	}

}
