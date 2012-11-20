package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.view.View;
import android.widget.AdapterView;

public class OnItemClickedViewEvent extends ViewEventAttribute<AdapterView<?>>
	implements AdapterView.OnItemClickListener{

	public OnItemClickedViewEvent(AdapterView<?> view) {
		super(view, "onItemClicked");
	}

	@Override
	protected void registerToListener(AdapterView<?> view) {
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class).register(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		this.invokeCommand(arg0,  arg1, arg2, arg3);
	}
}
