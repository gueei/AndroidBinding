package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import android.text.SpannableStringBuilder;

/**
 * CONCAT the given strings to one string
 * 
 * @usage str str ...
 * 
 * @arg str String
 * 
 * @return String
 * @author andy
 *
 */
public class CONCAT extends Converter<CharSequence> {

	public CONCAT(IObservable<?>[] dependents) {
		super(CharSequence.class, dependents);
	}

	@Override
	public CharSequence calculateValue(Object... args) throws Exception {
		int len = args.length;
		SpannableStringBuilder result = new SpannableStringBuilder("");
		for(int i=0; i<len; i++){
			if (args[i]==null) continue;
			if (args[i] instanceof CharSequence)
				result.append((CharSequence)args[i]);
			else
				result.append(args[i].toString());
		}
		return result.toString();
	}
}
