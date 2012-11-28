package gueei.binding.viewAttributes.view;

import java.util.ArrayList;
import java.util.Collection;

import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import android.view.View;

/**
 * Assign a value back to View Model
 * In MVVM, ideally we don't want to let View Model to know much about display logic, 
 * but the layout XML is supposed to aware of different device configuration
 * 
 * for example, we can write
 * binding:assign="{ displayInNewActivity=TRUE() }"
 * 
 * so, in the view model, the displayInActivity will set to true.
 * 
 * In this way, we can free the view model from trying to detect what display configuration it is in,
 * because XML layout/resources can be prepared with different configurations (e.g. -land, -port, -v14 etc)
 *
 * You can assign to multiple properties, since the accepted parameter is dynamic object:
 * 
 * e.g. binding:assign="{ propA = 'AValue', propB = 'BValue', propC = 'CValue' }" 
 * 
 * Note: This is currently only attribute that is not related to any view
 *  
 * @name assign
 * @widget View
 * @type gueei.binding.DynamicObject
 * 
 * @accepts	gueei.binding.DynamicObject

 * @category simple special
 * 
 * @author andy
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
