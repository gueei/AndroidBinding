package gueei.binding.viewAttributes.adapterView;

import java.lang.ref.WeakReference;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;

public class SelectedObjectViewAttribute extends ViewAttribute<AdapterView<?>, Object> implements OnItemSelectedListener {
	public SelectedObjectViewAttribute(AdapterView<?> view) {
		super(Object.class, view, "selectedObject");
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class).register(this);
	}
	
	WeakReference<Object> selectedItem;

	@Override
	protected void doSetAttributeValue(Object newValue) {	
		if(getView()==null) return;
		Object selected = getView().getSelectedItem();
		Object o=null;
		
		int pos = -1;
		
		if( newValue != null ) {
			int c = getView().getAdapter().getCount();
		
			for( int i=0; i<c; i++ ) {
				o = getView().getAdapter().getItem(i);
				if( o == null )
					continue;
				if( o.equals(newValue)) {
					pos = i;					
					break;
				}
			}
		}
		
		if( selected != null && o != null )
			if(selected.equals(o))
				return;
		
		selectedItem = new WeakReference<Object>(newValue);
		getView().setSelection(pos);
	}

	@Override
	public Object get() {
		if( selectedItem == null || selectedItem.get() == null)
			return null;
		
		return selectedItem.get();
	}

	int mPosition = -1;
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		if( pos < 0 || pos > getView().getAdapter().getCount() )
			return;
		
		selectedItem = new WeakReference<Object>(getView().getAdapter().getItem(pos));
		this.notifyChanged();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		selectedItem=null;
		this.notifyChanged();
	}
}
