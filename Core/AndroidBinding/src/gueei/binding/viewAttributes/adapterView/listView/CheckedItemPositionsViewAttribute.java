package gueei.binding.viewAttributes.adapterView.listView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Checked Item Positions
 * Note the name is very similar to checkedItemPosition (without s)
 * This one only applies to ListViews with CHOIC_MODE_MULTIPLE and return all checked Items' positions in SparseBooleanArray
 * You need to bind this to IObservable<SparseBooleanArray> and if you need to change the checked items, 
 * you need to call IObservable<SparseBooleanArray>.set(newArray) instead of just changing the content of the array, 
 * because SparseBooleanArray is not observable 
 * 
 * @name checkedItemPositions
 * @widget ListView
 * @type SparseBooleanArray
 * @accepts	SparseBooleanArray
 * @category list
 * @related 
 * 
 * @author andy
 */
public class CheckedItemPositionsViewAttribute extends ViewAttribute<ListView, SparseBooleanArray>
	implements OnItemClickListener{

	public CheckedItemPositionsViewAttribute(ListView view) {
		super(SparseBooleanArray.class, view, "checkedItemPositions");
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.register(this);
	}

	@Override
	public SparseBooleanArray get() {
		if(getView()==null) return null;
		return getView().getCheckedItemPositions();
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (!(newValue instanceof SparseBooleanArray)){
			return;
		}

		if (getView().getChoiceMode() != ListView.CHOICE_MODE_MULTIPLE) return; 
		
		SparseBooleanArray arr = (SparseBooleanArray)newValue;
		int len = arr.size();
		for (int i=0; i<len; i++){
			getView().setItemChecked(arr.keyAt(i), arr.valueAt(i));
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		this.notifyChanged();
	}
}
