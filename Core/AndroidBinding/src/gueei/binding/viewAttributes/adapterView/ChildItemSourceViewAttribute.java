package gueei.binding.viewAttributes.adapterView;

import android.widget.ExpandableListView;
import gueei.binding.ViewAttribute;

public class ChildItemSourceViewAttribute extends ViewAttribute<ExpandableListView, String> {

	public ChildItemSourceViewAttribute(ExpandableListView view) {
		super(String.class, view, "childItemSource");
	}

	private String mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue!=null)
			mValue = newValue.toString();
	}

	@Override
	public String get() {
		return mValue;
	}

}
