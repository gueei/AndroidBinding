package gueei.binding.viewAttributes.adapterView.listView;

import android.widget.Filter;
import android.widget.ListView;
import gueei.binding.ViewAttribute;

/**
 * Filter for performing filtering on ListViews 
 * 
 * @name filter
 * @widget ListView
 * @type Filter
 * @accepts	Filter
 * 
 * @category list
 * @category filter
 * 
 * @related http://developer.android.com/reference/android/widget/Filter.html
 * 
 * @author andy
 */
public class FilterViewAttribute extends ViewAttribute<ListView, Filter> {
	public FilterViewAttribute(ListView view) {
		super(Filter.class, view, "filter");
	}

	private Filter mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Filter){
			mValue = (Filter)newValue;
		}
	}

	@Override
	public Filter get() {
		return mValue;
	}
}
