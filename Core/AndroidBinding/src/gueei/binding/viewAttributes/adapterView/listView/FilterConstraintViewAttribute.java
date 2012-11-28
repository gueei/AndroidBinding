package gueei.binding.viewAttributes.adapterView.listView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import android.widget.Adapter;
import android.widget.Filterable;
import android.widget.ListView;

/**
 * Filter constraint
 * This Char Sequence will be supplied to Filter.filter(constraint) to perform filtering
 * 
 * @name filterConstraint
 * @widget ListView
 * @type CharSequence
 * @accepts	CharSequence
 * 
 * @category list
 * @category filter
 * 
 * @related http://developer.android.com/reference/android/widget/Filter.html
 * 
 * @author andy
 */
public class FilterConstraintViewAttribute extends ViewAttribute<ListView, CharSequence>{

	public FilterConstraintViewAttribute(ListView view) {
		super(CharSequence.class, view, "filterConstraint");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof CharSequence){
			try {
				@SuppressWarnings("unchecked")
				Adapter adapter = 
						((ViewAttribute<?, Adapter>)Binder.getAttributeForView(getView(), "adapter")).get();
				if (adapter instanceof Filterable){
					((Filterable)adapter).getFilter().filter((CharSequence)newValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public CharSequence get() {
		return null;
	}

}
