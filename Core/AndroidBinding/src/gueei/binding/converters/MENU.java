package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.menu.ContextMenuBinder;

/**
 * Use exclusively for contextMenu attribute
 * 
 * @usage menu_xml_id model
 * 
 * @arg menu_xml integer XML id of the context menu, e.g. @menu/context_menu
 * @arg model Object View model to bind to the menu, most probably it will be just "."
 * 
 * @return gueei.binding.menu.ContextMenuBinder
 * 
 * @author andy
 *
 */
public class MENU extends Converter<ContextMenuBinder>{

	public MENU(IObservable<?>[] dependents) {
		super(ContextMenuBinder.class, dependents);
	}

	private ContextMenuBinder menuBinder;
	
	/**
	 * Requires 2 arguments: 
	 * 1: XML id of the menu
	 * 2: View Model (most of the case, it would be .)
	 */
	@Override
	public ContextMenuBinder calculateValue(Object... args) throws Exception {
		if (args.length<2) return null;
		if (!(args[0] instanceof Integer)) return null;
		if (menuBinder==null){
			menuBinder = new ContextMenuBinder((Integer)args[0], args[1]);
			return menuBinder;
		}
		menuBinder.setMenuResId((Integer)args[0]);
		menuBinder.setViewModel(args[1]);
		return menuBinder;
	}
}
