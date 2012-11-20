package gueei.binding;

import gueei.binding.utility.WeakList;

import java.util.ArrayList;
import java.util.Collection;

public class Observable<T> implements IObservable<T> {
	@Override
	public String toString() {
		if (!isNull())
			return mValue.toString();
		return "null";
	}

	private WeakList<Observer> observers = new WeakList<Observer>();
	private T mValue;
	private final Class<T> mType;
	
	public Observable(Class<T> type){
		mType = type;
	}
	
	public Observable(Class<T> type, T initValue){
		this(type);
		mValue = initValue;
	}
	
	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#subscribe(gueei.binding.Observer)
	 */
	public void subscribe(Observer o){
		observers.add(o);
	}
	
	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#unsubscribe(gueei.binding.Observer)
	 */
	public  void unsubscribe(Observer o){
		observers.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#notifyChanged(java.lang.Object)
	 */
	public final void notifyChanged(Object initiator){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(initiator);
		this.notifyChanged(initiators);
	}
	
	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#notifyChanged(java.util.AbstractCollection)
	 */
	public final void notifyChanged(Collection<Object> initiators){
		initiators.add(this);
		for(Object o: observers.toArray()){
			if (initiators.contains(o)) continue;
			((Observer)o).onPropertyChanged(this, initiators);
		}
	}
	
	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#notifyChanged()
	 */
	public final void notifyChanged(){
		ArrayList<Object> initiators = new ArrayList<Object>();
		notifyChanged(initiators);
	}

	/* (non-Javadoc)
	 * @see gueei.binding.IObservable#set(T, java.util.AbstractCollection)
	 */
	public final void set(T newValue, Collection<Object> initiators){
		if (initiators.contains(this)) return;
		doSetValue(newValue, initiators);
		initiators.add(this);
		notifyChanged(initiators);
	}
	
	// Intenral use only. 
	public void _setObject(Object newValue, Collection<Object> initiators){
		try{
			T value = this.getType().cast(newValue);			
			if (value==null) return;
			this.set(value, initiators);
		}catch(ClassCastException e){
			BindingLog.warning("Observable._setObject", 
					String.format("Failed to assign value: '%s' to observable of type: '%s', type mismatch", 
							newValue, this.getType()));
			return;
		}
	}
	
	public final void set(T newValue){
		doSetValue(newValue, new ArrayList<Object>());
		notifyChanged(this);
	}
	
	protected void doSetValue(T newValue, Collection<Object> initiators){
		mValue = newValue;
	}
	
	protected final void setWithoutNotify(T newValue){
		mValue = newValue;
	}
	
	public T get(){
		return mValue;
	}

	public final Class<T> getType() {
		return mType;
	}

	public Observer[] getAllObservers() {
		return observers.toItemArray(new Observer[0]);
	}

	@Override
	public boolean isNull() {
		return mValue==null;
	}
}
