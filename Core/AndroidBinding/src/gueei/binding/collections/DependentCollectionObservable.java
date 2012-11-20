package gueei.binding.collections;

import java.util.ArrayList;
import java.util.Collection;

import gueei.binding.BindingLog;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;

public abstract class DependentCollectionObservable<T> extends Observable<T> implements CollectionObserver {

	protected IObservableCollection<?>[] mDependents;
	
	public DependentCollectionObservable(Class<T> type, IObservableCollection<?>... dependents) {
		super(type);
		for(IObservableCollection<?> o : dependents){
			o.subscribe((CollectionObserver)this);
		}
		this.mDependents = dependents;
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(this);
		this.onCollectionChanged(new ArrayListObservable<Object>(null), null, initiators);
	}
	
	// This is provided in case the constructor can't be used. 
	// Not intended for normal usage
	public void addDependents(IObservableCollection<?>... dependents){
		IObservableCollection<?>[] temp = mDependents;
		mDependents = new IObservableCollection<?>[temp.length + dependents.length];
		int len = temp.length;
		for(int i=0; i<len; i++){
			mDependents[i] = temp[i];
		}
		int len2 = dependents.length;
		for(int i=0; i<len2; i++){
			mDependents[i+len] = dependents[i];
			dependents[i].subscribe((CollectionObserver)this);
		}
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(this);
		this.onCollectionChanged(new ArrayListObservable<Object>(null), null, initiators);
	}
	
	public abstract T calculateValue(CollectionChangedEventArg e, Object... args) throws Exception;

	@Override
	public final void onCollectionChanged
		(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {
		dirty = true;
		changedArgs = args;		
		initiators.add(collection);
		this.notifyChanged(initiators);		
	}
	
	private boolean dirty = false;	
	private CollectionChangedEventArg changedArgs = null;

	@Override
	public T get() {
		if (dirty){
			int len = mDependents.length;
			Object[] values = new Object[len];
			for(int i=0; i<len; i++){
				values[i] = mDependents[i].get();
			}
			try{
				T value = this.calculateValue(changedArgs, values);
				this.setWithoutNotify(value);
			}catch(Exception e){
				BindingLog.exception
					("DependentCollectionObservable.CalculateValue()", e);
			}
			dirty = false;
			changedArgs = null;
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
