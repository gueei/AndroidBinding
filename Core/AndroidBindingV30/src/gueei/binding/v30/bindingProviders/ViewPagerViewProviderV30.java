package gueei.binding.v30.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.AdapterViewAttribute;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerViewProviderV30 extends BindingProvider {
	@SuppressWarnings({ "unchecked"})
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof ViewPager))
			return null;
		try {
			if (attributeId.equals("adapter")){
				return (ViewAttribute<Tv, ?>)new AdapterViewAttribute((ViewPager)view);
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}
}
