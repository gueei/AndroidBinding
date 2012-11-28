package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AdapterView;
import gueei.binding.Binder;
import gueei.binding.listeners.OnItemLongClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

/**
 * Fires when Item Long-Clicked
 * Equals to listening to AdapterView.OnItemLongClickListener.onItemLongClick()
 * 
 * @name onItemLongClicked
 * @widget ListView
 * @type Command
 * @accepts	Command
 * @category list
 * @related 
 * 
 * @author andy

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
