package gueei.binding.labs.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.labs.viewAttributes.breadCrumbs.BreadCrumbsStructureViewAttribute;
import gueei.binding.widgets.BreadCrumbs;
import android.view.View;

public class BreadCrumbsProvider extends BindingProvider{
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof BreadCrumbs)) return null;
		if (attributeId.equals("breadcrumbsStructure"))
			return (ViewAttribute<Tv, ?>) new BreadCrumbsStructureViewAttribute((BreadCrumbs)view);
		return null;
	}
}
