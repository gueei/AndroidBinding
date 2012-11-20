package gueei.binding.viewAttributes.tabHost;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;

@SuppressWarnings("rawtypes")
public class TabsViewAttribute extends ViewAttribute<TabHost, ArrayListObservable> {
	public TabsViewAttribute(TabHost view) {
		super(ArrayListObservable.class, view, "tabs");
	}

	private ArrayListObservable<Tab> mTabs;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (!(newValue instanceof ArrayListObservable)) return;
		
		// Type is not Tab
		if (!Tab.class.isAssignableFrom(((ArrayListObservable)newValue).getComponentType()))
			return;
		
		mTabs = (ArrayListObservable<Tab>)newValue;
		
		for(Tab t: mTabs){
			mTabHost.addTab(constructTabSpec(t));
		}
	}
	
	private TabSpec constructTabSpec(Tab tab){
		TabSpec spec = mTabHost.newTabSpec(tab.Tag.get());
		if (tab.Icon.get() != null){
			spec.setIndicator(tab.Label.get(), tab.Icon.get());
		}else{
			spec.setIndicator(tab.Label.get());
		}
		if (tab.Activity.get() != null){
			Intent intent;
			try {
				intent = new Intent(getView().getContext(), Class.forName(tab.Activity.get()));
				spec.setContent(intent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			spec.setContent(tab.ViewId.get());
		}
		
		return spec;
	}

	TabHost mTabHost;
	
	@Override
	protected void onBind(Context context, IObservable<?> prop,
			BindingType binding) {
		if (!(context instanceof TabActivity)) return;
		mTabHost = ((TabActivity)context).getTabHost();
		super.onBind(context, prop, binding);
	}

	@Override
	public ArrayListObservable<Tab> get() {
		return mTabs;
	}
}