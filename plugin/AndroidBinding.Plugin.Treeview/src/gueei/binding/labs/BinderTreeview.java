package gueei.binding.labs;

import gueei.binding.AttributeBinder;
import gueei.binding.labs.bindingProviders.TreeViewListProvider;
import android.app.Application;


public class BinderTreeview extends gueei.binding.Binder {
	/**
	 * call after BinderV3 init()
	 * @param application
	 */
	public static void init(Application application){
		// Put v30 providers first
		AttributeBinder.getInstance().registerProvider(new TreeViewListProvider());
	}
}
