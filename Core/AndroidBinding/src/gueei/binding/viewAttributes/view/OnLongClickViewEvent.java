package gueei.binding.viewAttributes.view;

import gueei.binding.Binder;
import gueei.binding.listeners.OnLongClickListenerMulticast;
import gueei.binding.utility.EventMarkerHelper;
import gueei.binding.viewAttributes.ViewEventAttribute;
import gueei.binding.viewAttributes.adapterView.listView.ItemViewEventMark;
import android.view.View;

public class OnLongClickViewEvent extends ViewEventAttribute<View> implements View.OnLongClickListener {

	public OnLongClickViewEvent(View view) {
		super(view, "onLongClick");
	}

	public boolean onLongClick(View v) {
		this.invokeCommand(v);
		return true;
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnLongClickListenerMulticast.class).register(this);
		ItemViewEventMark mark = EventMarkerHelper.getMark(view);
		if (null != mark) {
			Binder.getMulticastListenerForView(view, OnLongClickListenerMulticast.class).registerWithHighPriority(mark);
		}
	}
}
