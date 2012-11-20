package gueei.binding.viewAttributes.adapterView.listView;

import java.util.Collection;

import gueei.binding.Binder;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.OnItemClickListenerMulticast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class CheckedItemPositionViewAttribute extends ViewAttribute<ListView, Integer>
	implements OnItemClickListener, Observer{

	public CheckedItemPositionViewAttribute(ListView view) {
		super(Integer.class, view, "checkedItemPosition");
		Binder.getMulticastListenerForView(view, OnItemClickListenerMulticast.class)
			.register(this);
		try {
			Binder.getAttributeForView(view, "itemSource").subscribe(this);
		} catch (AttributeNotDefinedException e) {
			e.printStackTrace();
		}
	}

	private int mValue;
	
	@Override
	public Integer get() {
		return mValue;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (!(newValue instanceof Integer)) return;
		if (getView().getChoiceMode() != ListView.CHOICE_MODE_SINGLE) return;
		getView().setItemChecked((Integer)newValue, true);
		mValue = (Integer)newValue;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		mValue = getView().getCheckedItemPosition();
		this.notifyChanged();
	}

	public void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		getView().setItemChecked(mValue, true);
	}
}
