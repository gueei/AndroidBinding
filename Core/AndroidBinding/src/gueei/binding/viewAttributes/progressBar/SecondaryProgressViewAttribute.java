package gueei.binding.viewAttributes.progressBar;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;

/**
 * Secondary Progress of Progress Bar
 * equivalent to android:secondaryProgress
 * 
 * Note: Prior to 0.6, this is Float value between 0 and 1, 
 * but it cause so much trouble to implement and it changes to Integer from now on
 * progress also changes to Integer 
 * 
 * @name secondaryProgress
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
public class SecondaryProgressViewAttribute extends ViewAttribute<ProgressBar, Integer> {
	public SecondaryProgressViewAttribute(ProgressBar view) {
		super(Integer.class, view, "secondaryProgress");
		getView().setSecondaryProgress(0);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue == null) return;
		if (newValue instanceof Integer){
			getView().setSecondaryProgress((Integer)newValue);
		}
	}

	@Override
	public Integer get() {
		if(getView()==null) return null;
		return getView().getSecondaryProgress();
	}
}
