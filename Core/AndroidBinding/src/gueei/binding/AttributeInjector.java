package gueei.binding;

import gueei.binding.Binder.InflateResult;

import android.view.View;

/**
 * Helper method to inject attributes to the given View or View collections
 * It is supposed to do after the view is inflated, parsed and created view binding map, 
 * but before actual binding occurs
 * @author andy
 *
 */
public class AttributeInjector {

	public static class InjectParams{
		public int Id;
		public String AttrName;
		public String Statement;
	}
	
	/**
	 * Inject the given params to the whole tree of Views to the given root
	 * Since this is injection, the injecting parameters is always given higher priority and thus 
	 * if existing attr is there, it will be overriden by injected value
	 * @param params
	 * @param root
	 */
	public static void inject(InjectParams[] params, InflateResult result){
		int len = params.length;
		for(int i=0; i<len; i++){
			View target = result.rootView.findViewById(params[i].Id);
			if (!result.processedViews.contains(target))
				result.processedViews.add(target);
			if (target==null) continue;
			inject(target, params[i].AttrName, params[i].Statement);
		}
	}

	public static void inject(View view, String attrName, String statement){
		BindingMap map = Binder.getBindingMapForView(view);
		map.put(attrName, statement);
	}
}
