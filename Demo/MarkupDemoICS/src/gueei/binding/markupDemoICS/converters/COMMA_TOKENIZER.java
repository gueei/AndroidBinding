package gueei.binding.markupDemoICS.converters;

import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import gueei.binding.Converter;
import gueei.binding.IObservable;

public class COMMA_TOKENIZER extends Converter<MultiAutoCompleteTextView.Tokenizer> {

	public COMMA_TOKENIZER(IObservable<?>[] dependents) {
	    super(MultiAutoCompleteTextView.Tokenizer.class, dependents);
    }

	private MultiAutoCompleteTextView.Tokenizer tokenizer;
	@Override
    public Tokenizer calculateValue(Object... args) throws Exception {
	    if (tokenizer==null)
	    	tokenizer = new MultiAutoCompleteTextView.CommaTokenizer();
	    return tokenizer;
    }
}
