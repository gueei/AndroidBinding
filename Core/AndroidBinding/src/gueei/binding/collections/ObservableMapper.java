package gueei.binding.collections;

import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.IPropertyContainer;
import gueei.binding.Observable;
import gueei.binding.Observer;
import gueei.binding.utility.IModelReflector;

import java.util.Collection;
import java.util.HashMap;


public class ObservableMapper implements IPropertyContainer {
	@SuppressWarnings("rawtypes")
	public HashMap<String, MockObservable> observableMapping = new HashMap<String, MockObservable>();

	@SuppressWarnings("rawtypes")
	public HashMap<String, Observable> valueMapping = new HashMap<String, Observable>();
	public int mappedPosition;
	private Object mappingModel;

	public Object getCurrentMapping(){
		return mappingModel;
	}
	
	@SuppressWarnings("unchecked")
	public void changeMapping(IModelReflector reflector, Object model){
		if (mappingModel!=null && mappingModel instanceof ILazyLoadRowModel)
			((ILazyLoadRowModel)mappingModel).setMapped(false);
		
		mappingModel = model;
		try {
			for(String key: observableMapping.keySet()){
				IObservable<?> obs = reflector.getObservableByName(key, model);
				observableMapping.get(key).changeObservingProperty(obs);
			}
			/*
			for(String key: commandMapping.keySet()){
				commandMapping.get(key).changeCommand(reflector.getCommandByName(key, model));
			}
			*/
			for(String key: valueMapping.keySet()){
				valueMapping.get(key).set(reflector.getValueByName(key, model));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mappingModel!=null && mappingModel instanceof ILazyLoadRowModel)
			((ILazyLoadRowModel)mappingModel).setMapped(true);
	}
	
	private IModelReflector mReflector = null;
	private Object mModel = null;
	public void startCreateMapping(IModelReflector reflector, Object model){
		mReflector = reflector;
		mModel = model;
	}
	
	public void endCreateMapping(){
		mReflector = null;
	}
	
	// Remember! This maps 1-1 to the real observable
	private static class MockObservable<T> extends Observable<T> implements Observer{
		public MockObservable(Class<T> type) {
			super(type);
		}

		public IObservable<T> observingProperty;
		public T get() {
			if (observingProperty!=null){
				return observingProperty.get();
			}
			return null;
		}

		public void changeObservingProperty(IObservable<T> newProperty){
			if (observingProperty!=null){
				observingProperty.unsubscribe(this);
			}
			newProperty.subscribe(this);
			observingProperty = newProperty;
			this.notifyChanged(this);
		}
		
		public void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
			if (prop!=observingProperty){
				prop.unsubscribe(this);
				return;
			}
			initiators.add(this);
			this.notifyChanged(initiators);
		}

		@Override
		protected void doSetValue(T newValue,
				Collection<Object> initiators) {
			if (observingProperty!=null){
				observingProperty.set(newValue, initiators);
			}
		}
	}
/*	
	private class MockCommand implements Command{
		private WeakReference<Command> command;
		public void Invoke(View view, Object... args) {
			if (command.get()!=null){
				if (command.get() instanceof CollectionCommand){
					((CollectionCommand)command.get()).Invoke(mappedPosition);
				}else command.get().Invoke(view, mappedPosition, args);
			}
		}
		public void changeCommand(Command newCommand){
			command = new WeakReference<Command>(newCommand);
		}
	}
	
	public Command getCommandByName(String name) throws Exception{
		if ((!commandMapping.containsKey(name)) && (mReflector!=null)){
			MockCommand mCommand = new MockCommand();
			Command rCommand;
			rCommand = mReflector.getCommandByName(name, mModel);
			if (rCommand !=null){
				commandMapping.put(name, mCommand);
			}
		}
		return commandMapping.get(name);
	}
*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IObservable<?> getObservableByName(String name) throws Exception {
		if ((!observableMapping.containsKey(name)) && (mReflector!=null)){
			IObservable<?> obs = mReflector.getObservableByName(name, mModel);
			MockObservable mObservable = new MockObservable(obs.getType());
			if (mObservable !=null){
				observableMapping.put(name, mObservable);
			}
		}
		return observableMapping.get(name);
	}

	public Object getValueByName(String name) throws Exception {
		return null;
	}

	public Command getCommandByName(String name) throws Exception {
		return null;
	}
}