package gueei.binding.labs.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.labs.viewAttributes.treeView.TreeEnsureVisibleNodeViewAttribute;
import gueei.binding.labs.viewAttributes.treeView.TreeStructureViewAttribute;
import gueei.binding.widgets.TreeViewList;
import android.view.View;

public class TreeViewListProvider extends BindingProvider{
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof TreeViewList)) return null;
		if (attributeId.equals("treeStructure"))
			return (ViewAttribute<Tv, ?>) new TreeStructureViewAttribute((TreeViewList)view);
		else if (attributeId.equals("ensureVisibleNode"))
			return (ViewAttribute<Tv, ?>) new TreeEnsureVisibleNodeViewAttribute((TreeViewList)view);
		return null;
	}
}
