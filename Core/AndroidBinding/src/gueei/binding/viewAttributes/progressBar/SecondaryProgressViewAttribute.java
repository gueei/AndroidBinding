package gueei.binding.viewAttributes.progressBar;

import gueei.binding.ViewAttribute;
import android.widget.ProgressBar;


public class SecondaryProgressViewAttribute extends ViewAttribute<ProgressBar, Integer> {
	public SecondaryProgressViewAttribute(ProgressBar view) {
		super(Integer.class, view, "progress");
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
