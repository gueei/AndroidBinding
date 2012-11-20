package gueei.binding.viewAttributes.templates;

import gueei.binding.Observable;

public class LayoutObservable extends Observable<Layout> {

	public LayoutObservable(Layout initValue) {
		super(Layout.class, initValue);
	}

	public LayoutObservable() {
		super(Layout.class);
	}
}
