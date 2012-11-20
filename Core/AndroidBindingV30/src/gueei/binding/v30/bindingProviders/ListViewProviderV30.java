package gueei.binding.v30.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.v30.viewAttributes.absListView.ModalCheckedItemPositions;
import gueei.binding.v30.viewAttributes.absListView.MultiChoiceMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class ListViewProviderV30 extends BindingProvider{
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ListView)) return null;
		if (attributeId.equals("multiChoiceMode"))
			return (ViewAttribute<Tv, ?>) new MultiChoiceMode((AbsListView)view);
		if (attributeId.equals("modalCheckedItemPositions"))
			return (ViewAttribute<Tv, ?>) new ModalCheckedItemPositions((ListView)view);
		return null;
	}
}
