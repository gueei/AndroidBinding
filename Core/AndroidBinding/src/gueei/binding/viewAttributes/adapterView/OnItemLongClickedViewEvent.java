package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AdapterView;
import gueei.binding.Binder;
import gueei.binding.listeners.OnItemLongClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

/**
 * User: =ra=
 * Date: 28.07.11
 * Time: 17:27
 */
public class OnItemLongClickedViewEvent extends ViewEventAttribute<AdapterView<?>>
		implements AdapterView.OnItemLongClickListener {

	public OnItemLongClickedViewEvent(AdapterView<?> view) {
		super(view, "onItemLongClicked");
	}

	@Override protected void registerToListener(AdapterView<?> view) {
		Binder.getMulticastListenerForView(view, OnItemLongClickListenerMulticast.class).register(this);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		this.invokeCommand(parent, view, position, id);
		return true;
	}
}
