package gueei.binding.test;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import android.os.Bundle;
import android.view.View;

public class TestButtonActivity extends BindingActivity {

	public final IntegerObservable HelloWorld = new IntegerObservable(3);

	public final Command SayGoodBye = new Command() {
		@Override
		public void Invoke(View arg0, Object... arg1) {
			HelloWorld.set(1);
		}
	};

	public final Command SayHello = new Command() {
		@Override
		public void Invoke(View arg0, Object... arg1) {
			HelloWorld.set(0);
		}
	};
	
	public final BooleanObservable IsHello = new BooleanObservable(true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.testbuttonactivity_layout, this);
		//this.setContentView(R.layout.testbuttonactivity_layout);
	}
}
