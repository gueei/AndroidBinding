package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AdapterView;
import gueei.binding.Binder;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnItemSelectedViewEvent extends ViewEventAttribute<AdapterView<?>>
	implements AdapterView.OnItemSelectedListener{

	public OnItemSelectedViewEvent(AdapterView<?> view) {
		super(view, "onItemSelected");
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		this.invokeCommand(arg0,  arg1, arg2, arg3);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		this.invokeCommand(arg0);
	}

	@Override
	protected void registerToListener(AdapterView<?> view) {
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class).register(this);
	}
}
