package gueei.binding.viewAttributes.view;

import gueei.binding.Command;
import gueei.binding.ViewAttribute;
import android.view.View;

public class OnBindViewAttribute extends ViewAttribute<View, Object> {

	public OnBindViewAttribute(View view) {
		super(Object.class, view, "onBind");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null)
			return;
		
		if (newValue instanceof Command){
			Command cmd = (Command) newValue;
			cmd.Invoke(getView());
		}
	}

	@Override
	public Object get() {
		return null;
	}

}
