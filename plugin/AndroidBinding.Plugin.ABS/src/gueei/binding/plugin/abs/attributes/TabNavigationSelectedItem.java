package gueei.binding.plugin.abs.attributes;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.plugin.abs.BindableActionBar;

public class TabNavigationSelectedItem extends ViewAttribute<BindableActionBar, Object>{

	public TabNavigationSelectedItem(BindableActionBar view) {
		super(Object.class, view, "tabNavigationSelectedItem");
	}

	private Object mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		this.mValue = newValue;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.TwoWay;
	}

	@Override
	public Object get() {
		return mValue;
	}
}
