package gueei.binding.viewAttributes.view;

import gueei.binding.Binder;
import gueei.binding.listeners.OnClickListenerMulticast;
import gueei.binding.utility.EventMarkerHelper;
import gueei.binding.viewAttributes.ViewEventAttribute;
import gueei.binding.viewAttributes.adapterView.listView.ItemViewEventMark;
import android.view.View;

public class OnClickViewEvent extends ViewEventAttribute<View> implements View.OnClickListener {
	public OnClickViewEvent(View view) {
		super(view, "onClick");
	}

	public void onClick(View v) {
		invokeCommand(v);
	}

	@Override
	protected void registerToListener(View view) {
		Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class).register(this);
		ItemViewEventMark mark = EventMarkerHelper.getMark(view);
		if (null != mark) {
			Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class).registerWithHighPriority(mark);
		}
	}
}
