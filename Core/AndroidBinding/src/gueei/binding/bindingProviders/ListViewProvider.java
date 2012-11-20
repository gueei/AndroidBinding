package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.ItemSourceViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.CheckedItemPositionsViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.CheckedItemPositionViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.FilterConstraintViewAttribute;
import gueei.binding.viewAttributes.adapterView.listView.FilterViewAttribute;
import android.view.View;
import android.widget.ListView;


public class ListViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked" })
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ListView))
			return null;
		try {
			if (attributeId.equals("checkedItemPosition")) {
				return (ViewAttribute<Tv, ?>) new CheckedItemPositionViewAttribute((ListView)view);
			}else if (attributeId.equals("checkedItemPositions")) {
				return (ViewAttribute<Tv, ?>) new CheckedItemPositionsViewAttribute((ListView)view);
			}else if (attributeId.equals("filter")) {
				return (ViewAttribute<Tv, ?>) new FilterViewAttribute((ListView)view);
			}else if (attributeId.equals("filterConstraint")) {
				return (ViewAttribute<Tv, ?>) new FilterConstraintViewAttribute((ListView)view);
			}if (attributeId.equals("itemSource"))
				return (ViewAttribute<Tv, ?>) new ItemSourceViewAttribute((ListView)view, "itemSource");
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}
}
