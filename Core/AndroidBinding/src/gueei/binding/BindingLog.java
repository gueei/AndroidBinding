package gueei.binding;

import gueei.binding.ISyntaxResolver.SyntaxResolveException;
import android.util.Log;

public class BindingLog {
	public static final String tag = "Binder";
	
	public static void warning(String occuredAt, String message){
		Log.w(tag, occuredAt + " : " + message);
	}
	
	public static void exception(String occuredAt, Throwable e){
		if (e instanceof SyntaxResolveException){
			Log.e(tag, occuredAt + " : " + e.getMessage());
			if (e.getCause()!=null)
				exception(occuredAt, e.getCause());
		}
		else 
			Log.e(tag, occuredAt + " : " + e.getMessage(), e);
	}
	
	public static void debug(String occuredAt, String message){
		Log.w(tag, occuredAt + " : " + message);
	}

}
