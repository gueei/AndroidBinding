package gueei.binding.collections;

import android.content.Context;

/**
 * Collection with this interface will not load all children
 * immediately, rather they are loaded when required (displaying, for example)
 * @author andy
 *
 */
public interface LazyLoadParent {
	/**
	 * Changed to include the context. 
	 * @param context
	 */
	public void onLoadChildren(Context context);
}