package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class ClickedItemViewAttribute extends ViewAttribute<AdapterView<?>, Object>
	implements OnItemClickListener{

	private Object value;
	
	public ClickedItemViewAttribute(AdapterView<?> view, String attributeName) {
		super(Object.class, view, attributeName);
		//this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.registerWithHighPriority(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		value = newValue;
	}

	@Override
	public Object get() {
		return value;
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		try{
			this.value = getView().getItemAtPosition(position);
			this.notifyChanged();
		}catch(Exception e){
		}
	}
	
}
