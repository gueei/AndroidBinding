package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.widget.TextView;

/**
 * Base text color, typeface, size, and style of a textView
 * Similar to android:textAppearance
 * 
 * @name textAppearance
 * @widget TextView
 * @type Integer
 * 
 * @accepts	Integer

 * @category simple color
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @author egandro
 */
public class TextAppearanceViewAttribute extends ViewAttribute<TextView, Integer> {

	public TextAppearanceViewAttribute(TextView view) {
		super(Integer.class, view, "textAppearance");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof Integer){
			getView().setTextAppearance(getView().getContext(),(Integer)newValue);
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
