package gueei.binding.markupDemoICS.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Enforce range of input, should be used in EditText as it will set the value back
 * args[0] String (coz we need to allow empt string)
 * args[1] Min Value (int)
 * args[2] Max Value (int)
 * @author andy
 *
 */
public class InputRange extends Converter<CharSequence> {
	public InputRange(IObservable<?>[] dependents) {
		super(CharSequence.class, dependents);
	}

	@Override
	public boolean ConvertBack(Object value, Object[] outResult) {
		boolean needChange = false;
		
		Integer min = Integer.parseInt(this.mDependents[1].get().toString());
		Integer max = Integer.parseInt(this.mDependents[2].get().toString());
		outResult[1] = min;
		outResult[2] = max;
		
		// Allow zero-length string
		if (value!=null && value.toString().length() == 0){
			outResult[0] = "";
			return true;
		}

		Integer val = 0;
		try{
			val = Integer.parseInt(value.toString());
		}catch(Exception e){
			val = 0;
		}
		

		if (val>max){
			val = max;
			needChange = true;
		}
		else if (val<min){
			val = min;
			needChange = true;
		}
		
		outResult[0] = val.toString();
		if (needChange){
			this.set(val.toString());
		}
		
		return true;
	}

	@Override
	public CharSequence calculateValue(Object... args) throws Exception {
		if (args.length<1) return "3 arguments are required";
		return args[0].toString();
	}
}
