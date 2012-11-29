package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.autoCompleteTextView.AdapterViewAttribute;
import gueei.binding.viewAttributes.autoCompleteTextView.TokenizerViewAttribute;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;


public class AutoCompleteTextViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (view instanceof AutoCompleteTextView) {
			if (attributeId.equals("adapter"))
				return (ViewAttribute<Tv, ?>)new AdapterViewAttribute((AutoCompleteTextView)view);
		}
		if (view instanceof MultiAutoCompleteTextView) {
			if (attributeId.equals("tokenizer"))
				return (ViewAttribute<Tv, ?>)new TokenizerViewAttribute((MultiAutoCompleteTextView)view);
		}
		return null;
	}
}