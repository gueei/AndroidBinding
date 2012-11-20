package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class SelectedItemViewAttribute extends ViewAttribute<AdapterView<?>, Object>
	implements OnItemSelectedListener{

	public SelectedItemViewAttribute(AdapterView<?> view, String attributeName) {
		super(Object.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class)
			.registerWithHighPriority(this);
	}

	@Override
	public Object get() {
		if(getView()==null) return null;
		return getView().getSelectedItem();
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// Readonly, do nothing
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		this.notifyChanged();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		this.notifyChanged();
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.TwoWay;
	}
}
