package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ExpandableCollectionAdapter;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

import android.widget.Adapter;
import android.widget.ExpandableListView;


public class ExpandableListView_ItemSourceViewAttribute 
	extends ViewAttribute<ExpandableListView, Object> {
	
	Layout template, childItemTemplate;
	String childItemSource;
	
	private Observer attrObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			createAdapter();
		}
	};
	
	public  ExpandableListView_ItemSourceViewAttribute 
		(ExpandableListView view) {
		super(Object.class,view, "itemSource");
		try{
			ViewAttribute<?,?> attrItemTemplate = Binder.getAttributeForView(getView(), "itemTemplate");
			ViewAttribute<?,?> attrChildItemTemplate = Binder.getAttributeForView(getView(), "childItemTemplate");
			ViewAttribute<?,?> attrChildItemSource = Binder.getAttributeForView(getView(), "childItemSource");
			attrItemTemplate.subscribe(attrObserver);
			attrChildItemTemplate.subscribe(attrObserver);
			attrChildItemSource.subscribe(attrObserver);
		}catch(Exception e){
			BindingLog.exception("ExpandableListView_ItemSourceViewAttribute", e);
		}
		createAdapter();
	}

	private void getAttributeValue() throws Exception {
		template = ((Layout)Binder.getAttributeForView(getView(), "itemTemplate").get());
		childItemTemplate = ((Layout)Binder.getAttributeForView(getView(), "childItemTemplate").get());
		childItemSource = (String)(Binder.getAttributeForView(getView(), "childItemSource").get());
	}

	private Object mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		mValue = newValue;
		if(getView()==null) return;
		createAdapter();
	}

	private void createAdapter(){
		if (mValue==null) return;

		try {
			getAttributeValue();
			if ((template==null) || (childItemTemplate==null) || (childItemSource==null))
				return;
			
			Adapter groupAdapter = 
				gueei.binding.collections.Utility.getSimpleAdapter
					(getView().getContext(), mValue, template, template, null);
			ExpandableCollectionAdapter adapter = new ExpandableCollectionAdapter
				(getView().getContext(), groupAdapter, childItemSource, childItemTemplate);
			getView().setAdapter(adapter);
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public Object get() {
		// Set only attribute
		return null;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}
}
