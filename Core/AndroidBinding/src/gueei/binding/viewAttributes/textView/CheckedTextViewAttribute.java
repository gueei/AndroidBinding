package gueei.binding.viewAttributes.textView;

import gueei.binding.ViewAttribute;
import android.widget.CheckedTextView;

public class CheckedTextViewAttribute extends ViewAttribute<CheckedTextView, Boolean> {
	public CheckedTextViewAttribute(CheckedTextView view) {
		super(Boolean.class, view, "checked");
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
			getView().setChecked(changeTo);
		}
	}

	@Override
	public Boolean get() {
		if(getView()==null) return false;
		return getView().isChecked();
	}
}