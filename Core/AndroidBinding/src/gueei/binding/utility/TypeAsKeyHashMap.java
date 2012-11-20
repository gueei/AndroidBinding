package gueei.binding.utility;

import java.util.HashMap;

public class TypeAsKeyHashMap<T> {
	private HashMap<Class<? extends T>, T> mCollection = new HashMap<Class<? extends T>, T>();
	
	public void put(Class<? extends T> type, T value){
		mCollection.put(type, value);
	}
	
	@SuppressWarnings("unchecked")
	public <S extends T> S get(Class<S> type){
		if (mCollection.containsKey(type)){
			return (S)mCollection.get(type);
		}else{
			return null;
		}
	}
	
	public void remove(Class<? extends T> type){
		mCollection.remove(type);
	}
	
	public boolean containsKey(Class<? extends T> type){
		return mCollection.containsKey(type);
	}
}
