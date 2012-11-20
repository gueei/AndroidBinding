package gueei.binding.viewAttributes.compoundButton;

import android.widget.CompoundButton;
import gueei.binding.Binder;
import gueei.binding.listeners.OnCheckedChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnCheckedChangeViewEvent extends ViewEventAttribute<CompoundButton>
	implements CompoundButton.OnCheckedChangeListener{

	public OnCheckedChangeViewEvent(CompoundButton view) {
		super(view, "onCheckedChange");
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		this.invokeCommand(buttonView, isChecked);
	}

	@Override
	protected void registerToListener(CompoundButton view) {
		Binder.getMulticastListenerForView(view, OnCheckedChangeListenerMulticast.class).register(this);
	}

}
