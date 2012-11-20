package gueei.binding.viewAttributes.progressBar;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnSeekBarChangeListenerMulticast;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/*
 * Changed in v0.6
 * Accepts Integer only, works with Max or android:max
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
