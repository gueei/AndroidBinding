package gueei.binding;

import java.util.Collection;

public abstract class TwoWayDependentObservable<T> extends DependentObservable<T> {

	public TwoWayDependentObservable(Class<T> type, IObservable<?>... dependents){
		super(type, dependents);
	}
	
	/**
	 * 
	 * @param value The value to convert back
	 * @param outResult Output of the converted result
	 * @return true if the converter can handle, false if doesn't
	 */
	public abstract boolean ConvertBack(Object value, Object[] outResult);

	@Override
	protected void doSetValue(T newValue, Collection<Object> initiators) {
		int count = mDependents.length;
		Object[] outResult = new Object[count];
		for(int i=0; i<count; i++){
			// fill with current values
			outResult[i] = mDependents[i].get();
		}		
		if (!ConvertBack(newValue, outResult)) return;
		for(int i=0; i<count; i++){
			// only set values if not equals
			if( !mDependents[i].equals(outResult[i]))
				mDependents[i]._setObject(outResult[i], initiators);
		}
	}
}