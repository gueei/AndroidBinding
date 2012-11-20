package gueei.binding;

import java.util.Collection;

public interface CollectionObserver {
	void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators);
}
