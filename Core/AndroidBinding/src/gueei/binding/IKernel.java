package gueei.binding;

import gueei.binding.Binder.InflateResult;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.ViewMulticastListener;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface IKernel {
	/**
	 * Get the required attribute for the supplied view. This is done internally using the "tag" of the view
	 * so the attribute id must be using the Android id resource type
	 * @param view
	 * @param attributeId must be attribute defined in id type Resource
	 * @return
	 * @throws AttributeNotDefinedException
	 */
	public ViewAttribute<?, ?> getAttributeForView(View view, String attributeId) throws AttributeNotDefinedException;
	
	/**
	 * Get the associated View Tag of a view, if view tag is not existed or the
	 * existing tag is not view tag, a new viewTag will be created and return.
	 * @param view
	 * @return
	 */
	public ViewTag getViewTag(View view);
	
	public AttributeCollection getAttributeCollectionOfView(View view);
	public void putBindingMapToView(View view, BindingMap map);
	public BindingMap getBindingMapForView(View view);
	
	/**
	 * Inflate, and parse the binding information with Android binding
	 * @param context
	 * @param layoutId The xml layout declaration
	 * @param parent Parent view of the group, just pass null in most cases
	 * @param attachToRoot Pass false
	 * @return Inflate Result. 
	 */
	public InflateResult inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot);
	/**
	 * Returns the binded root view of the inflated view
	 * @param context
	 * @param inflatedView The inflated result from inflateView
	 * @param model The view model that is going to bind to
	 * @return RootView of the binded view
	 */
	public View bindView(Context context, InflateResult inflatedView, Object model);
	public <T extends ViewMulticastListener<?>> T getMulticastListenerForView(View view, Class<T> listenerType);
	public void init(Application application);
	
	public AttributeBinder getAttributeBinder();
	public ISyntaxResolver getSyntaxResolver();
}