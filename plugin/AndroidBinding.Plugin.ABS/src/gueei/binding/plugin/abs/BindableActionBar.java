package gueei.binding.plugin.abs;

import java.lang.ref.WeakReference;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;

import gueei.binding.BindingLog;
import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import android.app.Activity;
import android.view.View;

public class BindableActionBar extends View implements IBindableView<BindableActionBar>{

	ActionBarSherlock mSherlock;
	
	WeakReference<Activity> mActivityRef;
	
	public ActionBar getActionBar(){
		if(mActivityRef==null || mActivityRef.get() == null)
			return null;
		return mSherlock.getActionBar();
	}
	
	public BindableActionBar(Activity context, ActionBarSherlock sherlock) {
		super(context);
		mActivityRef = new WeakReference<Activity>(context);
		mSherlock = sherlock;
	}
	
	public Activity getActivity(){
		if(mActivityRef==null || mActivityRef.get() == null)
			return null;
		return mActivityRef.get();
	}

	public ViewAttribute<? extends View, ?> createViewAttribute(
			String attributeId) {
		try{
			String capId = attributeId.substring(0, 1).toUpperCase() + attributeId.substring(1);
			String className = "gueei.binding.plugin.abs.attributes." + capId;
			return (ViewAttribute<?,?>)Class.forName(className)
						.getConstructor(BindableActionBar.class)
						.newInstance((BindableActionBar)this);
		}catch(Exception e){
			BindingLog.warning("ActionBarAttributeBinder", "Attribute not found");
			return null;
		}
	}
}