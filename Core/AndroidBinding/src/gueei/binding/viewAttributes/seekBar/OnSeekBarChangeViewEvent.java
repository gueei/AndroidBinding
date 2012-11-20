package gueei.binding.viewAttributes.seekBar;

import android.widget.SeekBar;
import gueei.binding.Binder;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnSeekBarChangeViewEvent extends ViewEventAttribute<SeekBar> implements SeekBar.OnSeekBarChangeListener{

	public OnSeekBarChangeViewEvent(SeekBar view) {
		super(view, "onSeekBarChange");
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser){
			this.invokeCommand(seekBar, progress, fromUser);
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	protected void registerToListener(SeekBar view) {
		Binder.getMulticastListenerForView(view, OnSeekBarChangeListenerMulticast.class).register(this);
	}

}
