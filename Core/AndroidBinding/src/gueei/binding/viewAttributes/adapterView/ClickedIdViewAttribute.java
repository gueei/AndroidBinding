package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class ClickedIdViewAttribute extends ViewAttribute<AdapterView<?>, Long>
	implements OnItemClickListener{

	private Long value;
	
	public ClickedIdViewAttribute(AdapterView<?> view, String attributeName) {
		super(Long.class, view, attributeName);
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.register(this);
	}

	@Override
	public Long get() {
		return value;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// Readonly, do nothing
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		this.value = id;
		this.notifyChanged();
	}
}
