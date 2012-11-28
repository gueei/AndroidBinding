package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Typeface of Text View
 * 
 * @name typeface
 * @widget TextView
 * @type Typeface
 * 
 * @accepts	Typeface

 * @category simple
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @converter TYPEFACE_FROM_ASSET
 * 
 * @author andy
 */
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
