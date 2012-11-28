package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.widget.TextView;

/**
 * Max Lines of Text View
 * Similar to android:maxLines
 * 
 * @name maxLines
 * @widget TextView
 * @type Integer
 * 
 * @accepts	Integer

 * @category simple
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @author andy
 */
public class MaxLinesViewAttribute extends ViewAttribute<TextView, Integer> {
	public MaxLinesViewAttribute(TextView view) {
		super(Integer.class, view, "maxLines");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setMaxLines(1);
			return;
		}
		if (newValue instanceof Integer){
			getView().setMaxLines((Integer)newValue);
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Integer get() {
		return null;
	}
}
