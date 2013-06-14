package gueei.binding.viewAttributes.view;


import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.view.View;


public class VisibilityViewAttribute extends ViewAttribute<View, Object> {
	
	// this is a hackish way to avoid  cast exceptions when
	// when binding the visibility on a menu item and and a view
	// the menu item returns an integer and we need a boolean
	private boolean isBoolType = true; 
	
	public VisibilityViewAttribute(View view, String attributeName) {
		super(Object.class, view, attributeName);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Boolean){
			isBoolType = true;
			if ((Boolean)newValue)
				getView().setVisibility(View.VISIBLE);
			else
				getView().setVisibility(View.GONE);
			return;
		}
		if (newValue instanceof Integer){
			isBoolType = false;
			getView().setVisibility((Integer)newValue);
			return;
		}
	}

	@Override
	public Object get() {
		if(getView()==null) return null;
		int visibility =  getView().getVisibility();
		if(isBoolType) {
			if(View.VISIBLE == visibility)
				return true;
			return false;
		} else {
			return visibility;
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Boolean.class.isAssignableFrom(type))
			return BindingType.OneWay;
		if (Integer.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		return BindingType.NoBinding;
	}
}
