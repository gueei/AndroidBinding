package gueei.binding.v30.actionbar.attributes;

import android.app.ActionBar;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;

public class DisplayHomeAsUp extends ViewAttribute<BindableActionBar, Boolean> {

	public DisplayHomeAsUp(BindableActionBar view) {
		super(Boolean.class, view, "displayHomeAsUp");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		getHost().getActionBar().setDisplayHomeAsUpEnabled(newValue.equals(Boolean.TRUE));
	}

	@Override
	public Boolean get() {
		return (getHost().getActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) 
				== ActionBar.DISPLAY_HOME_AS_UP;
	}
}
