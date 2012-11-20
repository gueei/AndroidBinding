package gueei.binding.viewAttributes.textView;

import gueei.binding.Binder;
import gueei.binding.listeners.TextWatcherMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
