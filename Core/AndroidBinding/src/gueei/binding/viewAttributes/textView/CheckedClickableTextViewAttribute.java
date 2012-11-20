package gueei.binding.viewAttributes.textView;


import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnClickListenerMulticast;
import android.view.View;
import android.widget.CheckedTextView;


public class CheckedClickableTextViewAttribute extends ViewAttribute<CheckedTextView, Boolean>
	implements View.OnClickListener {

	// we have to use the click handler here, because there is no Checked Listener for CheckedTextViews	
	
	public CheckedClickableTextViewAttribute(CheckedTextView view) {
		super(Boolean.class, view, "checkedClickable");
		Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		boolean changeTo = getView().isChecked();
		if (newValue==null){
			changeTo = false;
		}
		if (newValue instanceof Boolean){
			changeTo = (Boolean)newValue;
		}
		if (newValue instanceof Number){
			changeTo = !((Number)newValue).equals(0);
		}
		if (changeTo != getView().isChecked()){
			Binder.getMulticastListenerForView(getView(), OnClickListenerMulticast.class).nextActionIsNotFromUser();
			getView().setChecked(changeTo);
		}
	}

	@Override
	public Boolean get() {
		if(getView()==null) return null;
		return getView().isChecked();
	}

	@Override
	public void onClick(View v) {
		if( !getView().isEnabled() )
			return;
		boolean changeTo = getView().isChecked();
		getView().setChecked(!changeTo);
		this.notifyChanged();		
	}
}