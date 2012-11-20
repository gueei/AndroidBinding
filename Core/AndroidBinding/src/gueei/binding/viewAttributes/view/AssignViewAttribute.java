package gueei.binding.viewAttributes.view;

import java.util.ArrayList;
import java.util.Collection;

import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import android.view.View;

/**
 * Assign a value BACK to the view model
 * @author andy
 *
 */
public class AssignViewAttribute extends ViewAttribute<View, DynamicObject> {

	IObservable<?> prop, val;
	
	public AssignViewAttribute(View view) {
		super(DynamicObject.class, view, "assign");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null || !(newValue instanceof DynamicObject)){
			return;
		}
		DynamicObject value = (DynamicObject)newValue;
		try{
			prop = value.getObservableByName("prop");
			val = value.getObservableByName("value");
			val.subscribe(new Observer(){
				@Override
				public void onPropertyChanged(IObservable<?> prop,
						Collection<Object> initiators) {
					prop._setObject(val.get(), initiators);
				}
			});
			ArrayList<Object> initiators = new ArrayList<Object>();
			initiators.add(this);
			initiators.add(value);
			prop._setObject(val.get(), initiators);
		}catch(Exception e){}
	}

	@Override
	public DynamicObject get() {
		return null;
	}
}
