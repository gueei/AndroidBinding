package gueei.binding.utility;

import gueei.binding.collections.ILazyLoadRowModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A HashMap that will have fixed size,
 * once the allocated size is over, the
 * oldest entry will be removed if it is not mapped
 * 
 * 
 * TODO: find a better algorithm
 * @param <E>
 * @author andy
 */
public class CacheHashMap<K, V extends ILazyLoadRowModel> extends HashMap<K, V> {
	private static final long         serialVersionUID = 1L;
	
	private              ArrayList<K> keyList          = new ArrayList<K>();
	private              int          mCacheSize       = 50;

	public CacheHashMap(int cacheSize) {
		mCacheSize = cacheSize;
	}

	@Override
	public V put(K key, V value) {
		// Check whether it is oversize
		int oversize = keyList.size() - mCacheSize;
		if (oversize > 0){
			ArrayList<Integer> pendingRemove = new ArrayList<Integer>(oversize);
			
			for(int i=0; i<oversize; i++){
				V rVal = this.get(keyList.get(i));
				if (rVal.isMapped()) continue;
				pendingRemove.add(i);
			}
			
			for (int i=0; i<pendingRemove.size(); i++){
				K removed = keyList.remove(pendingRemove.get(i)-i);
				this.remove(removed);
			}
		}
		keyList.add(key);
		return super.put(key, value);
	}

	public void reSize(int newSize) {
		mCacheSize = newSize;
	}

	@Override
	public int size() {
		return mCacheSize;
	}
}
