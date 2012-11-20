package gueei.binding.viewAttributes.templates;

public class LayoutItem {	
	private int mLayoutId = -1;
	private String mLayoutName = null;
	
	public LayoutItem(int layoutId){
		setLayoutId(layoutId);
	}
	
	public LayoutItem(String layoutName){
		setLayoutName(layoutName);
	}	
	
	public void setLayoutId(int id){
		mLayoutId = id;
		mLayoutName = null;
	}
	
	public int getLayoutId(){
		return mLayoutId;
	}
	
	public void setLayoutName(String name){
		mLayoutName = name;
		mLayoutId = -1;
	}	
	
	public String getLayoutName(){
		return mLayoutName;
	}		
}
