package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.Color;
import android.widget.TextView;


public class TextColorViewAttribute extends ViewAttribute<TextView, Integer> {

	public TextColorViewAttribute(TextView view) {
		super(Integer.class, view, "backgroundColor");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setTextColor(Color.RED);
			return;
		}
		if (newValue instanceof Integer){
			getView().setTextColor((Integer)newValue);
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
