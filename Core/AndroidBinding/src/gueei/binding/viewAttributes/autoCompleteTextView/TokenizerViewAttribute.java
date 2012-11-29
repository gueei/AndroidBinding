package gueei.binding.viewAttributes.autoCompleteTextView;

import gueei.binding.ViewAttribute;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

/**
 * Tokenizer for MultiAutoCompleteTextView
 * 
 * @name tokenizer
 * @widget MultiAutoCompleteTextView
 * @type Tokenizer
 * @accepts	Tokenizer
 * @category list simple 
 * @related
 * 
 * @author andy
 *
 */
public class TokenizerViewAttribute extends ViewAttribute<MultiAutoCompleteTextView, MultiAutoCompleteTextView.Tokenizer> {
	public TokenizerViewAttribute(MultiAutoCompleteTextView view) {
		super(MultiAutoCompleteTextView.Tokenizer.class, view, "tokenizer");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue instanceof MultiAutoCompleteTextView.Tokenizer){
			getView().setTokenizer((MultiAutoCompleteTextView.Tokenizer)newValue);
		}
	}

	@Override
    public Tokenizer get() {
	    return null;
    }
}