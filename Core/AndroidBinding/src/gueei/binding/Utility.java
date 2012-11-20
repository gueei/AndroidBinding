package gueei.binding;

import gueei.binding.ISyntaxResolver.SyntaxResolveException;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;

public class Utility {
	public static BindingMap createBindingMap(AttributeSet attrs){
		BindingMap map = new BindingMap();
		int count = attrs.getAttributeCount();
		for(int i=0; i<count; i++){
			String attrName = attrs.getAttributeName(i);
			String attrValue = attrs.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
			if (attrValue!=null){
				map.put(attrName, attrValue);
			}
		}
		return map;
	}

	public static Object getFieldForModel(String fieldName, Object model){
		try{
			if (model instanceof IPropertyContainer){
				return ((IPropertyContainer)model).getValueByName(fieldName);
			}
			Field field = model.getClass().getField(fieldName);
			return field.get(model);
		}catch(Exception e){
			return null;
		}
	}
	
	public static IObservable<?> getObservableForModel(Context context, String fieldName, Object model){
		try {
			return Binder.getSyntaxResolver().constructObservableFromStatement(context, fieldName, model);
		} catch (SyntaxResolveException e) {
			BindingLog.exception("Utiltiy.getObservableForModel()", e);
			return null;
		}
	}

	public static Command getCommandForModel(String fieldName, Object model){
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getCommandByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof Command)
			return (Command)rawField;
		return null;
	}
	
	public static int resolveLayoutResource(String attrValue, Context context){
		if (!attrValue.startsWith("@")) return -1;
		String name = attrValue.substring(1); // remove the @ sign
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
	
	public static int resolveResourceId(String attrValue, Context context, String type){
		String name = attrValue; // remove the @ sign
		if (attrValue.startsWith("@"))
			name = attrValue.substring(1); // remove the @ sign
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}
	
	/**
	 * Evaluate the value of the provided item
	 * If it is a plain item, it's value is returned, 
	 * if it is an Observable, it's get() is returned
	 * @param item
	 * @return
	 */
	public static Object evalValue(Object item){
		if (item instanceof IObservable) return ((IObservable<?>)item).get();
		return item;
	}
	
	/**
	 * Evaluate the value of item and cast to the desired type
	 * @param item
	 * @param expectedType
	 * @return
	 */
	public static <T> T evalValue(Object item, Class<T> expectedType){
		Object val = evalValue(item);
		if (val==null) return null;
		if (expectedType.isAssignableFrom(val.getClass())) return expectedType.cast(val);
		return null;
	}
}
