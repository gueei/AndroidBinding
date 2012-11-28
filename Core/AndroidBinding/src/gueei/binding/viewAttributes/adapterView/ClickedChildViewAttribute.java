package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnChildClickListenerMulticast;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * Clicked Item of Child in Expandable List View
 * Changes at ExpandableListView.OnChildClickListener.onChildClick()
 * 
 * @name clickedChild
 * @widget ExpandableListView
 * @type Object
 * @accepts	Object
 * @category expandable-list
 * @related 
 * 
 * @author andy
 */
public class ClickedChildViewAttribute extends ViewAttribute<ExpandableListView, Object>
	implements ExpandableListView.OnChildClickListener{

	private Object value;
	
	public ClickedChildViewAttribute(ExpandableListView view) {
		super(Object.class, view, "clickedChild");
		this.setReadonly(true);
		Binder.getMulticastListenerForView(view, OnChildClickListenerMulticast.class)
			.registerWithHighPriority(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		// do nothing. this is a readonly attribute
	}

	@Override
	public Object get() {
		return value;
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		if (!getView().equals(parent)) return false;
		try{
			this.value = getView().getExpandableListAdapter().getChild(groupPosition, childPosition);
			this.notifyChanged();
		}catch(Exception e){
		}
		return true;
	}
	
}
