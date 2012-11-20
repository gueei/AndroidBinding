package gueei.binding.viewAttributes.view;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class Layout_WidthViewAttribute extends ViewAttribute<View, Integer> {

	public Layout_WidthViewAttribute(View view) {
		super(Integer.class, view, "layout_width");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		LayoutParams param = getView().getLayoutParams();
		if (param==null) return;
		
		if (newValue==null){
			return;
		}

		int newWidth = 0;
		
		if (newValue instanceof Integer){
			newWidth = (Integer)newValue;
		} else if ("wrap_content".equals(newValue)){
			newWidth = LayoutParams.WRAP_CONTENT;
		} else if ("fill_parent".equals(newValue)){
			newWidth = LayoutParams.FILL_PARENT;
		}
		
		param.width = newWidth;
		getView().setLayoutParams(param);
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Integer get() {
		if(getView()==null) return null;
		LayoutParams param = getView().getLayoutParams();
		if (param==null) return 0;
		return param.width; 
	}
}
