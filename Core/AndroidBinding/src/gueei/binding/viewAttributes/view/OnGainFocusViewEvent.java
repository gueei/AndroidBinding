package gueei.binding.viewAttributes.view;

import gueei.binding.Binder;
import gueei.binding.listeners.OnFocusChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.view.View;

public class OnGainFocusViewEvent extends ViewEventAttribute<View> implements View.OnFocusChangeListener {
	public OnGainFocusViewEvent(View view) {
		super(view, "onGainFocus");
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnFocusChangeListenerMulticast.class).register(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			invokeCommand(v);
	}
}