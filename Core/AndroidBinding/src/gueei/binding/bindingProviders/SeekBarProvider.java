package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.seekBar.OnSeekBarChangeViewEvent;
import android.view.View;
import android.widget.SeekBar;


public class SeekBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof SeekBar)) return null;
		if (attributeId.equals("onSeekBarChange"))
			return (ViewAttribute<Tv, ?>) new OnSeekBarChangeViewEvent((SeekBar)view);
		return null;
	}
}