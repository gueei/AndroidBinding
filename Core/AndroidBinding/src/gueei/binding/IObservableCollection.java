package gueei.binding;



@SuppressWarnings("rawtypes")
public interface IObservableCollection<T> extends IObservable<IObservableCollection>{
	void subscribe(CollectionObserver c);
	void unsubscribe(CollectionObserver c);
	void notifyCollectionChanged(CollectionChangedEventArg arg);
	T getItem(int position);
	long getItemId(int position);
	void onLoad(int position);
	Class<T> getComponentType();
	int size();
	void setVisibleChildrenCount(Object setter, int total);
}