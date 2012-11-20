package gueei.binding;

import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.kernel.DefaultKernel;
import gueei.binding.listeners.ViewMulticastListener;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class Binder {
	public static final String BINDING_NAMESPACE = "http://www.gueei.com/android-binding/";
	public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
	
	private static IKernel _kernel;
	public static IKernel getInstance(){
		if (_kernel==null) 
			throw new RuntimeException(
					"Binder has not yet initializated. Do you forget to put Binder.init(application) in Application.Create? ");
		return _kernel;
	}
	
	public static ViewAttribute<?, ?> getAttributeForView(View view, String attributeId)
		throws AttributeNotDefinedException	{
		return _kernel.getAttributeForView(view, attributeId);
	}
	
	public static ViewTag getViewTag(View view){
		return _kernel.getViewTag(view);
	}
	
	public static AttributeCollection getAttributeCollectionOfView(View view){
		return _kernel.getAttributeCollectionOfView(view);
	}

 	public static void putBindingMapToView(View view, BindingMap map){
		_kernel.putBindingMapToView(view, map);
	}
	
	public static BindingMap getBindingMapForView(View view){
		return _kernel.getBindingMapForView(view);
	}
	
	public static InflateResult inflateView(Context context, int layoutId, ViewGroup parent, boolean attachToRoot){
		return _kernel.inflateView(context, layoutId, parent, attachToRoot);
	}
	
	public static View bindView(Context context, InflateResult inflatedView, Object model){
		return _kernel.bindView(context, inflatedView, model);
	}
	
	public static void init(Application application){
		init(application, new DefaultKernel());
	}
	
	public static void init(Application application, IKernel kernel){
		if (_kernel!=null) throw new RuntimeException(
				"Init should only called once. Please check your code. ");
		
		_kernel = kernel;
		_kernel.init(application);
	}

	public static <T extends ViewMulticastListener<?>> T getMulticastListenerForView(View view, Class<T> listenerType){
		return _kernel.getMulticastListenerForView(view, listenerType);
	}
	
	public static ISyntaxResolver getSyntaxResolver(){
		return _kernel.getSyntaxResolver();
	}
	
	/**
	 * The class holding inflated result from Android Binding.
	 * @author andy
	 *
	 */
	public static class InflateResult{
		public ArrayList<View> processedViews = new ArrayList<View>();
		public View rootView;
	}
	
	public static String currentVersion(){
		return "0.6";
	}
}
