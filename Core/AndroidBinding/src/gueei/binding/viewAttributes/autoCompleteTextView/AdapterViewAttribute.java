package gueei.binding.viewAttributes.autoCompleteTextView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

/**
 * To Provide adapter to Auto Complete Text View
 * 
 * @name adapter
 * @widget AutoCompleteTextView
 * @type ListAdapter
 * @accepts	ListAdapter
 * @category list simple 
 * @related
 * 
 * @converter ADAPTER
 * 
 * @author andy
 *
 */
public class AdapterViewAttribute <T extends ListAdapter & Filterable> extends ViewAttribute<AutoCompleteTextView, ListAdapter> {
	public AdapterViewAttribute(AutoCompleteTextView view) {
		super(ListAdapter.class, view, "adapter");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof ListAdapter && newValue instanceof Filterable){
			getView().setAdapter((T)newValue);
		}
	}

	@Override
	public ListAdapter get() {
		if(getView()==null) return null;
		return getView().getAdapter();
	}

	@Override
    protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
    }
}