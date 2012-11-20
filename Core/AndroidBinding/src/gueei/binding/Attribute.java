package gueei.binding;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;

public abstract class Attribute<Th, T> extends Observable<T> {
	
	protected Th mHost;
	//protected WeakReference<Th> mHostRef;
	protected String attributeName;
	private boolean readonly = false;
	
	public Attribute(Class<T> type, Th host, String attributeName) {
		super(type);
		//this.mHostRef = new WeakReference<Th>(host);
		mHost = host;
		this.attributeName = attributeName;
	}
	
	public Th getHost(){
		return mHost;
	}
	/*
	@SuppressWarnings("unchecked")
	public final <To> void onPropertyChanged(IObservable<To> prop, 
			Collection<Object> initiators){
		set((T)prop.get(), initiators);
	}
	*/
	protected abstract void doSetAttributeValue(Object newValue);
	
	@Override
	protected void doSetValue(final T newValue, Collection<Object> initiators) {
		if (readonly) return;
		doSetAttributeValue(newValue);
		/*
		uiHandler.post(new Runnable(){
			public void run() {
				doSetAttributeValue(newValue);
			}
		});*/
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public void _setObject(final Object newValue, Collection<Object> initiators){
		if (readonly) return;
		doSetAttributeValue(newValue);
		initiators.add(this);
	}
	
	@Override
	public abstract T get();

	// Set to package internal for debug use
	Bridge mBridge;
	
	public BindingType BindTo(Context context, IObservable<?> prop) {
		if (prop == null) return BindingType.NoBinding;
		
		BindingType binding;
		
		// Dirty fix
		// Since for InnerFieldObservable, it may not know what type of it will be
		// So, it assumes two way no matter what
		if (prop instanceof Undetermined)
			binding = BindingType.TwoWay;
		else
			binding = AcceptThisTypeAs(prop.getType());
		
		if (binding.equals(BindingType.NoBinding)) return binding;
		
		onBind(context, prop, binding);
		
		return binding;
	}
	
	/*
	 * Hook to allow modifying of binding behavior in subclasses
	 */
	protected void onBind(Context context, IObservable<?> prop, BindingType binding){
		mBridge = new Bridge(this, prop);
		prop.subscribe(mBridge);
		if (binding.equals(BindingType.TwoWay)) this.subscribe(mBridge);

		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(prop);
		this._setObject(prop.get(), initiators);
		// Broadcast initial change
		notifyChanged(initiators);		
	}
	
	protected BindingType AcceptThisTypeAs(Class<?> type){
		if (this.getType() != type){
			if (this.getType().isAssignableFrom(type)){
				return BindingType.OneWay;
			}
			return BindingType.NoBinding;
		}
		return BindingType.TwoWay;
	}
	
	public IObservable<?> getBindedObservable(){
		if (mBridge==null) return null;
		return mBridge.mBindedObservable;
	}
	
	class Bridge implements Observer{
		Attribute<Th, T> mAttribute;
		IObservable<?> mBindedObservable;
		public Bridge(Attribute<Th, T> attribute, IObservable<?> observable){
			mAttribute = attribute;
			mBindedObservable = observable;
		}
		
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			if (prop==mAttribute){
				mBindedObservable._setObject(prop.get(), initiators);
			}
			else if (prop==mBindedObservable){
				if (initiators.contains(Attribute.this)) return;
				mAttribute._setObject(prop.get(), initiators);
				initiators.add(this);
				initiators.add(Attribute.this);
				notifyChanged(initiators);
			}
		}
	}
}