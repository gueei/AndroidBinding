package gueei.binding.viewAttributes.view;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.graphics.drawable.Drawable;
import android.view.View;


public class BackgroundViewAttribute extends ViewAttribute<View, Object> {

	public BackgroundViewAttribute(View view) {
		super(Object.class, view, "background");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setBackgroundDrawable(null);
			return;
		}
		if (newValue instanceof Integer){
			getView().setBackgroundResource((Integer)newValue);
		}
		if (newValue instanceof Drawable){
			getView().setBackgroundDrawable((Drawable)newValue);
			return;
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Object get() {
		return null;
	}
}
