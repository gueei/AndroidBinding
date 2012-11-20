package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.tabHost.TabsViewAttribute;
import android.view.View;
import android.widget.TabHost;


public class TabHostProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof TabHost)) return null;
		if (attributeId.equals("tabs")){
			return 
				(ViewAttribute<Tv, ?>) new TabsViewAttribute((TabHost)view);
		}
		return null;
	}
}