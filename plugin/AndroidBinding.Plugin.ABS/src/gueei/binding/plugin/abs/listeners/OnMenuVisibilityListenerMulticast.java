package gueei.binding.plugin.abs.listeners;

import gueei.binding.listeners.ViewMulticastListener;
import gueei.binding.plugin.abs.BindableActionBar;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.view.View;

public class OnMenuVisibilityListenerMulticast 
	extends ViewMulticastListener<OnMenuVisibilityListener> 
	implements OnMenuVisibilityListener {

	public void onMenuVisibilityChanged(boolean isVisible) {
		for(OnMenuVisibilityListener l : listeners){
			l.onMenuVisibilityChanged(isVisible);
		}
	}

	@Override
	public void registerToView(View v) {
		((BindableActionBar)v).getActionBar().addOnMenuVisibilityListener(this);
	}
}
