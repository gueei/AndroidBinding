package gueei.binding.v30.widget;

import gueei.binding.Binder;
import gueei.binding.ConstantObservable;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.Utility;
import gueei.binding.menu.AbsMenuBridge;
import gueei.binding.menu.IMenuItemChangedCallback;
import gueei.binding.menu.MenuGroupBridge;
import gueei.binding.menu.MenuItemBridge;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public class ActionModeBinder implements ActionMode.Callback, IMenuItemChangedCallback {
	private Hashtable<Integer, AbsMenuBridge> items = 
			new Hashtable<Integer, AbsMenuBridge>();	
	
	private final Context mContext;
	private final int mMenuResId;
	private final Object mModel;
	private IObservable<?> mTitleObservable; 
	private ActionMode mActionMode;
	private boolean invalidated = false;
	
	protected ActionModeBinder(Context context, int menuResId, Object model, IObservable<?> title){
		mContext = context;
		mMenuResId = menuResId;
		mModel = model;
		mTitleObservable = title;
		if (title!=null)
			title.subscribe(titleObserver);
	}

	public static ActionModeBinder startActionMode
		(Activity activity, int menuResId, Object model, CharSequence title){
		ActionModeBinder binder =  new ActionModeBinder(activity, menuResId, model, 
				new ConstantObservable<CharSequence>(CharSequence.class, title));
		activity.startActionMode(binder);
		return binder;
	}
	
	public static ActionModeBinder startActionMode(Activity activity, int menuResId, Object model, IObservable<?> title){
		ActionModeBinder binder =  new ActionModeBinder(activity, menuResId, model, title);
		activity.startActionMode(binder);
		return binder;
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem mi) {
		AbsMenuBridge item = items.get(mi.getItemId());
		if (item!=null){
			return item.onOptionsItemSelected(mi);
		}
		return false;
	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {

		mActionMode = mode;
		
		// First inflate the menu - default action
		mode.getMenuInflater().inflate(mMenuResId, menu);
		
		// Now, parse the menu
		XmlResourceParser parser = mContext.getResources().getXml(mMenuResId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType==XmlResourceParser.START_TAG){
					int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
					MenuItem mi = menu.findItem(id);
					if (mi!=null){
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = null;
							if ("item".equals(nodeName)){
								item = new MenuItemBridge(id, attrs, mContext, mModel, this);
							}else if ("group".equals(nodeName)){
								item = new MenuGroupBridge(id, attrs, mContext, mModel, this);
							}
							if (item!=null){
								items.put(id, item);
							}
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

		for(AbsMenuBridge item: items.values()){
			item.onCreateOptionItem(menu);
			item.onPrepareOptionItem(menu);
		}
		return true;
	}

	public void onDestroyActionMode(ActionMode mode) {
		// Dispatch this... 
		mActionMode = null;
	}

	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		if (titleChanged){
			titleChanged = false;
			mode.setTitle(Utility.evalValue(mTitleObservable, CharSequence.class));
		}
		if (invalidated){
			invalidated = false;
			for(AbsMenuBridge item: items.values()){
				item.onPrepareOptionItem(menu);
			}
			return true;
		}
		return false;
	}

	public void onItemChanged(IObservable<?> prop, AbsMenuBridge item) {
		invalidated = true;
		if (mActionMode!=null)
			mActionMode.invalidate();
	}
	
	public ActionMode getActionMode(){
		return mActionMode;
	}
	
	private boolean titleChanged = true;
	private Observer titleObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			if (mActionMode!=null){
				titleChanged = true;
				mActionMode.invalidate();
			}
		}
	};
}
