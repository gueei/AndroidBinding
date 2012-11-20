package gueei.binding.viewAttributes.progressBar;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;

/*
 * Accepts Float from 0 - 1, and translate the result to a 1000-based fraction
 * as the progress.
 * i.e. progress of progress bar will become 
 */
public class MaxViewAttribute extends ViewAttribute<ProgressBar, Integer>{
	
	public MaxViewAttribute(ProgressBar view) {
		super(Integer.class, view, "progress");
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
