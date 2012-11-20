package gueei.binding.app.rootView;

import gueei.binding.widgets.BindableFrameLayout;
import android.content.Context;

/**
 * Default root view for the Activity
 * @author andy
 *
 */
public class BindableRootView extends BindableFrameLayout {
	
	public BindableRootView(Context context) {
		super(context);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		super.setLayoutParams(params);
	}
}
