package gueei.binding.labs.viewAttributes.breadCrumbs;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.widgets.BreadCrumbs;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsStructure;
import android.widget.Adapter;

public class BreadCrumbsStructureViewAttribute extends ViewAttribute<BreadCrumbs, BreadCrumbsStructure> {
			
		public  BreadCrumbsStructureViewAttribute 
			(BreadCrumbs view) {
			super(BreadCrumbsStructure.class,view, "breadcrumbsStructure");
		}
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if(getView()==null) return;
			if (newValue instanceof BreadCrumbsStructure){
				getView().setBreadCrumbsStructure((BreadCrumbsStructure)newValue);
			}
		}

		@Override
		public BreadCrumbsStructure get() {
			if(getView()==null) return null;
			return getView().getBreadCrumbsStructure();
		}
		
		@Override
        protected BindingType AcceptThisTypeAs(Class<?> type) {
			if (Adapter.class.isAssignableFrom(type)) return BindingType.OneWay;
			return super.AcceptThisTypeAs(type);
        }
}
