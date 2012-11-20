package gueei.binding.app;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class BindingWidget {
	
	public static Dialog createAndBindDialog(Context context, int layoutId, Object contentViewModel) {
		Dialog dialog = new Dialog(context);
		InflateResult result = Binder.inflateView(context, layoutId, null, false);
		dialog.setContentView(result.rootView);
		for(View v: result.processedViews){
			AttributeBinder.getInstance().bindView(context, v, contentViewModel);
		}			
        return dialog;                 
	}
	
}
