package gueei.binding;

import android.view.View;

public abstract class ViewAttribute<Tv extends View, T> extends Attribute<Tv, T> {
	public ViewAttribute(Class<T> type, Tv view, String attributeName) {
		super(type, view, attributeName);
	}
	
	public Tv getView(){
		return super.getHost();
	}
}