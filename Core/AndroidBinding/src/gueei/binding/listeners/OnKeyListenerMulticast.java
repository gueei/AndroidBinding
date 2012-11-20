package gueei.binding.listeners;

import android.view.KeyEvent;
import android.view.View;

public class OnKeyListenerMulticast extends ViewMulticastListener<View.OnKeyListener> implements View.OnKeyListener {
	@Override
	public void registerToView(View v) {
		v.setOnKeyListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean result = false;
		
		for (View.OnKeyListener l: listeners){
			if( l.onKey(v, keyCode, event) )
				result = true;
		}

		return result;
	}
}