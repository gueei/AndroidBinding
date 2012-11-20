package gueei.binding.listeners;

import android.view.View;
import android.widget.AdapterView;

public class OnItemSelectedListenerMulticast 
	extends ViewMulticastListener<AdapterView.OnItemSelectedListener> 
	implements AdapterView.OnItemSelectedListener {

	@Override
	public void registerToView(View v) {
		if (!(v instanceof AdapterView<?>)) return;
		((AdapterView<?>)v).setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		for (AdapterView.OnItemSelectedListener l: listeners){
			l.onItemSelected(arg0, arg1, arg2, arg3);
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		for (AdapterView.OnItemSelectedListener l: listeners){
			l.onNothingSelected(arg0);
		}
	}
}