package gueei.binding.collections;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionChangedEventArg.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class ArrayListObservable<T> 
	extends ObservableCollection<T> 
	implements List<T>, Parcelable{
	
	private final Class<T> mType;
	protected ArrayList<T> mArray;
	
	public ArrayListObservable(Class<T> type){
		this(type, null);
	}
	
	public ArrayListObservable(Class<T> type, T[] initArray){
		mType = type;
		mArray = new ArrayList<T>();
		if (initArray!=null)
		{
			for(int i=0; i<initArray.length; i++){
				mArray.add(initArray[i]);
			}
		}
	}
	
	public T getItem(int position) {
		return mArray.get(position);
	}

	public void onLoad(int position) {
	}

	public void clear(){
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Reset, (List<?>)null);
		mArray.clear();
		this.notifyCollectionChanged(e);
	}
	
	/**
	 * set from array and clear
	 * 
	 * @param newArray
	 */
	public void setArray(T[] newArray){
		Object [] oldItems = mArray.toArray();		
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Replace, Arrays.asList(newArray),  Arrays.asList(oldItems));
		mArray.clear();
		int size = newArray.length;
		for (int i=0; i<size; i++){
			mArray.add(newArray[i]);
		}
		this.notifyCollectionChanged(e);		
	}
	

	public void replaceItem(int position, T item){		
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Replace, item, mArray.get(position), position);
		mArray.set(position, item);
		this.notifyCollectionChanged(e);
	}
	
	public int indexOf(Object item){
		return mArray.indexOf(item);
	}

	public Class<T> getComponentType() {
		return mType;
	}

	public boolean addAll(Collection<? extends T> arg0) {
		boolean result = mArray.addAll(arg0);
		if (result){
			@SuppressWarnings("unchecked")
			CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Add, Arrays.asList(arg0), mArray.size()-arg0.size()-1);
			this.notifyCollectionChanged(e);
		}
		return result;
	}
	
	/**
	 * set from list and clear
	 * 
	 * @param arg0
	 */
	public void setAll(Collection<? extends T> arg0) {		
		Object [] oldItems = mArray.toArray();
		mArray.clear();
		mArray.addAll(arg0);				
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Replace, mArray, Arrays.asList(oldItems));
		this.notifyCollectionChanged(e);		
	}	

	public boolean containsAll(Collection<?> arg0) {
		return mArray.containsAll(arg0);
	}

	public boolean isEmpty() {
		return mArray.isEmpty();
	}

	public Iterator<T> iterator() {
		return mArray.iterator();
	}

	public boolean removeAll(Collection<?> arg0) {
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Remove, mArray);
		boolean result = mArray.removeAll(arg0);
		if (result){
			this.notifyCollectionChanged(e);
		}
		return result;
	}

	public boolean retainAll(Collection<?> arg0) {
		List<Object>inverseIntersect = new ArrayList<Object>();
		if(arg0.size() > mArray.size()) {
			for(Object o : arg0){
				if(!mArray.contains(o))
					inverseIntersect.add(o);
			}
			
		} else {
			for(Object o : mArray){
				if(!arg0.contains(o))
					inverseIntersect.add(o);
			}
		}
						
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Remove, inverseIntersect);
		boolean result = mArray.retainAll(arg0);
		if (result){
			this.notifyCollectionChanged(e);
		}
		return result;
	}
	
	public T remove(int index){
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Remove, 
											Arrays.asList(new Object[]{mArray.get(index)}), index);
		T obj = mArray.remove(index);
		this.notifyCollectionChanged(e);
		return obj;
	}

	public int size() {
		return mArray.size();
	}

	public Object[] toArray() {
		return mArray.toArray();
	}

	public <Ta> Ta[] toArray(Ta[] array) {
		return mArray.toArray(array);
	}

	public boolean add(T object) {		
		boolean result = mArray.add(object);
		if (result){	
			CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Add, Arrays.asList(new Object[]{object}), (int)mArray.size()-1);
			this.notifyCollectionChanged(e);
		}
		return result;
	}

	public boolean contains(Object object) {
		return mArray.contains(object);
	}

	public boolean remove(Object object) {
		int index = mArray.indexOf(object);
		boolean result = mArray.remove(object);
		if (result){
			CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Remove, Arrays.asList(new Object[]{object}), index);
			this.notifyCollectionChanged(e);
		}
		return result;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		try{
			dest.writeArray(this.toArray());
		}catch(Exception e){
			// The array is not parcelable.. ok?
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void _setObject(Object newValue, Collection<Object> initiators) {
		if (newValue instanceof ArrayListObservable && this != newValue) {
			mArray=((ArrayListObservable<T>)newValue).mArray;
			initiators.add(this);
			this.notifyChanged(initiators);
		}
	}

	public static final Parcelable.Creator<ArrayListObservable<?>> CREATOR =
			new Parcelable.Creator<ArrayListObservable<?>>() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public ArrayListObservable<?> createFromParcel(Parcel source) {
					Object[] arr = source.readArray(this.getClass().getClassLoader());
					return new ArrayListObservable(arr.getClass().getComponentType(), arr);
				}

				public ArrayListObservable<?>[] newArray(int size) {
					return new ArrayListObservable[size];
				}
			};

	public void add(int location, T object) {
		if( location < 0 )
			location = 0;
		if( location>mArray.size())
			location = mArray.size();
		
		mArray.add(location, object);
				
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Add, Arrays.asList(new Object[]{object}), location);
		this.notifyCollectionChanged(e);	
	}

	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return false;
	}

	public T get(int location) {
		return mArray.get(location);
	}

	public int lastIndexOf(Object object) {
		return mArray.lastIndexOf(object);
	}

	public ListIterator<T> listIterator() {
		return mArray.listIterator();
	}

	public ListIterator<T> listIterator(int location) {
		return mArray.listIterator(location);
	}

	public T set(int location, T object) {
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Replace, mArray.indexOf(object));
		T temp = mArray.set(location, object);
		notifyCollectionChanged(e);
		return temp; 
	}

	public List<T> subList(int start, int end) {
		return mArray.subList(start, end);
	}

	public void setVisibleChildrenCount(Object setter, int total) {
	}

	@Override
	public boolean isNull() {
		return false;
	}
}
