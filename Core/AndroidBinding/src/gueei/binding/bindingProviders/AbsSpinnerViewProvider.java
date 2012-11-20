package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import android.view.View;
import android.widget.AbsSpinner;


public class AbsSpinnerViewProvider extends BindingProvider {

	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof AbsSpinner)) return null;
		return null;
	}
}
