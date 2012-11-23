package gueei.binding.markupDemoICS;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.markupDemoICS.viewModels.DemoEntry;
import gueei.binding.markupDemoICS.viewModels.ShowDemoViewModel;
import gueei.binding.serialization.ViewModelParceler;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;
import android.view.View;

public class ShowDemoActivity extends BindingActivityV30 {
	
	public final Observable<Object> Demo = new Observable<Object>(Object.class);
	
	public final Command Back = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ShowDemoActivity.this.finish();
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Bundle entryBundle = getIntent().getBundleExtra("Entry");
	    DemoEntry entry = new DemoEntry();
	    ViewModelParceler.restoreViewModel(entryBundle, entry);
	    
	    Demo.set(new ShowDemoViewModel(this, entry));
	    this.inflateAndBind(R.xml.showdemo_metadata, this);
	}

	
}