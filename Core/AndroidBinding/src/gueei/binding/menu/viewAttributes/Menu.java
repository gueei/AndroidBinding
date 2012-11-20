package gueei.binding.menu.viewAttributes;

import gueei.binding.ViewAttribute;
import gueei.binding.menu.BindableOptionsMenu;

public class Menu extends ViewAttribute<BindableOptionsMenu, Integer> {
	public Menu(BindableOptionsMenu view) {
		super(Integer.class, view, "Menu");
	}

	private int mId = -1;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Integer){
			int newId = (Integer)newValue;
			if (newId != mId){
				mId = newId;
				getHost().invalidate();
			}
		}else{
			mId = -1;
		}
	}

	@Override
	public Integer get() {
		return mId;
	}
}
