package gueei.binding.v30.listeners;

import gueei.binding.listeners.ViewMulticastListener;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;

public class MultiChoiceModeListenerMulticast extends ViewMulticastListener<AbsListView.MultiChoiceModeListener> 
	implements AbsListView.MultiChoiceModeListener {
	@Override
	public void registerToView(View v) {
		if (!(v instanceof AbsListView)) return;
		((AbsListView)v).setMultiChoiceModeListener(this);
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		for (AbsListView.MultiChoiceModeListener l: listeners){
			l.onActionItemClicked(mode, item);
		}	
		return true;
	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		for (AbsListView.MultiChoiceModeListener l: listeners){
			l.onCreateActionMode(mode, menu);
		}
		return true;
	}

	public void onDestroyActionMode(ActionMode mode) {
		for (AbsListView.MultiChoiceModeListener l: listeners){
			l.onDestroyActionMode(mode);
		}
	}

	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		for (AbsListView.MultiChoiceModeListener l: listeners){
			l.onPrepareActionMode(mode, menu);
		}
		return true;
	}

	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		for (AbsListView.MultiChoiceModeListener l: listeners){
			l.onItemCheckedStateChanged(mode, position, id, checked);
		}
	}

	@Override
	public void register(MultiChoiceModeListener listener) {
		super.register(listener);
	}
	
	
}