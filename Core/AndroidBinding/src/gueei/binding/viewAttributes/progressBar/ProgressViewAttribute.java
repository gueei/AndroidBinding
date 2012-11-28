package gueei.binding.viewAttributes.progressBar;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/**
 * Progress of Progress Bar
 * equivalent to android:progress
 * 
 * Note: Prior to 0.6, this is Float value between 0 and 1, 
 * but it cause so much trouble to implement and it changes to Integer from now on
 * secondaryProgress also changes to Integer 
 * 
 * @name progress
 * @widget ProgressBar
 * @type Integer
 * 
 * @accepts	Integer
 * 
 * @category simple
 * @related http://developer.android.com/reference/android/widget/ProgressBar.html
 * 
 * @author andy
 */
public class ProgressViewAttribute extends ViewAttribute<ProgressBar, Integer>
	implements SeekBar.OnSeekBarChangeListener{
	
	public ProgressViewAttribute(ProgressBar view) {
		super(Integer.class, view, "progress");
		getView().setProgress(0);
		if (view instanceof SeekBar){
			Binder.getMulticastListenerForView(view, OnSeekBarChangeListenerMulticast.class)
				.register(this);
		}
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue == null) return;
		if (newValue instanceof Integer){
			getView().setProgress((Integer)newValue);
		}
	}

	@Override
	public Integer get() {
		if(getView()==null) return null;
		return getView().getProgress();
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser){
			this.notifyChanged();
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
