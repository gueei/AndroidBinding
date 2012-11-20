package gueei.binding.v30.actionbar.attributes;

import gueei.binding.v30.actionbar.BindableActionBar;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class TabNavigationOnItemSelected 
	extends ViewEventAttribute<BindableActionBar>{

	public TabNavigationOnItemSelected(BindableActionBar view) {
		super(view, "TabNavigationOnItemSelected");
	}

	@Override
	protected void registerToListener(BindableActionBar view) {
		// nothing, only register in list navigation adapter
	}
}
