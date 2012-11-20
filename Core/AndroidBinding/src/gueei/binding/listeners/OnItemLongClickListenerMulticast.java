package gueei.binding.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * User: =ra=
 * Date: 28.07.11
 * Time: 17:17
 */
public class OnItemLongClickListenerMulticast extends ViewMulticastListener<OnItemLongClickListener>
		implements OnItemLongClickListener {

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		boolean returnValue = false;
		for (OnItemLongClickListener l : listeners) {
			if (l.onItemLongClick(parent, view, position, id)) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>)) {
			return;
		}
		((AdapterView<?>) v).setOnItemLongClickListener(this);
	}
}
