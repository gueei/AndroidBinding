package gueei.binding.viewAttributes.view;

import gueei.binding.ViewAttribute;
import gueei.binding.menu.ContextMenuBinder;
import android.view.View;

public class ContextMenuViewAttribute extends ViewAttribute<View, ContextMenuBinder>{

	private ContextMenuBinder mValue;
	
	public ContextMenuViewAttribute(View view) {
		super(ContextMenuBinder.class, view, "contextMenu");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		 if (newValue instanceof ContextMenuBinder){
			 mValue = (ContextMenuBinder)newValue;
			 getView().setOnCreateContextMenuListener(mValue);
		 }
	}

	@Override
	public ContextMenuBinder get() {
		return mValue;
	}
}
