package gueei.binding.plugin.abs.menu;

import gueei.binding.IObservable;

public interface IMenuItemChangedCallback {
	public void onItemChanged(IObservable<?> prop, AbsMenuBridge item);
}
