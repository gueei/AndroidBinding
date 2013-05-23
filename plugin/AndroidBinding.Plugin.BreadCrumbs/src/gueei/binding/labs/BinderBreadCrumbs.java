package gueei.binding.labs;

import gueei.binding.AttributeBinder;
import gueei.binding.labs.bindingProviders.BreadCrumbsProvider;
import android.app.Application;


public class BinderBreadCrumbs extends gueei.binding.Binder {
	/**
	 * call after BinderV3 init()
	 * @param application
	 */
	public static void init(Application application){
		// Put v30 providers first
		AttributeBinder.getInstance().registerProvider(new BreadCrumbsProvider());
	}
}
