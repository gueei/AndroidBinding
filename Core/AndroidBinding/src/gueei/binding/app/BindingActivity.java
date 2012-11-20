package gueei.binding.app;

import java.lang.ref.WeakReference;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.Utility;
import gueei.binding.converters.ADAPTER;
import gueei.binding.menu.OptionsMenuBinder;
import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class BindingActivity extends Activity {

	OptionsMenuBinder menuBinder;
	Object mMenuViewModel;
	private WeakReference<View> mRootViewRef;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    protected void onDestroy() {
		super.onDestroy();
    }
	
	// idea from : http://stackoverflow.com/questions/1147172/what-android-tools-and-methods-work-best-to-find-memory-resource-leaks
	/**
	 * Original Name: unbindDrawables. Change to this to avoid "bind" since binding is different meaning for A-B
	 */
	protected void releaseDrawables() {
		if(mRootViewRef != null && mRootViewRef.get() != null)
			releaseDrawables(mRootViewRef.get());   		
	}

	/**
	 * Utility method to help release drawables from Activity once activity is onDestroy
	 * see more at: http://www.alonsoruibal.com/bitmap-size-exceeds-vm-budget/
	 * @param view
	 */
    protected void releaseDrawables(View view) {
    	if( view == null ) return;
        if (view.getBackground() != null) {
        	view.getBackground().setCallback(null);
        }
        
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            	releaseDrawables(((ViewGroup) view).getChildAt(i));
            }            
        	try {
        		if((view instanceof AdapterView<?>)) {
        			AdapterView<?> adapterView = (AdapterView<?>)view;
        			adapterView.setAdapter(null);        			
        		} else {
        			((ViewGroup) view).removeAllViews();
        		}
        	} catch(Exception e) {
        	}
        }
    }	
	
	protected View setAndBindRootView(int layoutId, Object... contentViewModel){
		if (mRootViewRef!=null && mRootViewRef.get() !=null){
			throw new IllegalStateException("Root view is already created");
		}
		InflateResult result = Binder.inflateView(this, layoutId, null, false);
		mRootViewRef = new WeakReference<View>(result.rootView);
		for(int i=0; i<contentViewModel.length; i++){
			Binder.bindView(this, result, contentViewModel[i]);
		}
		setContentView(mRootViewRef.get());
		return mRootViewRef.get();
	}
	
	protected void setAndBindOptionsMenu(int menuId, Object menuViewModel){
		if (menuBinder!=null){
			throw new IllegalStateException("Options menu can only set once");
		}
		menuBinder = new OptionsMenuBinder(menuId);
		mMenuViewModel = menuViewModel;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// No menu is defined
		if (menuBinder==null)
			return false;
		return menuBinder.onCreateOptionsMenu(this, menu, mMenuViewModel);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (menuBinder==null)
			return false;
		return menuBinder.onPrepareOptionsMenu(this, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuBinder!=null)
			return menuBinder.onOptionsItemSelected(this, item);
		return super.onOptionsItemSelected(item);
	}

	public View getRootView() {
		if(mRootViewRef == null)
			return null;
		return mRootViewRef.get();
	}

	public void setRootView(View rootView) {
		mRootViewRef = new WeakReference<View>(rootView);
	}

}
