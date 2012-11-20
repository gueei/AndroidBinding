package gueei.binding.converters;

import android.graphics.Color;
import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Covert to Color value of three components or four components
 * If only three arguments specified, color will be RGB
 * if four arguments provided, color will be ARGB
 * 
 * @usage rValue gValue bValue
 * @usage aValue rValue gValue bValue
 * 
 * @arg @optional rValue  integer Integer value from 0-255, if obmitted, it is assumed to be 255
 * @arg aValue integer Integer value from 0-255, the R component of color
 * @arg gValue integer Integer value from 0-255, the G component of color
 * @arg bValue integer Integer value from 0-255, the B component of color
 * 
 * @return integer Android color-coded Integer
 **/

public class ARGB extends Converter<Integer> {

	public ARGB(IObservable<?>[] dependents) {
		super(Integer.class, dependents);
	}

	@Override
	public Integer calculateValue(Object... args) throws Exception {
		if (args.length<3) return 0;
		if (args.length<4)
			return Color.rgb((Integer)args[0], (Integer)args[1], (Integer)args[2]);
		return Color.argb((Integer)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3]);
	}

}
