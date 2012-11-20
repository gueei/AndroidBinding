package gueei.binding.utility;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ObservableCollectionMultiplexer<T> {
		
	private Hashtable<T, List<CollectionObservableEntry>> parentHT = new Hashtable<T, List<CollectionObservableEntry>>();

	private Observer childChangedObserver = null;

	private static class CollectionObservableEntry {				
		public CollectionObserver collectionObserver = null;
		public IObservableCollection<?> observableCollection = null;	
	}
	
	public ObservableCollectionMultiplexer(Observer childChangedObserver) {
		this.childChangedObserver = childChangedObserver;
	}
	
	public void add(IObservableCollection<?> observableCollection, T parent) {
		if( observableCollection == null || parent == null )
			return;
		
		List<CollectionObservableEntry> parentList = null;		
		if( parentHT.containsKey(parent)) {
			parentList = parentHT.get(parent);
		} else {
			parentList = new ArrayList<CollectionObservableEntry>();
			parentHT.put(parent,parentList);
		}
		
		CollectionObserver observer = new CollectionObserver() {			
			@Override
			public void onCollectionChanged(
					IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {	
				if (initiators.contains(this)) return;
				T parent = getParentFromObserver(this);
				if( parent != null && childChangedObserver != null ){
					initiators.add(parent);
					childChangedObserver.onPropertyChanged(collection,initiators);
				}
			}
		};
		
		CollectionObservableEntry entry = new CollectionObservableEntry();
		entry.observableCollection = observableCollection;
		entry.collectionObserver = observer;		
		entry.observableCollection.subscribe(entry.collectionObserver);
		
		parentList.add(entry);		
	}
	
	public void removeParent(T parent) {
		if( !parentHT.containsKey(parent) )
			return;
		
		List<CollectionObservableEntry> list = parentHT.get(parent);
		for(CollectionObservableEntry entry :list ) {
			if( entry.collectionObserver == null || entry.collectionObserver == null )
				continue;
			entry.observableCollection.unsubscribe(entry.collectionObserver);
		}
		list.clear();
	}
	
	public void clear() {
		for (Iterator<Entry<T, List<CollectionObservableEntry>>> iter = parentHT.entrySet().iterator(); 
			iter.hasNext();) {
			Entry<T, List<CollectionObservableEntry>> entry = iter.next();			
			removeParent(entry.getKey());
			iter.remove();
		}
	}
	
	private T getParentFromObserver(CollectionObserver collectionObserver) {		
		for (Iterator<Entry<T, List<CollectionObservableEntry>>> iter = parentHT.entrySet().iterator(); 
				iter.hasNext();) {
				Entry<T, List<CollectionObservableEntry>> entry = iter.next();			
				List<CollectionObservableEntry> list = entry.getValue();
				for(CollectionObservableEntry e : list) {
					if( e.collectionObserver != null && e.collectionObserver.equals(collectionObserver))
						return entry.getKey();
				}
		}			
		return null;
	}
}
