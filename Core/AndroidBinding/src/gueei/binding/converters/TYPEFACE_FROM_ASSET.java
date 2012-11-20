package gueei.binding.converters;

import android.graphics.Typeface;
import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Get type face from asset folder
 *  
 * @usage type_name
 * @arg type_name String Name of the typeface
 * 
 * @return android.graphics.Typeface
 */
public class TYPEFACE_FROM_ASSET extends Converter<Typeface> {

	public TYPEFACE_FROM_ASSET(IObservable<?>[] dependents) {
		super(Typeface.class, dependents);
	}

	@Override
	public Typeface calculateValue(Object... args) throws Exception {
		if (args.length<1) return null;
		
		String file = args[0].toString();
		if( file == null ) return null;
					
		// TODO check if this is slow - if yes, cache me :) 
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), file);  
		return font;
	}
}
