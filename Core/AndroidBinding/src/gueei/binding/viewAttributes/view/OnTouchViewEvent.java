package gueei.binding.viewAttributes.view;

import gueei.binding.Binder;
import gueei.binding.listeners.OnTouchListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.view.MotionEvent;
import android.view.View;

public class OnTouchViewEvent extends ViewEventAttribute<View> implements View.OnTouchListener {
	public OnTouchViewEvent(View view) {
		super(view, "onTouch");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		TouchEventResult result = new TouchEventResult();			
		this.invokeCommand(v, event, result);		
		return result.eventConsumed;
	}
	
	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnTouchListenerMulticast.class).register(this);
	}
	
}
