package gueei.binding.plugin.abs.attributes;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.plugin.abs.BindableActionBar;

public class ListNavigationSelectedItem extends ViewAttribute<BindableActionBar, Object> {

	public ListNavigationSelectedItem(BindableActionBar view) {
		super(Object.class, view, "ListNavigationSelectedItem");
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
