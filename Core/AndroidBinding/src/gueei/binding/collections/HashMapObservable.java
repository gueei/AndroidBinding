package gueei.binding.collections;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionChangedEventArg.Action;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashMapObservable<K, V> extends ObservableCollection<V>
	implements Map<K,V>{

	private HashMap<K,V> mMap;
	private final Class<K> mKeyType;
	private final Class<V> mValueType;
	
	public HashMapObservable(Class<K> keyType, Class<V> valueType){
		mKeyType = keyType;
		mValueType = valueType;
		mMap = new HashMap<K,V>();
	}
	
	@SuppressWarnings("unchecked")
	public V getItem(int position) {
		return (V)mMap.values().toArray()[position];
	}

	public void onLoad(int position) {
	}

	public Class<K> getKeyType(){
		return mKeyType;
	}
	
	public Class<V> getComponentType() {
		return mValueType;
	}

	public int size() {
		return mMap.size();
	}

	public void clear() {
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Reset, (List<V>) null);
		mMap.clear();
		this.notifyCollectionChanged(e);
	}

	public boolean containsKey(Object key) {
		return mMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return mMap.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return mMap.entrySet();
	}

	public V get(Object key) {
		return mMap.get(key);
	}

	public boolean isEmpty() {
		return mMap.isEmpty();
	}

	public Set<K> keySet() {
		return mMap.keySet();
	}

	public V put(K key, V value) {
		V result = mMap.put(key, value);
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Add,key);
		this.notifyCollectionChanged(e);
		return result;
	}

	public void putAll(Map<? extends K, ? extends V> arg0) {
		mMap.putAll(arg0);
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Add,Arrays.asList(arg0.keySet().toArray()));
		this.notifyCollectionChanged(e);
	}

	public V remove(Object key) {
		V result = mMap.remove(key);
		CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Remove,key);
		this.notifyCollectionChanged(e);
		return result;
	}

	public Collection<V> values() {
		return mMap.values();
	}

	@Override
	public boolean isNull() {
		return false;
	}
}
