package gueei.binding.v30.viewAttributes.adapterView.viewPager;

import gueei.binding.listeners.ViewMulticastListener;
import android.support.v4.view.ViewPager;
import android.view.View;

public class OnPageChangeListenerMulticast 
	extends ViewMulticastListener<ViewPager.OnPageChangeListener> 
	implements ViewPager.OnPageChangeListener {

	public void onPageScrollStateChanged(int arg0) {
		for(ViewPager.OnPageChangeListener listener: listeners){
			listener.onPageScrollStateChanged(arg0);
		}
    }

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		for(ViewPager.OnPageChangeListener listener: listeners){
			listener.onPageScrolled(arg0, arg1, arg2);
		}
    }

	public void onPageSelected(int arg0) {
		for(ViewPager.OnPageChangeListener listener: listeners){
			listener.onPageSelected(arg0);
		}
    }

	@Override
    public void registerToView(View v) {
		if (v instanceof ViewPager)
			((ViewPager)v).setOnPageChangeListener(this);
    }
}
