package gueei.binding.widgets;

import gueei.binding.Binder.InflateResult;
import gueei.binding.viewAttributes.templates.LayoutItem;

public class BindableLayoutContent {
	private LayoutItem mLayoutItem = null;
	private Object mDataSource = null;
	private InflateResult mInflateResult = null;
	
	public void setLayoutId(int id){
		mLayoutItem = new LayoutItem(id);
	}
	
	public int getLayoutId(){
		if(mLayoutItem==null)
			return 0;
		return mLayoutItem.getLayoutId();
	}
	
	public void setLayoutItem(LayoutItem layoutItem){
		mLayoutItem = layoutItem;
	}
	
	public LayoutItem getLayoutItem(){
		return mLayoutItem;
	}
	
	public void setDataSource(Object dataSource){
		mDataSource = dataSource;
	}
	
	public Object getDataSource(){
		return mDataSource;
	}
	
	public void setInflateResult(InflateResult inflateResult){
		mInflateResult = inflateResult;
	}
	
	public InflateResult getInflateResult(){
		return mInflateResult;
	}

	public void clear() {
		mLayoutItem = null;
		mDataSource = null;
		mInflateResult = null;
	}	
	
}
