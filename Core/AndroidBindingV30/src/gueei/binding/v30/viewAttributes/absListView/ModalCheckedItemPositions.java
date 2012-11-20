package gueei.binding.v30.viewAttributes.absListView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.listeners.MultiChoiceModeListenerMulticast;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Set a null to the value to reset the list
 * TODO: implement programmatically change the selected positions
 * @author andy
 *
 */
public class ModalCheckedItemPositions extends ViewAttribute<ListView, SparseBooleanArray>
	implements AbsListView.MultiChoiceModeListener{
	
	private boolean suppress = false;
	
	public ModalCheckedItemPositions(ListView view) {
		super(SparseBooleanArray.class, view, "modalCheckedItemPositions");
		Binder.getMulticastListenerForView(view, MultiChoiceModeListenerMulticast.class).register(this);
	}

	@Override
	public SparseBooleanArray get() {
		if(getView()==null) return null;
		return Helper.cloneSBArray((getView().getCheckedItemPositions()));
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (!(newValue instanceof SparseBooleanArray)){
			getView().clearChoices();
		}
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		return false;
	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	public void onDestroyActionMode(ActionMode mode) {
	}

	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		if (suppress) {
			suppress = false;
			return;
		}
		
		this.notifyChanged();
	}
}
