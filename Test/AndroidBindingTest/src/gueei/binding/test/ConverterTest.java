package gueei.binding.test;

import gueei.binding.exception.AttributeNotDefinedException;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class ConverterTest extends ActivityInstrumentationTestCase2<TestButtonActivity> {

	public ConverterTest() {
	    super(TestButtonActivity.class);
    }
	
	public void test_IF_Switchable_Command() throws AttributeNotDefinedException{
		final TestButtonActivity activity = getActivity();
		
		activity.runOnUiThread(new Runnable(){
			public void run() {
				assertEquals(3, (int)activity.HelloWorld.get());
				((Button)activity.findViewById(R.id.button_say)).performClick();				
				assertEquals(0, (int)activity.HelloWorld.get());
				activity.IsHello.set(false);
				((Button)activity.findViewById(R.id.button_say)).performClick();
				assertEquals(1, (int)activity.HelloWorld.get());
			}
		});
	}
}