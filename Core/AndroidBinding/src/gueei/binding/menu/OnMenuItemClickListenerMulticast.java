package gueei.binding.menu;

import android.view.MenuItem;
import gueei.binding.MulticastListener;

public class OnMenuItemClickListenerMulticast 
	extends MulticastListener<MenuItem, MenuItem.OnMenuItemClickListener> 
	implements MenuItem.OnMenuItemClickListener{

	public boolean onMenuItemClick(MenuItem item) {
		for(MenuItem.OnMenuItemClickListener l : listeners){
			l.onMenuItemClick(item);
		}
		return true;
	}

	@Override
	public void registerToHost(MenuItem host) {
		host.setOnMenuItemClickListener(this);
	}

}
