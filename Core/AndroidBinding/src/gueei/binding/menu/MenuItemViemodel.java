package gueei.binding.menu;

import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;

public class MenuItemViemodel {
	public Command onClick;
	public IObservable<?> title;
	public IObservable<?> visible;
	public IObservable<?> enabled;
	public IObservable<?> checked;
	public IObservable<?> icon;
	public IObservableCollection<MenuItemViemodel> group;
}
