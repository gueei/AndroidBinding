package gueei.binding.v30.actionbar.attributes;

import android.app.ActionBar.OnMenuVisibilityListener;
import gueei.binding.Binder;
import gueei.binding.v30.actionbar.BindableActionBar;
import gueei.binding.v30.actionbar.listeners.OnMenuVisibilityListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnMenuVisibilityChanged 
	extends ViewEventAttribute<BindableActionBar> 
	implements OnMenuVisibilityListener {

	public OnMenuVisibilityChanged(BindableActionBar view) {
		super(view, "onMenuVisibilityChanged");
	}

	@Override
	protected void registerToListener(BindableActionBar view) {
		Binder.getMulticastListenerForView(view, OnMenuVisibilityListenerMulticast.class)
			.register(this);
	}

	public void onMenuVisibilityChanged(boolean isVisible) {
		this.invokeCommand(this.getView(), isVisible);
	}
}