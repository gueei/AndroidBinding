package gueei.binding.collections;

import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.Context;
import android.widget.Adapter;
import android.widget.Filter;


public class Utility {
	@SuppressWarnings({ "rawtypes" })
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			Layout layout, Layout dropDownLayout, Filter filter, String enableItemStatement) throws Exception{
		if ((collection instanceof IObservableCollection)){
			IObservableCollection obsCollection = (IObservableCollection)collection;
			return new CollectionAdapter(
					context, 
					obsCollection, 
					layout, 
					dropDownLayout,
					filter, enableItemStatement);
		}
		if (collection instanceof IObservable){
			Class<?> clazz = ((IObservable)collection).getType();
			if (IObservableCollection.class.isAssignableFrom(clazz)){
				return new CollectionAdapter(
						context,
						(IObservableCollection)(((IObservable)collection).get()),
						layout,
						dropDownLayout,
						filter, enableItemStatement);
			}
		}
		return null;
	}
	
	public static Adapter getSimpleAdapter(
			Context context, Object collection, 
			Layout layout, Layout dropDownLayout, Filter filter) throws Exception{
		return getSimpleAdapter(context, collection, layout, dropDownLayout, filter, null);
	}
}
