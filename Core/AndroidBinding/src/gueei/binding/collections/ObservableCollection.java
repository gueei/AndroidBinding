package gueei.binding.collections;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.utility.WeakList;

import java.util.ArrayList;
import java.util.Collection;


public abstract class ObservableCollection<T> implements IObservableCollection<T>{

	private WeakList<CollectionObserver> mCollectionObservers
		= new WeakList<CollectionObserver>();
	
	@SuppressWarnings("rawtypes")
	public Class<IObservableCollection> getType() {
		return IObservableCollection.class;
	}

	public final void subscribe(Observer o) {
	}

	public final void unsubscribe(Observer o) {
	}

	public final Observer[] getAllObservers() {
		// TODO Auto-generated method stub
		return null;
	}

	public final void notifyChanged(Object initiator) {
	}

	public final void notifyChanged(Collection<Object> initiators) {
	}

	public final void notifyChanged() {
	}

	@SuppressWarnings("rawtypes")
	public final void set(IObservableCollection newValue,
			Collection<Object> initiators) {
	}

	@SuppressWarnings("rawtypes")
	public final void set(IObservableCollection newValue) {
	}

	public void _setObject(Object newValue,
			Collection<Object> initiators) {
	}

	public final IObservableCollection<?> get() {
		return this;
	}

	public void subscribe(CollectionObserver c) {
		mCollectionObservers.add(c);
	}

	public void unsubscribe(CollectionObserver c) {
		mCollectionObservers.remove(c);
	}
	
	public void clear() {
		mCollectionObservers.clear();
	}

	public void notifyCollectionChanged(CollectionChangedEventArg args){
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(this);
		notifyCollectionChanged(args,initiators);
	}
	
	public void notifyCollectionChanged(CollectionChangedEventArg args, Collection<Object> initiators) {
		if(initiators==null) return;
		initiators.add(this);
		for(Object c: mCollectionObservers.toArray()){
			if (initiators.contains(c)) continue;
			((CollectionObserver)c).onCollectionChanged(this, args, initiators);
		}
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * Added: 18/10/2011
	 * Set the number of visible children (most probably by Adapter)
	 * A collection might not bound to only one adapter, so this is for reference only
	 * @param setter the object the initiate this
	 * @param total the count
	 */
	public void setVisibleChildrenCount(Object setter, int total){}
}
