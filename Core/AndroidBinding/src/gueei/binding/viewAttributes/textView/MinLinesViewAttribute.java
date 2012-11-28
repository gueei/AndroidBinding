package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.widget.TextView;

/**
 * Min Lines of Text View
 * Similar to android:minLines
 * 
 * @name minLines
 * @widget TextView
 * @type Integer
 * 
 * @accepts	Integer

 * @category simple
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @author andy
 */
public class MinLinesViewAttribute extends ViewAttribute<TextView, Integer> {
	public MinLinesViewAttribute(TextView view) {
		super(Integer.class, view, "minLines");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setMaxLines(1);
			return;
		}
		if (newValue instanceof Integer){
			getView().setMinLines((Integer)newValue);
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
