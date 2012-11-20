package gueei.binding.v30;

import gueei.binding.Binder;
import gueei.binding.BindingMap;
import android.app.Application;
import android.view.View;

/**
 * @deprecated Use Binder instead
 * @author andy
 *
 */
public class BinderV30 extends Binder {
	/**
	 * @deprecated Use Binder.init(Application, IKernel) instead
	 * @param application
	 */
	@Deprecated
	public static void init(Application application){
		Binder.init(application, new DefaultKernelV30());
	}
	
	/**
	 * Merge the BindingMap in the view with the supplied new map
	 * If both keys are existed, it will be replaced by the new map
	 * @param view
	 * @param map
	 */
	@Deprecated
	public static void mergeBindingMap(View view, BindingMap map){
		BindingMap original = getBindingMapForView(view);
		if (original==null){
			putBindingMapToView(view, map);
			return;
		}
		
		for(String key: map.getAllKeys()){
			original.put(key, map.get(key));
		}
		putBindingMapToView(view, original);
	}
}
