package gueei.binding.collections;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;
import java.util.WeakHashMap;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;


/**
 * Provide a nested relation of adapters
 * It is designed to use in ExpandableList
 * @author andy
 *
 */
public class ExpandableCollectionAdapter extends BaseExpandableListAdapter implements CollectionObserver {

	private final String mChildName;
	private final Layout mChildLayout;
	private final WeakHashMap<Integer, Adapter> mChildAdapters = new WeakHashMap<Integer, Adapter>();
	private final Adapter mGroupAdapter;
	private final Context mContext;
	protected final Handler mHandler;

	public ExpandableCollectionAdapter(Context context, Adapter groupAdapter, String childName, Layout childLayout){
		mHandler = new Handler();
		mChildName = childName;
		mChildLayout = childLayout;
		mContext = context;
		mGroupAdapter = groupAdapter;
		if (mGroupAdapter instanceof CollectionAdapter) {
			((CollectionAdapter) mGroupAdapter).subscribeCollectionObserver(this);
		}
	}
	/*
	public ExpandableCollectionAdapter(Context context,
			IObservableCollection<?> collection, int layoutId, int dropDownLayoutId, String childName, int childLayoutId)
			throws Exception {
		mChildName = childName;
		mChildLayoutId = childLayoutId;
		mContext = context;
		mGroupAdapter = new CollectionAdapter(context, collection, layoutId, dropDownLayoutId);
	}
*/
	
	private Adapter getChildAdapter(int groupPosition){
		try{
			if (!mChildAdapters.containsKey(groupPosition)){
				Object item = mGroupAdapter.getItem(groupPosition);
				if (item instanceof LazyLoadParent){
					((LazyLoadParent)item).onLoadChildren(mContext);
				}
				IObservable<?> child = gueei.binding.Utility.getObservableForModel(mContext, mChildName, item);
				Object childCollection = child.get();
				mChildAdapters.put(groupPosition, Utility.getSimpleAdapter(mContext, child.get(), mChildLayout, mChildLayout, null));
				if (childCollection instanceof IObservableCollection) {
					((IObservableCollection<?>) childCollection).subscribe(this);
				}
			}
			return mChildAdapters.get(groupPosition);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItem(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return getChildAdapter(groupPosition).getItemId(childPosition);
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		return getChildAdapter(groupPosition).getView(childPosition, convertView, parent);
	}

	public int getChildrenCount(int groupPosition) {
		return getChildAdapter(groupPosition).getCount();
	}

	public Object getGroup(int groupPosition) {
		return mGroupAdapter.getItem(groupPosition);
	}

	public int getGroupCount() {
		return mGroupAdapter.getCount();
	}

	public long getGroupId(int groupPosition) {
		return mGroupAdapter.getItemId(groupPosition);
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return mGroupAdapter.getView(groupPosition, convertView, parent);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void onGroupCollapsed(int groupPosition) {
		mChildAdapters.remove(groupPosition);
	}

	public void onGroupExpanded(int groupPosition) {
	}

	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCollectionChanged(IObservableCollection<?> collection,
			CollectionChangedEventArg args, Collection<Object> initiators) {
		mHandler.post(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
}