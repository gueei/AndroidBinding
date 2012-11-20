package gueei.binding.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import gueei.binding.IObservable;
import gueei.binding.Observer;

public class ObservableMultiplexer<T> {
		
	private Hashtable<T, List<ObservableEntry>> parentHT = new Hashtable<T, List<ObservableEntry>>();

	private Observer childChangedObserver = null;

	private static class ObservableEntry {				
		public Observer observer = null;
		public IObservable<?> observable = null;	
	}
	
	public ObservableMultiplexer(Observer childChangedObserver) {
		this.childChangedObserver = childChangedObserver;
	}
	
	public void add(IObservable<?> observable, T parent) {
		if( observable == null || parent == null )
			return;
		
		List<ObservableEntry> parentList = null;		
		if( parentHT.containsKey(parent)) {
			parentList = parentHT.get(parent);
		} else {
			parentList = new ArrayList<ObservableEntry>();
			parentHT.put(parent,parentList);
		}
		
		Observer observer = new Observer() {			
			@Override
			public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {	
				T parent = getParentFromObserver(this);
				if( parent != null && childChangedObserver != null )
					childChangedObserver.onPropertyChanged(prop,Arrays.asList(new Object[]{parent}));
			}
		};
		
		ObservableEntry entry = new ObservableEntry();
		entry.observable = observable;
		entry.observer = observer;		
		entry.observable.subscribe(entry.observer);
		
		parentList.add(entry);		
	}
	
	public void removeParent(T parent) {
		if( !parentHT.containsKey(parent) )
			return;
		
		List<ObservableEntry> list = parentHT.get(parent);
		for(ObservableEntry entry :list ) {
			if( entry.observable == null || entry.observer == null )
				continue;
			entry.observable.unsubscribe(entry.observer);
		}
		list.clear();
	}
	
	public void clear() {
		for (Iterator<Entry<T, List<ObservableEntry>>> iter = parentHT.entrySet().iterator(); 
			iter.hasNext();) {
			Entry<T, List<ObservableEntry>> entry = iter.next();			
			removeParent(entry.getKey());
			iter.remove();
		}
	}
	
	private T getParentFromObserver(Observer observer) {		
		for (Iterator<Entry<T, List<ObservableEntry>>> iter = parentHT.entrySet().iterator(); 
				iter.hasNext();) {
				Entry<T, List<ObservableEntry>> entry = iter.next();			
				List<ObservableEntry> list = entry.getValue();
				for(ObservableEntry e : list) {
					if( e.observer != null && e.observer.equals(observer))
						return entry.getKey();
				}
		}			
		return null;
	}
}
