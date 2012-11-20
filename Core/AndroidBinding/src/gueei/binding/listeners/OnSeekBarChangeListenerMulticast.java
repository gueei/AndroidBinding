package gueei.binding.listeners;

import android.view.View;
import android.widget.SeekBar;

public class OnSeekBarChangeListenerMulticast
	extends ViewMulticastListener<SeekBar.OnSeekBarChangeListener>
	implements SeekBar.OnSeekBarChangeListener{

	@Override
	public void registerToView(View v) {
		if (SeekBar.class.isInstance(v)){
			((SeekBar)v).setOnSeekBarChangeListener(this);
		}
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		for(SeekBar.OnSeekBarChangeListener l: listeners){
			l.onProgressChanged(seekBar, progress, fromUser);
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		for(SeekBar.OnSeekBarChangeListener l: listeners){
			l.onStartTrackingTouch(seekBar);
		}
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		for(SeekBar.OnSeekBarChangeListener l: listeners){
			l.onStopTrackingTouch(seekBar);
		}
	}
}
