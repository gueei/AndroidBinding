package gueei.binding.viewAttributes.seekBar;

import android.widget.SeekBar;
import gueei.binding.Binder;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

/**
 * On Seek Bar Changed View Event. Fires when Seek Bar's changed
 * 
 * @name onSeekBarChange
 * @widget SeekBar
 * @type Command
 * 
 * @accepts	Command
 * 
 * @category simple
 * @related http://developer.android.com/reference/android/widget/SeekBar.html
 * @related http://developer.android.com/reference/android/widget/SeekBar.OnSeekBarChangeListener.html
 * 
 * @author andy
 */
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
