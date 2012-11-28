package gueei.binding.viewAttributes.compoundButton;

import android.widget.CompoundButton;
import gueei.binding.Binder;
import gueei.binding.listeners.OnCheckedChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

/**
 * Fires when checked state changed. 
 * Note change may come from user or from change of observed properties 
 * 
 * @name checked
 * @widget CompoundButton
 * @type Command
 * 
 * @accepts	Command
 * 
 * @category simple
 * @related http://developer.android.com/reference/android/widget/CompoundButton.html
 * 
 * @author andy
 */
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
