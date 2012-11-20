package gueei.binding.listeners;

import android.view.View;
import android.widget.ExpandableListView;

public class OnChildClickListenerMulticast extends ViewMulticastListener<ExpandableListView.OnChildClickListener> 
	implements ExpandableListView.OnChildClickListener {

	@Override
	public void registerToView(View v) {
		if (!(v instanceof ExpandableListView)) return;
		((ExpandableListView)v).setOnChildClickListener(this);
	}
	
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		for(ExpandableListView.OnChildClickListener l : listeners){
			l.onChildClick(parent, v, groupPosition, childPosition, id);
		}
		
		return true;
	}
}