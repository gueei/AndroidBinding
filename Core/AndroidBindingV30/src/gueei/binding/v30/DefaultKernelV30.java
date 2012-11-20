package gueei.binding.v30;

import gueei.binding.AttributeBinder;
import gueei.binding.BindingLog;
import gueei.binding.kernel.DefaultKernel;
import gueei.binding.v30.bindingProviders.ListViewProviderV30;
import gueei.binding.v30.bindingProviders.ViewPagerViewProviderV30;
import gueei.binding.v30.bindingProviders.ViewProviderV30;

public class DefaultKernelV30 extends DefaultKernel{
	
	protected static final boolean hasCompatibilityLibrarySupport;

    static {
        boolean found = false;
        try {
            Class.forName("android.support.v4.app.Fragment");
            found = true;
        } catch( Exception ignored ) {}
        hasCompatibilityLibrarySupport = found;
    }
    
	@Override
    protected void registerProviders(AttributeBinder attrBinder) {
		attrBinder.registerProvider(new ListViewProviderV30());
		attrBinder.registerProvider(new ViewProviderV30());
		
		if( hasCompatibilityLibrarySupport ) {
			attrBinder.registerProvider(new ViewPagerViewProviderV30());
		} else {
			BindingLog.warning("BinderV30.init", "android.support.v4.app compatibility library not found");
		}
	    super.registerProviders(attrBinder);
    }
}
