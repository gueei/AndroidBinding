package gueei.binding.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickListenerMulticast extends ViewMulticastListener<OnItemClickListener>
		implements OnItemClickListener {

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		for(OnItemClickListener l:listeners){
			l.onItemClick(arg0, arg1, arg2, arg3);
		}
	}

	@Override
	public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>)) return;
		((AdapterView<?>)v).setOnItemClickListener(this);
	}

}
