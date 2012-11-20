package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import gueei.binding.listeners.OnItemLongClickListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * User: =ra=
 * Date: Dec 14, 2011
 * Time: 1:37:15 PM
 */
public class ClickActionPositionViewAttribute extends ViewAttribute<AdapterView<?>, Integer> implements OnItemClickListener,
		AdapterView.OnItemLongClickListener {

	public ClickActionPositionViewAttribute(AdapterView<?> view) {
		super(Integer.class, view, "clickActionPosition");
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class).registerWithHighPriority(this);
		Binder.getMulticastListenerForView(view, OnItemLongClickListenerMulticast.class).registerWithHighPriority(this);
	}

	private int mValue;

	@Override
	public Integer get() {
		return mValue;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (!(newValue instanceof Integer))
			return;
		mValue = (Integer) newValue;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		handleEvent(parent, position);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		handleEvent(parent, position);
		return false;
	}

	private void handleEvent(AdapterView<?> parent, int newValue) {
		if (!getView().equals(parent))
			return;
		mValue = newValue;
		this.notifyChanged();
	}
}
