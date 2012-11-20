package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.listeners.OnChildClickListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;
import android.view.View;
import android.widget.ExpandableListView;

public class ExpandableListView_OnChildClickViewEvent extends ViewEventAttribute<ExpandableListView>
	implements ExpandableListView.OnChildClickListener{

	public ExpandableListView_OnChildClickViewEvent(ExpandableListView view) {
		super(view, "onChildClick");
	}

	@Override
	protected void registerToListener(ExpandableListView view) {
		Binder.getMulticastListenerForView(view, OnChildClickListenerMulticast.class).register(this);
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		this.invokeCommand(parent, v, groupPosition, childPosition, id);
		return true;
	}
}
