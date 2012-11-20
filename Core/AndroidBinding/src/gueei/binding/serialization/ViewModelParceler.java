package gueei.binding.serialization;

import gueei.binding.IObservable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;

public class ViewModelParceler {
	// Parcel all the 'Parcelable' Observables in the View Model
	public static Bundle parcelViewModel(Object viewModel){
		Bundle bundle = new Bundle();
		Field[] fields = viewModel.getClass().getFields();
		for(Field f : fields){
			try {
				Object fieldValue = f.get(viewModel);
				String fieldName = f.getName();
				if (fieldValue instanceof IObservable){
					if (f.getAnnotation(NoParcel.class)!=null) continue;
					
					IObservable<?> obs = (IObservable<?>)fieldValue;
					if (obs!=null){
						Object value = obs.get();
						if (value!=null){
							if (value instanceof Parcelable)
								//((Parcelable)value).
								bundle.putParcelable(fieldName, (Parcelable)value);
							else if (value instanceof Short)
								bundle.putShort(fieldName, (Short)value);
							else if (value instanceof Integer)
								bundle.putInt(fieldName, (Integer)value);
							else if(value instanceof Long)
								bundle.putLong(fieldName, (Long)value);
							else if (value instanceof Float)
								bundle.putFloat(fieldName, (Float)value);
							else if (value instanceof Double)
								bundle.putDouble(fieldName, (Double)value);
							else if (value instanceof String)
								bundle.putString(fieldName, (String)value);
							else if (value instanceof CharSequence)
								bundle.putCharSequence(fieldName, (CharSequence)value);
							else if (value instanceof Boolean)
								bundle.putBoolean(fieldName, (Boolean)value);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return bundle;
	}
	
	public static void restoreViewModel(Bundle bundle, Object viewModel){
		if (bundle==null) return;
		Field[] fields = viewModel.getClass().getFields();
		for(Field f : fields){
			try {
				if (!IObservable.class.isAssignableFrom(f.getType())) continue;
				String fieldName = f.getName();
				
				if (f.getAnnotation(NoParcel.class)!=null) continue;
				
				if (!bundle.containsKey(fieldName)) continue;
				Object fieldValue = f.get(viewModel);
				if (fieldValue != null){
					IObservable<?> obs = (IObservable<?>)fieldValue;
					Object obj = bundle.get(fieldName);
					obs._setObject(obj, new ArrayList<Object>());						
				}
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
		}
	}
}
