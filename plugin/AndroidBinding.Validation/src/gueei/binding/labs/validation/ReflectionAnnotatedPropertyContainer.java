package gueei.binding.labs.validation;

import gueei.binding.IObservable;
import gueei.binding.Observable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Hashtable;

public class ReflectionAnnotatedPropertyContainer implements IAnnoatedPropertyContainer {

	protected Hashtable<String, Field> cachedObservableFields =
			new Hashtable<String, Field>();
	
	final Object model;
	
	public ReflectionAnnotatedPropertyContainer(Object model){
		Class<?> clazz = model.getClass();
		for (Field field : clazz.getFields()){
			try{
				if (!(field.get(model) instanceof Observable)) continue;
				cachedObservableFields.put(field.getName(), field);
			}catch(Exception e){
				// ignore any error
				continue;
			}
		}
		this.model = model;
	}
	
	@Override
	public String[] getAllObservableNames() {
		return cachedObservableFields.keySet().toArray(new String[0]);
	}
	
	public IObservable<?> getObservable(String observableName) throws Exception{
		if (!cachedObservableFields.containsKey(observableName)) throw new IllegalArgumentException();

		return (IObservable<?>)cachedObservableFields.get(observableName).get(model);
	}
	
	@Override
	public Annotation[] getAnnotations(String observableName) throws Exception {
		if (!cachedObservableFields.containsKey(observableName)) throw new IllegalArgumentException();
		
		Field field = cachedObservableFields.get(observableName);
		return field.getAnnotations();
	}

}