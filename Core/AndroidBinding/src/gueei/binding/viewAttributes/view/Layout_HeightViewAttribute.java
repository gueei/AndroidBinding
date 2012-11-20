package gueei.binding.viewAttributes.view;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class Layout_HeightViewAttribute extends ViewAttribute<View, Integer> {

	public Layout_HeightViewAttribute(View view) {
		super(Integer.class, view, "layout_height");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		LayoutParams param = getView().getLayoutParams();
		if (param==null) return;
		
		if (newValue==null){
			return;
		}

		int newHeight = 0;
		
		if (newValue instanceof Integer){
			newHeight = (Integer)newValue;
		} else if ("wrap_content".equals(newValue)){
			newHeight = LayoutParams.WRAP_CONTENT;
		} else if ("fill_parent".equals(newValue)){
			newHeight = LayoutParams.FILL_PARENT;
		}
		
		param.height = newHeight;
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
		return param.height; 
	}
}
