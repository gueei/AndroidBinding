package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.progressBar.ProgressViewAttribute;
import gueei.binding.viewAttributes.progressBar.SecondaryProgressViewAttribute;
import android.view.View;
import android.widget.ProgressBar;


public class ProgressBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ProgressBar)) return null;
		if (attributeId.equals("progress")){
			return (ViewAttribute<Tv, ?>)
				new ProgressViewAttribute((ProgressBar)view);
		}
		if (attributeId.equals("secondaryProgress")){
			return (ViewAttribute<Tv, ?>)
				new SecondaryProgressViewAttribute((ProgressBar)view);
		}
		return null;
	}
}
