package gueei.binding.menu;

import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.labs.EventAggregator;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// Each OptionsMenuBinder correspond to one AbsMenuBridge xml. 
// Instance should be kept by the activity
public class BindableOptionsMenu extends View
	implements IMenuItemChangedCallback, IBindableView<BindableOptionsMenu>{
	
	public BindableOptionsMenu(Activity context) {
		super(context);
		mActivity = new WeakReference<Activity>(context);;
	}

	private boolean menuCreated = false;
	private boolean firstCreate = true;
	private int mMenuResId;
	private Hashtable<Integer, AbsMenuBridge> items = 
			new Hashtable<Integer, AbsMenuBridge>();
	
	private final WeakReference<Activity> mActivity;
	
	// Called by owner activity
	public boolean onCreateOptionsMenu(Menu menu){
		Activity activity = mActivity.get();
		Object model;
		
		try{
			mMenuResId = (Integer)Binder.getAttributeForView(this, "menu").get();
			model = Binder.getAttributeForView(this, "dataSource").get();
		}catch(Exception e) {
			BindingLog.exception("BindableOptionsMenu.onCreateOptionsMenu", e);
			return false;
		}
		
		// First inflate the menu - default action
		activity.getMenuInflater().inflate(mMenuResId, menu);
		
		if (firstCreate){
			// Now, parse the menu
			XmlResourceParser parser = activity.getResources().getXml(mMenuResId);
			try{
				int eventType= parser.getEventType();
				while(eventType != XmlResourceParser.END_DOCUMENT){
					if (eventType==XmlResourceParser.START_TAG){
						int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = 
									AbsMenuBridge.create(nodeName, id, attrs, activity, model);
							if (item!=null){
								items.put(id, item);
							}
						}
					}
					eventType = parser.next();
				}
			}catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException ex){
				ex.printStackTrace();
			}
			firstCreate = false;
		}
		
		for(AbsMenuBridge item: items.values()){
			item.onCreateOptionItem(menu);
		}
		
		return true;
	}
	
	public boolean onPrepareOptionsMenu(Menu menu){
		for(AbsMenuBridge item: items.values()){
			item.onPrepareOptionItem(menu);
		}
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi){
		AbsMenuBridge item = items.get(mi.getItemId());
		if (item!=null){
			return item.onOptionsItemSelected(mi);
		}
		return false;
	}

	@Override
	public void onItemChanged(IObservable<?> prop, AbsMenuBridge item) {
		invalidate();
	}
	
	public void invalidate(){
		if (menuCreated && mActivity!=null && mActivity.get()!=null)
			EventAggregator.getInstance(mActivity.get()).publish("invalidateOptionsMenu()", this, new Bundle());		
	}

	@Override
	public ViewAttribute<? extends View, ?> createViewAttribute(
			String attributeId) {
		try{
			String capId = attributeId.substring(0, 1).toUpperCase() + attributeId.substring(1);
			String className = "gueei.binding.menu.viewAttributes." + capId;
			return (ViewAttribute<?,?>)Class.forName(className)
						.getConstructor(BindableOptionsMenu.class)
						.newInstance(BindableOptionsMenu.this);
		}catch(Exception e){
			BindingLog.warning("BindableOptionsMenu", "Attribute not found");
			return null;
		}
	}
}