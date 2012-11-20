package gueei.binding.collections;

import gueei.binding.Binder;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

import java.util.Collection;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Singleton Adapter is a adapter contains one and only one object
 * @author andy
 */
public class SingletonAdapter implements Adapter, SpinnerAdapter, Observer, ListAdapter {
	private final Object mObj;
	private final Context mContext;
	private View mView, mDropDownView;
	private DataSetObserver mDataSetObserver;
	private final Layout mTemplate, mDropDownTemplate;
	
	public SingletonAdapter(Context context, Object obj, Layout template, Layout dropDownTemplate){
		mObj = obj;
		mContext = context;
		mTemplate = template;
		mDropDownTemplate = dropDownTemplate;
		
		if(obj instanceof Observable){
			((Observable<?>) obj).subscribe(this);
		}		
	}
	
	public SingletonAdapter(Context context, Object obj, int layoutId, int dropDownLayoutId){
		this(context, obj, new SingleTemplateLayout(layoutId), new SingleTemplateLayout(dropDownLayoutId));
	}
	
	public int getCount() {
		return 1;
	}

	public Object getItem(int position) {
		return mObj;
	}

	public long getItemId(int position) {
		return 0;
	}

	public int getItemViewType(int position) {
		return mTemplate.getLayoutTypeId(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (mView==null){
			mView = Binder.bindView(mContext, 
						Binder.inflateView(mContext, mTemplate.getDefaultLayoutId(), parent, false),
						mObj);
		}
		return mView;
	}

	public int getViewTypeCount() {
		return mTemplate.getTemplateCount();
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = observer;
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = null;
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (mDropDownView==null){
			mDropDownView = Binder.bindView(mContext, 
						Binder.inflateView(mContext, mDropDownTemplate.getDefaultLayoutId(), parent, false),
						mObj);
		}
		return mDropDownView;
	}

	public void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		if (mDataSetObserver!=null)
			mDataSetObserver.notify();
	}

	// This is supposed to be separator, so it is disabled
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}