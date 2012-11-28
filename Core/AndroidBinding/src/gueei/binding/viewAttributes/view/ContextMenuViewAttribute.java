package gueei.binding.viewAttributes.view;

import gueei.binding.ViewAttribute;
import gueei.binding.menu.ContextMenuBinder;
import android.view.View;

/**
 * Context Menu attribute
 * binds to View.onCreateContextMenu()
 * 
 * this is not designed to use alone, check the related "MENU" converter
 * 
 * 
 * @name contextMenu
 * @widget View
 * @type ContextMenuBinder
 * 
 * @accepts	ContextMenuBinder
 * 
 * @category simple
 * @related http://developer.android.com/reference/android/widget/View.html
 * @related http://developer.android.com/reference/android/graphics/Color.html
 * @converter MENU
 * 
 * @author andy
 */
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
