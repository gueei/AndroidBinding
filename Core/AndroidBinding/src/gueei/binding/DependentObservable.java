package gueei.binding;

import java.util.Collection;
import java.util.ArrayList;

public abstract class DependentObservable<T> extends Observable<T> implements Observer{

	protected IObservable<?>[] mDependents;
	
	public DependentObservable(Class<T> type, IObservable<?>... dependents) {
		super(type);
		for(IObservable<?> o : dependents){
			o.subscribe(this);
		}
		this.mDependents = dependents;
		this.onPropertyChanged(null, new ArrayList<Object>());
	}
	
	// This is provided in case the constructor can't be used. 
	// Not intended for normal usage
	public void addDependents(IObservable<?>... dependents){
		IObservable<?>[] temp = mDependents;
		mDependents = new IObservable<?>[temp.length + dependents.length];
		int len = temp.length;
		for(int i=0; i<len; i++){
			mDependents[i] = temp[i];
		}
		int len2 = dependents.length;
		for(int i=0; i<len2; i++){
			mDependents[i+len] = dependents[i];
			dependents[i].subscribe(this);
		}
		this.onPropertyChanged(null, new ArrayList<Object>());
	}

	public abstract T calculateValue(Object... args) throws Exception;
	
	public final void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		dirty = true;
		initiators.add(this);
		this.notifyChanged(initiators);
	}

	private boolean dirty = false;
	
	@Override
	public T get() {
		if (dirty){
			int len = mDependents.length;
			Object[] values = new Object[len];
			for(int i=0; i<len; i++){
				values[i] = mDependents[i].get();
			}
			try{
				T value = this.calculateValue(values);
				this.setWithoutNotify(value);
			}catch(Exception e){
				BindingLog.exception
					("DependentObservable.CalculateValue()", e);
			}
			dirty = false;
		}
		return super.get();
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}