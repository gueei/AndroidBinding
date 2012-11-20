package gueei.binding.viewAttributes.view;

import gueei.binding.Binder;
import gueei.binding.listeners.OnFocusChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.view.View;

public class OnLostFocusViewEvent extends ViewEventAttribute<View> implements View.OnFocusChangeListener {
	public OnLostFocusViewEvent(View view) {
		super(view, "onLostFocus");
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnFocusChangeListenerMulticast.class).register(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus)
			invokeCommand(v);
	}
}