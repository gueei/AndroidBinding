package gueei.binding.listeners;

import android.view.View;

public class OnLongClickListenerMulticast extends ViewMulticastListener<View.OnLongClickListener> 
	implements View.OnLongClickListener {

	@Override
	public void registerToView(View v) {
		v.setOnLongClickListener(this);
	}

	public boolean onLongClick(View v) {
		boolean returnValue = false;
		for(View.OnLongClickListener l : listeners){
			if (l.onLongClick(v)){
				returnValue = true;
			}
		}
		return returnValue;
	}
}