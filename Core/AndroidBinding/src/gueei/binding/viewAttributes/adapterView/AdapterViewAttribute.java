package gueei.binding.viewAttributes.adapterView;

import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.LazyLoadAdapter;

/**
 * To Provide adapter to AdapterViews, including subclasses like Spinner, ListView, Gallery etc.
 * 
 * @name adapter
 * @widget AdapterView
 * @type Adapter
 * @accepts	Adapter
 * @category list 
 * @related
 * 
 * @converter ADAPTER
 * @converter STITCH
 * @converter SECTION
 * 
 * @author andy
 *
 * @param <T>
 */
public class AdapterViewAttribute<T extends Adapter> extends ViewAttribute<AdapterView<T>, Adapter> {
	public AdapterViewAttribute(AdapterView<T> view) {
		super(Adapter.class, view, "adapter");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof Adapter){
			getView().setAdapter((T)newValue);
			if (newValue instanceof LazyLoadAdapter){
				if (getView() instanceof AbsListView)
				((LazyLoadAdapter)newValue).setRoot((AbsListView)getView());
			}
		}
	}

	@Override
	public Adapter get() {
		if(getView()==null) return null;
		return getView().getAdapter();
	}
}