package gueei.binding.v30.listeners;

import gueei.binding.listeners.ViewMulticastListener;
import android.view.View;

public class OnAttachStateChangeListenerMulticast extends ViewMulticastListener<View.OnAttachStateChangeListener> implements View.OnAttachStateChangeListener {
	@Override
	public void registerToView(View v) {
		v.addOnAttachStateChangeListener(this);
	}

	public void onViewAttachedToWindow(View v) {
		for (View.OnAttachStateChangeListener l: listeners){
			l.onViewAttachedToWindow(v);
		}
	}

	public void onViewDetachedFromWindow(View v) {
		for (View.OnAttachStateChangeListener l: listeners){
			l.onViewDetachedFromWindow(v);
		}	
	}
}