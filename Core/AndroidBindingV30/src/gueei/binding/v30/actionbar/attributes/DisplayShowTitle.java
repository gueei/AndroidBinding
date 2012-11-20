package gueei.binding.v30.actionbar.attributes;

import android.app.ActionBar;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;

public class DisplayShowTitle extends ViewAttribute<BindableActionBar, Boolean> {

	public DisplayShowTitle(BindableActionBar view) {
		super(Boolean.class, view, "displayShowTitle");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		getHost().getActionBar().setDisplayShowTitleEnabled(newValue.equals(Boolean.TRUE));
	}

	@Override
	public Boolean get() {
		return (getHost().getActionBar().getDisplayOptions() & ActionBar.DISPLAY_SHOW_TITLE) 
				== ActionBar.DISPLAY_SHOW_TITLE;
	}
}
