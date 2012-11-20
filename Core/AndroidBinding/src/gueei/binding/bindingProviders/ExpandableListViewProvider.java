package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.adapterView.ExpandableListView_ItemSourceViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.expandableListView.AdapterViewAttribute;
import android.view.View;
import android.widget.ExpandableListView;

public class ExpandableListViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked" })
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ExpandableListView))
			return null;
		if (attributeId.equals("itemSource"))
			return (ViewAttribute<Tv, ?>) 
					new ExpandableListView_ItemSourceViewAttribute((ExpandableListView)view);
		if (attributeId.equals("adapter"))
			return (ViewAttribute<Tv, ?>) 
					new AdapterViewAttribute((ExpandableListView)view);
		return null;
	}
}
