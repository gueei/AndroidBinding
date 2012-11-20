package gueei.binding.pojo;

import gueei.binding.BindingLog;
import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.IPropertyContainer;
import gueei.binding.Observable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Hashtable;

import android.view.View;


public class PojoViewModelWrapper<T extends PojoViewModel> implements IPropertyContainer {
	private final T mViewModel;
	private final Hashtable<String, PojoObservable<?>> observables =
		new Hashtable<String, PojoObservable<?>>();
	
	private final Hashtable<String, PojoCommand> commands =
		new Hashtable<String, PojoCommand>();
	
	private PojoViewModelWrapper(T viewModel){
		mViewModel = viewModel;
		attachToViewModel();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void attachToViewModel() {
		try{
			for(Method m: mViewModel.getClass().getMethods()){
				String methodName = m.getName();
				// Find out all getters. We suppose getters must be in this format getPropertyName
				// And it must be argument-less
				if ((methodName.startsWith("get")) && (methodName.length()>=3)){
					String propertyName = methodName.substring(3);
					// Ignore getClass
					if (propertyName.equals("Class")) continue;
					
					// So we have a getter, now look for the setter (if existed)
					// setter is in setPropertyName format
					// where it takes one parameter, and it must be the same as the getter parameter
					Class<?> type = m.getReturnType();
					Method setter = null;
					try{
						 setter = mViewModel.getClass().getMethod("set" + propertyName, type);
					}catch(NoSuchMethodException nse){
						// OK, it is read-only
						setter = null;
					}
					observables.put(propertyName, new PojoObservable(type, m, setter));
				}else{
					// It's not a getter, see if it can be a command
					// A command must be argument-less public method
					if (m.getParameterTypes().length>0) continue;
					// Should not have any return (void)
					if (m.getReturnType()!= void.class) continue;
					commands.put(methodName, new PojoCommand(m));
				}
			}
		}catch(Exception e){
			BindingLog.exception("PojoViewModelWrapper.attachToViewModel", e);
		}
	}

	public void onPropertyChanged(String name){
		if (observables.containsKey(name)){
			observables.get(name).onPropertyChanged();
		}
	}
	
	public static <Tv extends PojoViewModel> PojoViewModelWrapper<Tv> create(Tv viewModel){
		PojoViewModelWrapper<Tv> wrapper =  new PojoViewModelWrapper<Tv>(viewModel);
		viewModel.getHelper().registerWrapper(wrapper);
		return wrapper;
	}
	
	public IObservable<?> getObservableByName(String name) throws Exception {
		return observables.get(name);
	}

	public Command getCommandByName(String name) throws Exception {
		return commands.get(name);
	}

	public Object getValueByName(String name) throws Exception {
		return null;
	}
	
	private class PojoCommand extends Command{
		private final Method mMethod;
		public PojoCommand(Method method){
			mMethod = method;
		}
		public void Invoke(View view, Object... args) {
			try {
				mMethod.invoke(mViewModel);
			} catch (Exception e) {
				BindingLog.exception("PojoViewModelWrapper.PojoCommand.Invoke", e);
			}
		}
	}
	
	private class PojoObservable<To> extends Observable<To>{
		private final Method mGetter, mSetter;
		
		// prevent loop back recursion of setting
		private boolean ignoreNext = false;
		public PojoObservable(Class<To> type, Method getter, Method setter) {
			super(type);
			mGetter= getter;
			mSetter = setter;
		}

		public void onPropertyChanged(){
			if (!ignoreNext){
				this.notifyChanged();
			}
			ignoreNext = false;
		}
		
		@Override
		protected void doSetValue(To newValue,
				Collection<Object> initiators) {
			super.doSetValue(newValue, initiators);
			if (mSetter==null) return; // Readonly
			try{
				ignoreNext = true;
				mSetter.invoke(mViewModel, newValue);
			}catch(Exception e){
				BindingLog.exception("PojoViewModelWrapper.PojoObservable.doSetValue", e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public To get() {
			try {
				return (To)mGetter.invoke(mViewModel);
			} catch (Exception e) {
				BindingLog.exception("PojoViewModelWrapper.PojoObservable.get", e);
				return null;
			}
		}
	}
}
