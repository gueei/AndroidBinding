package gueei.binding.collections;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;


/**
 * Provide a nested relationship between adapters
 * @author andy
 *
 */
public class CombinedAdapter extends BaseAdapter implements LazyLoadAdapter{
	private ArrayList<TranslatedAdapter> mTranslated = new ArrayList<TranslatedAdapter>();

	private DataSetObserver observer = new DataSetObserver(){
		@Override
		public void onChanged() {
			super.onChanged();
			calculateTranslation();
			notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			super.onInvalidated();
			notifyDataSetInvalidated();
			calculateTranslation();
		}
	};

	public void addAdapter(Adapter[] adapter){
		for (int i=0; i<adapter.length; i++){
			mTranslated.add(new TranslatedAdapter(adapter[i]));
			adapter[i].registerDataSetObserver(observer);
		}
		
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	/**
	 * Add a new adapter to combined adapter, whenever a new adapter is added, the 
	 * List will be invalidated.
	 * @param adapter
	 */
	public void addAdapter(Adapter adapter){
		mTranslated.add(new TranslatedAdapter(adapter));
		adapter.registerDataSetObserver(observer);
		
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	public void remvoeAdapter(Adapter adapter){
		for(int i=0; i<mTranslated.size(); i++){
			if (mTranslated.get(i).adapter.equals(adapter)){
				mTranslated.remove(i);
				adapter.unregisterDataSetObserver(observer);
				break;
			}
		}
		calculateTranslation();
		notifyDataSetChanged();
	}
	
	private int mItemCount, mItemTypeCount;
	
	private void calculateTranslation(){
		int pos = 0;
		int typeOffset = 0;
		mItemTypeCount = 0;
		mItemCount = 0;
		for(TranslatedAdapter p: mTranslated){
			p.offset = pos;
			p.itemTypeOffset = typeOffset;
			pos += p.adapter.getCount();
			typeOffset += p.adapter.getViewTypeCount();
		}

		mItemCount = pos;
		mItemTypeCount = typeOffset;
	}
	
	public int getCount() {
		return mItemCount;
	}

	public Object getItem(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.adapter.getItem(position - adapter.offset);
		return null;
	}

	private TranslatedAdapter getAdapterAt(int position){
		int length = mTranslated.size();
		TranslatedAdapter adapter; 
		for (int i=0; i<length; i++){
			adapter = mTranslated.get(i);
			if(position >= adapter.offset + adapter.adapter.getCount())
				continue;
			else
				return adapter;
		}
		return null;
	}
	
	public long getItemId(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.adapter.getItemId(position - adapter.offset);
		return -1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (mHelper!=null){
			mHelper.onGetView(position);
		}
		if (adapter!=null)
			return adapter.adapter.getView(position - adapter.offset, convertView, parent);
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		TranslatedAdapter adapter = getAdapterAt(position);
		if (adapter!=null)
			return adapter.itemTypeOffset + adapter.adapter.getItemViewType(position - adapter.offset);
		return BaseAdapter.IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return mItemTypeCount;
	}

	private class TranslatedAdapter{
		public int offset, itemTypeOffset;
		public final Adapter adapter;
		public TranslatedAdapter(Adapter adapter){
			this.adapter =adapter;
		}
	}

	private Mode mMode = Mode.LoadWhenStopped;
	private LazyLoadRootAdapterHelper mHelper;
	
	public void setRoot(AbsListView view) {
		mHelper = new LazyLoadRootAdapterHelper(view, this, mMode);
	}

	public void setMode(Mode mode) {
		if (mHelper!=null)
		{
			mHelper.setMode(mode);
		}
		mMode = mode;
	}

	public void onVisibleChildrenChanged(int first, int total) {
		visibleChildrenChanged(first, total);
	}
	
	private void visibleChildrenChanged(int first, int total){
		TranslatedAdapter adapter = getAdapterAt(first);
		if (adapter==null) return;
		int afirst = first - adapter.offset;
		if (adapter.adapter.getCount() - afirst < total){
			int atotal = adapter.adapter.getCount() - afirst;
			if (adapter.adapter instanceof LazyLoadAdapter){
				((LazyLoadAdapter)adapter.adapter).onVisibleChildrenChanged(afirst, atotal);
			}
			visibleChildrenChanged(first+atotal, total-atotal);
		}else{
			if (adapter.adapter instanceof LazyLoadAdapter){
				((LazyLoadAdapter)adapter.adapter).onVisibleChildrenChanged(afirst, total);
			}
		}
	}

	@Override
	public boolean areAllItemsEnabled() {
		for(TranslatedAdapter ta: mTranslated){
			Adapter adapter = ta.adapter;
			if (adapter instanceof ListAdapter){
				if (!((ListAdapter)adapter).areAllItemsEnabled())
					return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		TranslatedAdapter ta = getAdapterAt(position);
		if (ta.adapter instanceof ListAdapter)
			return ((ListAdapter)ta.adapter).isEnabled(position - ta.offset);
		return true;
	}
}
