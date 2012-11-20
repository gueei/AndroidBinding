package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;

public class SelectedPositionViewAttribute extends ViewAttribute<AdapterView<?>, Integer> implements OnItemSelectedListener {
	public SelectedPositionViewAttribute(AdapterView<?> view) {
		super(Integer.class, view, "selectedPosition");
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class).register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (!(newValue instanceof Integer)) return;
		
		getView().setSelection((Integer)newValue);
		mValue = (Integer)newValue;
	}

	@Override
	public Integer get() {
		return mValue;
	}

	private int mValue = 0;
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		mValue = pos;
		this.notifyChanged();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		mValue = -1;
		this.notifyChanged();
	}
}
