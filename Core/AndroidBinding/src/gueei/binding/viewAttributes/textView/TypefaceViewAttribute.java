package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.Typeface;
import android.widget.TextView;


public class TypefaceViewAttribute extends ViewAttribute<TextView, Typeface> {

	public TypefaceViewAttribute(TextView view) {
		super(Typeface.class, view, "typeface");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			return;
		}
		if (newValue instanceof Typeface){
			getView().setTypeface((Typeface)newValue);
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Typeface get() {
		return null;
	}
}
