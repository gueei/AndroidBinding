package gueei.binding.treeviewdemo;

import gueei.binding.Binder;
import gueei.binding.labs.BinderTreeview;
import gueei.binding.v30.DefaultKernelV30;
import android.app.Application;

public class TreeviewDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
		Binder.init(this, new DefaultKernelV30());
		BinderTreeview.init(this);
		//gueei.binding.widgets.TreeViewList.setSmoothScrollEnsureVisible(true);
	}

}
