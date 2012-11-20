package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.collections.Utility;
import gueei.binding.viewAttributes.templates.Layout;
import android.widget.Adapter;
import android.widget.Filter;

/**
 * ADAPTER accepts only one argument, and it must be DynamicObject
 * It will return a simple adapter, the same parameters to gueei.binding.collections.Utility.getSimpleAdapter:
 * 
 * @usage params
 * 
 * @arg params DynamicObject
 * 
 * @item template gueei.binding.viewAttributes.templates.Layout
 * @item source Object
 * @item @optional spinnerTemplate gueei.binding.viewAttributes.templates.Layout
 * @item @optional enable String child property that determines the enabled state of the item
 * @item @optional filter android.widget.Filter filter supplies to adapter
 * 
 * @return android.widget.Adapter
 * 
 * @author andy
 *
 */
public class ADAPTER extends Converter<Adapter> {
	public ADAPTER(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		try{
			DynamicObject object = (DynamicObject)args[0];
			if (!object.observableExists("template")) return null;
			if (!object.observableExists("source")) return null;
			Layout template = ((Layout)object.getObservableByName("template").get());
			Layout spinnerTemplate = null;
			if (object.observableExists("spinnerTemplate"))
				spinnerTemplate = (Layout)object.getObservableByName("spinnerTemplate").get();
			
			spinnerTemplate = spinnerTemplate == null ? template : spinnerTemplate;
			
			IObservable<?> source = object.getObservableByName("source");
			
			String enableStatement = null;
			if (object.observableExists("enable")){
				try{
					enableStatement = object.getObservableByName("enable").get().toString();
				}catch(Exception e){}
			}
			Filter filter = null;
			if (object.observableExists("filter")){
				try{
					filter = (Filter)(object.getObservableByName("filter").get());
				}catch(Exception e){}
			}
			return Utility.getSimpleAdapter(getContext(), source, template, spinnerTemplate, filter, enableStatement);
		}catch(Exception e){
			return null;
		}
	}
}
