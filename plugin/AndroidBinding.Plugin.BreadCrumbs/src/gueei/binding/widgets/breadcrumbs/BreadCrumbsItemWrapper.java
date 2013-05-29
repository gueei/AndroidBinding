package gueei.binding.widgets.breadcrumbs;

import java.util.Collection;

import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.widgets.BreadCrumbs;

public class BreadCrumbsItemWrapper {	
	public final IntegerObservable WrapperBreadCrumbLayoutId = new IntegerObservable(0);
	public final ObjectObservable WrapperBreadCrumbDataSource = new ObjectObservable(null);
	public Object parent;
	public IObservableCollection<?> children;
	private BreadCrumbs breadCrumbs;
	
	public BreadCrumbsItemWrapper(BreadCrumbs breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}
	
	private IObservable<?> layoutIdObservable=null;
	private IObservable<Object> selectedPositionObservable=null;
	
	private Observer observerLayoutId = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			int id = 0;
			if(prop!=null && prop.get() instanceof Integer)
				id = (Integer)prop.get();
			WrapperBreadCrumbLayoutId.set(id);
		}
	};	
	
	private Observer observerSelectedPosition = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if(prop == null)
				return;
			
			int childPos = -1;
			if(prop.get() instanceof Integer)
				childPos = (Integer)prop.get();
			
			breadCrumbs.setSelectedPosition(BreadCrumbsItemWrapper.this, childPos);
		}
	};	
	
	public void setLayoutIdObservable(IObservable<?> prop) {
		if(layoutIdObservable!=null)
			layoutIdObservable.unsubscribe(observerLayoutId);
		layoutIdObservable=prop;
		if(layoutIdObservable!=null) {
			layoutIdObservable.subscribe(observerLayoutId);
			if(prop!=null && prop.get() instanceof Integer)
				WrapperBreadCrumbLayoutId.set((Integer)prop.get());
			else
				WrapperBreadCrumbLayoutId.set(0);
		} else {
			WrapperBreadCrumbLayoutId.set(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setSelectedPositionObservable(IObservable<?> prop) {
		if(selectedPositionObservable!=null)
			selectedPositionObservable.unsubscribe(observerSelectedPosition);
		selectedPositionObservable=(IObservable<Object>)prop;
		if(selectedPositionObservable!=null)
			selectedPositionObservable.subscribe(observerSelectedPosition);
	}
	

	public void detach() {
		setLayoutIdObservable(null);
		setSelectedPositionObservable(null);
	}

	public void setSelectedPositionValue(Integer value) {
		if(selectedPositionObservable==null)
			return;
		selectedPositionObservable.unsubscribe(observerSelectedPosition);
		selectedPositionObservable.set(value);
		selectedPositionObservable.subscribe(observerSelectedPosition);
	}
	
	
}
