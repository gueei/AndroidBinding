package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.markupDemoICS.ShowDemoActivity;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import gueei.binding.serialization.ViewModelParceler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DemoCategory {
	public final BooleanObservable ShowDemoInNewActivity = new BooleanObservable(true); 
	
	public final StringObservable Name = 
			new StringObservable();
	
	public final ArrayListObservable<DemoEntry> Entries = 
			new ArrayListObservable<DemoEntry>(DemoEntry.class);

	public final IntegerObservable SelectedDemoPosition = 
			new IntegerObservable(-1);
	
	private Context mContext;
	
	public DemoCategory(Context context, String name){
		Name.set(name);
		mContext = context;
	}
	
	public final Command ShowDemo = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (args.length>0){
				DemoEntry entry = (DemoEntry)args[0];
				doShowDemo(entry);
			}
		}
	};

	private void doShowDemo(DemoEntry entry){
		if (ShowDemoInNewActivity.get()){
			Intent intent = new Intent(mContext, ShowDemoActivity.class);
			Bundle bundle = ViewModelParceler.parcelViewModel(entry);
			intent.putExtra("Entry", bundle);
			mContext.startActivity(intent);

		}else{
			Bundle bundle = ViewModelParceler.parcelViewModel(entry);
			EventAggregator.getInstance(mContext)
				.publish("ShowDemo", DemoCategory.this, bundle);
		}
	}
	
	public void showFirstDemo(){
		if (!ShowDemoInNewActivity.get()){
			SelectedDemoPosition.set(0);
			doShowDemo(Entries.get(0));
		}
	}
}
