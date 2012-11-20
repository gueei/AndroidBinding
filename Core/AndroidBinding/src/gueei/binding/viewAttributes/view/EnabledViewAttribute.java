package gueei.binding.viewAttributes.view;

import gueei.binding.ViewAttribute;
import android.view.View;

public class EnabledViewAttribute extends ViewAttribute<View, Boolean> {

	public EnabledViewAttribute(View view, String attributeName) {
		super(Boolean.class, view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setEnabled(false);
			return;
		}
		if (newValue instanceof Boolean){
			getView().setEnabled((Boolean)newValue);
		}
	}

	@Override
	public Boolean get() {
		if(getView()==null) return null;
		return getView().isEnabled();
	}
}
