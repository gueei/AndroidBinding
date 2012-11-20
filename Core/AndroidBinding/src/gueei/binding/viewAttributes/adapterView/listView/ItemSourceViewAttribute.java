package gueei.binding.viewAttributes.adapterView.listView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

import android.widget.Adapter;
import android.widget.Filter;
import android.widget.ListView;


public class ItemSourceViewAttribute extends ViewAttribute<ListView, Object> {

	Layout template, spinnerTemplate;
	Filter filter;
	ViewAttribute<?,Layout> itemTemplateAttr, spinnerTemplateAttr;
	ViewAttribute<?,Filter> filterAttr;
	Object mValue;

	private Observer templateObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
			doSetAttributeValue(mValue);
		}
	};
	
	@SuppressWarnings("unchecked")
	public ItemSourceViewAttribute(ListView view, String attributeName) {
		super(Object.class,view, attributeName);
		
		try{
			itemTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "itemTemplate");
			itemTemplateAttr.subscribe(templateObserver);
			spinnerTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "spinnerTemplate");
			spinnerTemplateAttr.subscribe(templateObserver);
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
			filterAttr = (ViewAttribute<?,Filter>)Binder.getAttributeForView(getView(), "filter");
			filter = filterAttr.get();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		mValue = newValue;

		if (newValue == null)
			return;
		
		if (newValue instanceof Adapter){
			try {
				((ViewAttribute<?, Adapter>)Binder.getAttributeForView(getView(), "adapter")).set((Adapter)newValue);
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (template==null) return;
		
		spinnerTemplate = spinnerTemplate == null ? template : spinnerTemplate;
		
		try {
			Adapter adapter = gueei.binding.collections.Utility.getSimpleAdapter
				(getView().getContext(), newValue, spinnerTemplate, template, filter);
			((ViewAttribute<?, Adapter>)Binder.getAttributeForView(getView(), "adapter")).set(adapter);
			ViewAttribute<?,Integer> SelectedPosition = (ViewAttribute<?,Integer>)Binder.getAttributeForView(getView(), "selectedPosition");
			getView().setSelection(SelectedPosition.get());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Object get() {
		return mValue;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}
}
