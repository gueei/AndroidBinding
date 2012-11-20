package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.LayoutItem;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

/**
 * ITEM_LAYOUT this adapter is used for bindable linear layouts
 *
 */
public class ITEM_LAYOUT extends Converter<LayoutItem> {

	public ITEM_LAYOUT(IObservable<?>[] dependents) {
		super(LayoutItem.class, dependents);
	}

	@Override
	public LayoutItem calculateValue(Object... args) throws Exception {		
		if (args.length<1) return null;
		
		LayoutItem layout = null;		
		if( args[0] instanceof SingleTemplateLayout) {
			layout = new LayoutItem(((SingleTemplateLayout)args[0]).getDefaultLayoutId());
		} else if (args[0] != null) {
			layout = new LayoutItem(args[0].toString());			
		}
		
		return layout;
	}
	
}
