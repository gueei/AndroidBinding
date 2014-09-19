package gueei.binding.test;

import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.kernel.DefaultKernel;
import android.app.Application;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

public class CustomAttributeWithFullClassNameTest extends AndroidTestCase {
	
	public void testCustomAttributeResolution() throws AttributeNotDefinedException{
		DefaultKernel kernel = new DefaultKernel();
		kernel.init((Application) this.getContext().getApplicationContext());
		// TODO:fix this test!		
		//String attributeName = CustomAttributeOfView.class.getName();
		//kernel.getAttributeForView(new View(mContext), attributeName);
		//kernel.getAttributeForView(new TextView(mContext), attributeName);
	}
	
	public static class CustomAttributeOfView<T extends View> extends ViewAttribute<T, Integer>{
		public CustomAttributeOfView(T view) {
	        super(Integer.class, view, "Custom");
        }
		@Override
        protected void doSetAttributeValue(Object newValue) {
        }

		@Override
        public Integer get() {
	        return null;
        }
	}
	
	public static class CustomAttributeOfTextView extends ViewAttribute<TextView, Integer>{
		public CustomAttributeOfTextView(TextView view) {
	        super(Integer.class, view, "Custom");
        }
		@Override
        protected void doSetAttributeValue(Object newValue) {
        }

		@Override
        public Integer get() {
	        return null;
        }
	}
}
