package gueei.binding.listeners;

import android.view.View;

public class OnFocusChangeListenerMulticast extends ViewMulticastListener<View.OnFocusChangeListener> 
	implements View.OnFocusChangeListener {
	
	@Override
	public void registerToView(View v) {
		v.setOnFocusChangeListener(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		for(View.OnFocusChangeListener l : listeners){
			l.onFocusChange(v, hasFocus);
		}
	}
}