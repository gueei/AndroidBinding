package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.imageView.SourceViewAttribute;
import android.view.View;
import android.widget.ImageView;


public class ImageViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof ImageView)) return null;
		if (attributeId.equals("source")){
			return 
				(ViewAttribute<Tv, ?>) new SourceViewAttribute((ImageView)view);
		}
		return null;
	}
}
