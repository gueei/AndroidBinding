package gueei.binding.utility;

import gueei.binding.Command;
import gueei.binding.IObservable;

public interface IModelReflector {
	public abstract Command getCommandByName(String name, Object object)
			throws Exception;

	public abstract IObservable<?> getObservableByName(String name,
			Object object) throws Exception;

	public abstract Object getValueByName(String name, Object object)
			throws Exception;

	public abstract Class<?> getValueTypeByName(String name);
}