package gueei.binding.plugin.abs.attributes;

import com.actionbarsherlock.app.ActionBar;
import gueei.binding.ViewAttribute;
import gueei.binding.plugin.abs.BindableActionBar;

public class NavigationMode extends ViewAttribute<BindableActionBar, Integer> {

	public NavigationMode(BindableActionBar view) {
		super(Integer.class, view, "displayHomeAsUp");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Integer)
			getHost().getActionBar().setNavigationMode((Integer)newValue);
		else
			getHost().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public Integer get() {
		return getHost().getActionBar().getNavigationMode();
	}
}
