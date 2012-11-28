package gueei.binding.viewAttributes.progressBar;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;

/**
 * Maximum Value of the Progress Bar
 * equivalent to android:max
 * 
 * @name max
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

public class MaxViewAttribute extends ViewAttribute<ProgressBar, Integer>{
	
	public MaxViewAttribute(ProgressBar view) {
		super(Integer.class, view, "max");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue == null) return;
		if (newValue instanceof Integer){
			getView().setMax((Integer)newValue);
		}
	}

	@Override
	public Integer get() {
		if(getView()==null) return null;
		return getView().getMax();
	}
}
