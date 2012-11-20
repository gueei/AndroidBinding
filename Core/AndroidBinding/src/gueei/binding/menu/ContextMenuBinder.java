package gueei.binding.menu;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.AdapterView;

public class ContextMenuBinder implements View.OnCreateContextMenuListener, OnMenuItemClickListener{
	private int mMenuResId;
	private Object mViewModel;
	private Hashtable<Integer, AbsMenuBridge> items = 
			new Hashtable<Integer, AbsMenuBridge>();
	
	public ContextMenuBinder(int menuResId, Object viewModel){
		mMenuResId = menuResId;
		mViewModel = viewModel;
	}
	
	public void setViewModel(Object viewModel){
		mViewModel = viewModel;
	}
	
	public void setMenuResId(int menuResId){
		mMenuResId = menuResId;
	}
	
	@SuppressWarnings("rawtypes")
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// Activity should be the context, or else, nothing can do
		if (!(v.getContext() instanceof Activity)) return;

		// Helping adapter view to get the menu info... 
		// TODO: modulize this dirty hack
		if (v instanceof ExpandableListView){
			long ppos = ((ExpandableListView.ExpandableListContextMenuInfo)menuInfo)
					.packedPosition;
			int child = ExpandableListView.getPackedPositionChild(ppos);
			int group = ExpandableListView.getPackedPositionGroup(ppos);
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(v, "clickedItem");
				attr._setObject(((ExpandableListView)v).getExpandableListAdapter().getChild(group, child),
							new ArrayList<Object>());
				attr.notifyChanged(attr);
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
			}
		}else if (v instanceof AdapterView){
			int pos = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(v, "clickedItem");
				attr._setObject(((AdapterView)v).getItemAtPosition(pos), new ArrayList<Object>());
				attr.notifyChanged(attr);
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
			}
		}
		
		// First inflate the menu - default action
		Activity activity = (Activity)v.getContext();
		activity.getMenuInflater().inflate(mMenuResId, menu);
		
		// Now, parse the menu
		XmlResourceParser parser = activity.getResources().getXml(mMenuResId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType==XmlResourceParser.START_TAG){
					int id = parser.getAttributeResourceValue(Binder.ANDROID_NAMESPACE, "id", -1);
					MenuItem mi = menu.findItem(id);
					if (mi!=null){
						mi.setOnMenuItemClickListener(this);
						
						String nodeName = parser.getName();
						if (id>0){
							AttributeSet attrs = Xml.asAttributeSet(parser);
							AbsMenuBridge item = null;
							if ("item".equals(nodeName)){
								item = new MenuItemBridge(id, attrs, activity, mViewModel);
							}else if ("group".equals(nodeName)){
								item = new MenuGroupBridge(id, attrs, activity, mViewModel);
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
	}
	
	public boolean onMenuItemClick(MenuItem mi) {
		AbsMenuBridge item = items.get(mi.getItemId());
		if (item!=null){
			return item.onOptionsItemSelected(mi);
		}
		return false;
	}
}
