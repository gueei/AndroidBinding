package gueei.binding.viewAttributes.view;

import android.view.View;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;

public class SelectedViewAttribute extends ViewAttribute<View, Boolean> {

	public SelectedViewAttribute(View view) {
		super(Boolean.class, view, "selected");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (Boolean.TRUE.equals(newValue)){
			getView().setSelected(true);
		}else{
			getView().setSelected(false);
		}
	}

	@Override
	public Boolean get() {
		if(getView()==null) return null;
		return getView().isSelected();
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Boolean.class.isAssignableFrom(type)){
			return BindingType.OneWay;
		}
		return BindingType.NoBinding;
	}
}
