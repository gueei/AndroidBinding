package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.compoundButton.CheckedViewAttribute;
import gueei.binding.viewAttributes.compoundButton.OnCheckedChangeViewEvent;
import android.view.View;
import android.widget.CompoundButton;


public class CompoundButtonProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof CompoundButton)) return null;
		if (attributeId.equals("checked")){
			ViewAttribute<CompoundButton, Boolean> attr = new CheckedViewAttribute((CompoundButton)view);
			return (ViewAttribute<Tv, ?>)attr;
		}
		if (attributeId.equals("onCheckedChange")){
			return (ViewAttribute<Tv, ?>) new OnCheckedChangeViewEvent((CompoundButton)view);
		}
		return null;
	}
}
