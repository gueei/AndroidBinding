package gueei.binding;


public interface IPropertyContainer {
	public IObservable<?> getObservableByName(String name) throws Exception;
	public Command getCommandByName(String name) throws Exception;
	public Object getValueByName(String name) throws Exception;
}
