package gueei.binding.v30.actionbar.attributes;

import gueei.binding.BindingType;
import gueei.binding.IObservableCollection;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;

@SuppressWarnings("rawtypes")
public class NavigationItemSource extends ViewAttribute<BindableActionBar, IObservableCollection> {

	public NavigationItemSource(BindableActionBar view) {
		super(IObservableCollection.class, view, "navigationItemSource");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
	}

	@Override
	public IObservableCollection get() {
		return null;
	}
	
	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}
}
