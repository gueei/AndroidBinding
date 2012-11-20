package gueei.binding.viewAttributes.viewAnimator;


import gueei.binding.ViewAttribute;
import android.widget.ViewAnimator;


public class DisplayedChildViewAttribute extends ViewAttribute<ViewAnimator, Integer>{

	public DisplayedChildViewAttribute(ViewAnimator view) {
		super(Integer.class, view, "displayedChild");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		int totalView = getView().getChildCount();
		if (newValue == null){
			return;
		}
		if (newValue instanceof Integer){
			int newInt = (Integer)newValue;
			if (newInt < totalView)
				getView().setDisplayedChild(newInt);
			return;
		}
	}

	@Override
	public Integer get() {
		if(getView()==null) return null;
		return getView().getDisplayedChild();
	}
}