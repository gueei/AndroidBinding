package gueei.binding;

import android.content.Context;

/**
 * Base class for All Converters 'function' in XML Markup
 * @author andy
 *
 * @param <T>
 */
public abstract class Converter<T> extends TwoWayDependentObservable<T> {
	public Converter(Class<T> type, IObservable<?>[] dependents) {
		super(type, dependents);
	}
	
	@Override
	public boolean ConvertBack(Object value, Object[] outResult) {
		return false;
	}

	private Context mContext;
	public void setContext(Context context){
		mContext = context;
	}
	public Context getContext(){
		return mContext;
	}
}
