package gueei.binding;

import java.util.ArrayList;
import java.util.Collection;

import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;

/**
 * tracks for modification for the given dependents
 * 
 * you have to enable tracking by enableTracking(true)
 * and you have to reset the modify state by calling resetModified()
 *
 */
public class ModificationObservable extends Observable<Boolean> implements Observer {
	
	protected IObservable<?>[] mDependents;	
	
	public ModificationObservable(IObservable<?>... dependents) {
		super(Boolean.class);
		for(IObservable<?> o : dependents){
			o.subscribe(this);
		}
		this.mDependents = dependents;
		this.onPropertyChanged(null, new ArrayList<Object>());		
	}
	
	// This is provided in case the constructor can't be used. 
	// Not intended for normal usage
	public void addDependents(IObservable<?>... dependents){
		IObservable<?>[] temp = mDependents;
		mDependents = new IObservable<?>[temp.length + dependents.length];
		int len = temp.length;
		for(int i=0; i<len; i++){
			mDependents[i] = temp[i];
		}
		int len2 = dependents.length;
		for(int i=0; i<len2; i++){
			mDependents[i+len] = dependents[i];
			dependents[i].subscribe(this);
		}
		this.onPropertyChanged(null, new ArrayList<Object>());
	}	

	public final void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		set(true);
		initiators.add(this);
		this.notifyChanged(initiators);
	}

	private boolean isTracking = false;
	
	@Override
	public Boolean get() {
		if(!isTracking)
			return false;
		return super.get();
	}	
	
	public void resetModified() {
		setWithoutNotify(false);
	}	
	
	public void enableTracking(boolean enabled) {		
		isTracking = enabled;
		if( isTracking )
			resetModified();				
	}
	
	public Boolean isTrackingEnabled() {
		return isTracking;
	}

}
