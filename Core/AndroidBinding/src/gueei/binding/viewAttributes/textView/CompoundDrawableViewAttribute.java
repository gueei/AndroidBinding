package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.drawable.Drawable;
import android.widget.TextView;


public class CompoundDrawableViewAttribute extends ViewAttribute<TextView, Object> {

	public CompoundDrawableViewAttribute(TextView view, String type) {
		super(Object.class, view, type);
	}
	
	Drawable mValue = null;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof Integer){
			int value = (Integer)newValue;
			if( value != 0 )
				mValue = getView().getContext().getResources().getDrawable(value);
			else
				mValue = null;
		} else if (newValue instanceof Drawable){
			mValue = (Drawable)newValue;
		} else {
			mValue = null;
		}

		//getCompoundDrawables - drawables for the left, top, right, and bottom borders.		
		//setCompoundDrawablesWithIntrinsicBounds (Drawable left, Drawable top, Drawable right, Drawable bottom)
		
		if(attributeName.equals("drawableLeft")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					mValue, getView().getCompoundDrawables()[1], 
					getView().getCompoundDrawables()[2], getView().getCompoundDrawables()[3]);
		} else if(attributeName.equals("drawableTop")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], mValue, 
					getView().getCompoundDrawables()[2], getView().getCompoundDrawables()[3]);
		} else if(attributeName.equals("drawableRight")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], getView().getCompoundDrawables()[1], 
					mValue, getView().getCompoundDrawables()[3]);			
		} else if(attributeName.equals("drawableBottom")) {
			getView().setCompoundDrawablesWithIntrinsicBounds(
					getView().getCompoundDrawables()[0], getView().getCompoundDrawables()[1], 
					getView().getCompoundDrawables()[2], mValue);		
		}				
	}

	@Override
	public Object get() {
		return mValue;
	}
	
	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (Drawable.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		return BindingType.OneWay;
	}	
}
