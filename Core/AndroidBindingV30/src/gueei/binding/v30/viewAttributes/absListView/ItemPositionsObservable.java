package gueei.binding.v30.viewAttributes.absListView;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionChangedEventArg.Action;
import gueei.binding.collections.ObservableCollection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.util.SparseBooleanArray;

public class ItemPositionsObservable extends
	ObservableCollection<Boolean>{
	
	private SparseBooleanArray arr = new SparseBooleanArray();

	public Boolean getItem(int position) {
		return arr.valueAt(position);
	}

	public void onLoad(int position) {
	}

	public Class<Boolean> getComponentType() {
		return Boolean.class;
	}

	public int size() {
		return arr.size();
	}
	
	public void clear(){
		CollectionChangedEventArg arg = new CollectionChangedEventArg(Action.Reset);
		arr.clear();
		notifyCollectionChanged(arg);
	}
	
	public void put(int key, boolean value){
		arr.put(key, value);
		notifyCollectionChanged(new CollectionChangedEventArg(Action.Add, key));
	}
	
	public void putAll(Map<Integer, Boolean> values){
		for(Entry<Integer, Boolean> set : values.entrySet()){
			arr.put(set.getKey(), set.getValue());
		}
		notifyCollectionChanged(new CollectionChangedEventArg(Action.Add, values.keySet()));
	}

	public void replaceAll(SparseBooleanArray newArr, Object initiator){
		arr = Helper.cloneSBArray(newArr);
		ArrayList<Object> initiators = new ArrayList<Object>();
		initiators.add(initiator);
		notifyCollectionChanged(new CollectionChangedEventArg(Action.Reset), initiators);
	}
	
	public void delete(int key){
		arr.delete(key);
		notifyCollectionChanged(new CollectionChangedEventArg(Action.Remove, key));
	}
	
	public int indexOfKey(int key){
		return arr.indexOfKey(key);
	}
	
	public int indexOfValue(boolean value){
		return arr.indexOfValue(value);
	}
	
	public int keyAt(int index){
		return arr.keyAt(index);
	}
	
	public boolean valueAt(int index){
		return arr.valueAt(index);
	}
	
	public boolean get(int key){
		return arr.get(key, false);
	}
	
	public boolean get(int key, boolean valueIfKeyNotFound){
		return arr.get(key, valueIfKeyNotFound);
	}

	public boolean isNull() {
		return false;
	}
}
