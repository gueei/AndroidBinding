package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemLongClickListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;


public class LongClickedItemViewAttribute extends ViewAttribute<AdapterView<?>, Object>
	implements OnItemLongClickListener{

	private Object value;
	
	public LongClickedItemViewAttribute(AdapterView<?> view, String attributeName) {
		super(Object.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemLongClickListenerMulticast.class)
			.registerWithHighPriority(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// do nothing. this is a readonly attribute
	}

	@Override
	public Object get() {
		return value;
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (!getView().equals(parent)) return false;
		try{
			this.value = getView().getItemAtPosition(position);
			this.notifyChanged();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
