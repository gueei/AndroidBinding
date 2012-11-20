package gueei.binding.converters;

import gueei.binding.IObservable;
import android.text.Html;
import android.text.Spanned;

/**
 * Concat, Styles and Convert HTML Charsequences into Styled Spanned. 
 * 
 * @usage str str ...
 * 
 * @arg str String If supplied Object, it will call Object.toString()
 * 
 * @return android.text.Spanned formatted with HTML tags in string
 */
public class HTML extends CONCAT {
	public HTML(IObservable<?>[] dependents) {
		super(dependents);
	}

	@Override
	public Spanned calculateValue(Object... args) throws Exception {
		return Html.fromHtml(super.calculateValue(args).toString());
	}
}
