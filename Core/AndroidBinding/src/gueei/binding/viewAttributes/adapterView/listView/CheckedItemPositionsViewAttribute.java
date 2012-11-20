package gueei.binding.viewAttributes.adapterView.listView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


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
