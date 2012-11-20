package gueei.binding.v30.actionbar.attributes;

import gueei.binding.v30.actionbar.BindableActionBar;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class ListNavigationOnItemSelected 
	extends ViewEventAttribute<BindableActionBar>{

	public ListNavigationOnItemSelected(BindableActionBar view) {
		super(view, "ListNavigationOnItemSelected");
	}

	@Override
	protected void registerToListener(BindableActionBar view) {
		// nothing, only register in list navigation adapter
	}
}
