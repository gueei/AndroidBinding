package gueei.binding.listeners;

import android.view.MotionEvent;
import android.view.View;

public class OnTouchListenerMulticast extends ViewMulticastListener<View.OnTouchListener> implements View.OnTouchListener {
	@Override
	public void registerToView(View v) {
		v.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean result = false;
		
		for (View.OnTouchListener l: listeners){
			if( l.onTouch(v, event) )
				result = true;
		}

		return result;
	}
}