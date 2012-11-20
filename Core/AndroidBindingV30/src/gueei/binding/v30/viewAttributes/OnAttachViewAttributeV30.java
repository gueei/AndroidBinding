package gueei.binding.v30.viewAttributes;

import android.view.View;
import gueei.binding.Binder;
import gueei.binding.v30.listeners.OnAttachStateChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnAttachViewAttributeV30 extends ViewEventAttribute<View> implements View.OnAttachStateChangeListener {
	public OnAttachViewAttributeV30(View view) {
		super(view, "onAttach");
	}
	
	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnAttachStateChangeListenerMulticast.class).register(this);
	}

	public void onViewAttachedToWindow(View v) {
		this.invokeCommand(v);		
	}

	public void onViewDetachedFromWindow(View v) {			
	}
	
}

