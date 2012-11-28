package gueei.binding.viewAttributes.textView;

import gueei.binding.Binder;
import gueei.binding.listeners.TextWatcherMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
/**
 * Fires when Text Changed
 * This is to respond to event from TextWatcher
 * Note it is not guaranteed that the Change is from code or from user
 * 
 * @name onTextChanged
 * @widget TextView
 * @type Command
 * 
 * @accepts	Command

 * @category simple event
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @author andy
 */
public class OnTextChangedViewEvent extends ViewEventAttribute<EditText> implements TextWatcher{

	public OnTextChangedViewEvent(EditText view) {
		super(view, "onTextChanged");
	}

	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		this.invokeCommand(getView(), s, start, before, count);
	}

	@Override
	protected void registerToListener(EditText view) {
		Binder.getMulticastListenerForView(view, TextWatcherMulticast.class)
			.register(this);
	}
}
