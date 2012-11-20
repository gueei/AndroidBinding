package gueei.binding.viewAttributes.templates;

import gueei.binding.Binder.InflateResult;

public abstract class Layout {
	private int mDefaultId = -1;
	
	public Layout(int defaultId){
		setDefaultLayoutId(defaultId);
	}
	
	public void setDefaultLayoutId(int id){
		mDefaultId = id;
	}
	
	public int getDefaultLayoutId(){
		return mDefaultId;
	}
	
	public abstract int getLayoutTypeId(int pos);
	
	public abstract int getLayoutId(int pos);
	
	public abstract int getTemplateCount();
	
	/**
	 * Provide a hook for injecting additional binding attributes to the view
	 * @param root
	 */
	public void onAfterInflate(InflateResult result, int pos){}
}