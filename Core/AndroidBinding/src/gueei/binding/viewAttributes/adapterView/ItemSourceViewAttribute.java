package gueei.binding.viewAttributes.adapterView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

import android.widget.Adapter;
import android.widget.AdapterView;


public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	Layout template, spinnerTemplate;
	ViewAttribute<?,Layout> itemTemplateAttr, spinnerTemplateAttr;
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
	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class,view, attributeName);
		
		try{
			itemTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "itemTemplate");
			itemTemplateAttr.subscribe(templateObserver);
			spinnerTemplateAttr = (ViewAttribute<?,Layout>)Binder.getAttributeForView(getView(), "spinnerTemplate");
			spinnerTemplateAttr.subscribe(templateObserver);
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
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
				(getView().getContext(), newValue, spinnerTemplate, template, null);
			((ViewAttribute<?, Adapter>)Binder.getAttributeForView(getView(), "adapter")).set(adapter);
			ViewAttribute<?,Object> SelectedObject = (ViewAttribute<?,Object>)Binder.getAttributeForView(getView(), "selectedObject");
			if( SelectedObject != null) {				
				Object selObject = SelectedObject.get();				
				if( selObject != null ) {
					int c = getView().getAdapter().getCount();
					int pos = -1;
					for( int i=0; i<c; i++ ) {
						Object o = getView().getAdapter().getItem(i);
						if( o == null ) {
							if( selObject == null ) {
								pos = i;
								break;
							}
							continue;
						}
						if( o.equals(selObject)) {
							pos = i;
							break;
						}
					}
					getView().setSelection(pos);
				}
			} else {				
				ViewAttribute<?,Integer> SelectedPosition = (ViewAttribute<?,Integer>)Binder.getAttributeForView(getView(), "selectedPosition");
				getView().setSelection(SelectedPosition.get());
			}
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
