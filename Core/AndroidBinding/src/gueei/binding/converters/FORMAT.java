package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import android.text.Html;
import android.text.Spanned;


/**
 * Format behaves similar to String.format(formatstring, item...)
 * in the Java API. And it also styles the resulting string to HTML rich text
 * 
 * @usage formatString str str ...
 * 
 * @arg formatString String Java-styled format string
 * @arg str String If supplied Object, it will call Object.toString()
 * 
 * @return android.text.Spanned formatted with HTML tags in string
 */
public class FORMAT extends Converter<Spanned> {
	public FORMAT(IObservable<?>[] dependents) {
		super(Spanned.class, dependents);
	}

	@Override
	public Spanned calculateValue(Object... args) throws Exception {
		if (args.length<1) return null;
		if (args[0]==null) return null;
		if (args.length<2) return Html.fromHtml(args[0].toString());
		String formatString = args[0].toString();
		
		Object[] items = new Object[args.length-1];
		System.arraycopy(args, 1, items, 0, args.length-1);
		
		return Html.fromHtml(String.format(formatString, items));
	}
}
