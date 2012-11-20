package gueei.binding.listeners;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnCheckedChangeListenerMulticast
	extends ViewMulticastListener<CompoundButton.OnCheckedChangeListener>
	implements CompoundButton.OnCheckedChangeListener{

	@Override
	public void registerToView(View v) {
		if (CompoundButton.class.isInstance(v)){
			((CompoundButton)v).setOnCheckedChangeListener(this);
		}
	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (!this.isFromUser()){
			this.clearBroadcastState();
			return;
		}
		
		for(OnCheckedChangeListener l: listeners){
			l.onCheckedChanged(arg0, arg1);
		}
		this.clearBroadcastState();
	}
}
