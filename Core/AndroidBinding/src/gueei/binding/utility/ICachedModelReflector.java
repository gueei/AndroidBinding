package gueei.binding.utility;

import java.lang.reflect.Field;
import java.util.HashMap;


public interface ICachedModelReflector<T> extends IModelReflector {

	public abstract HashMap<String, Field> getObservables();

	public abstract HashMap<String, Field> getCommands();

	public abstract HashMap<String, Field> getValues();

	// TODO: andy i can't compile with this line
	/*
	public abstract Command getCommandByName(String name, T object)
			throws Exception;
	*/
}