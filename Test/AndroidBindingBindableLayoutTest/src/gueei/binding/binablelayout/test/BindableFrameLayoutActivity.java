package gueei.binding.binablelayout.test;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.app.BindingActivity;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.os.Bundle;
import android.view.View;

public class BindableFrameLayoutActivity extends BindingActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainViewModel vm = new MainViewModel();
		this.setAndBindRootView(R.layout.bindableframelayout_activity, vm);
	}
	
	public static class TestViewModel {
		public final StringObservable Text = new StringObservable("Test string");
	}
	
	public static class MainViewModel {		
		
		private TestViewModel vm = new TestViewModel();
		
		public final Observable<TestViewModel> DataSource = new Observable<TestViewModel>(TestViewModel.class);		
		public final IntegerObservable LayoutId = new IntegerObservable();
		public final StringObservable Count = new StringObservable("<< click Test to count>>");	
		
		public final Observable<TestViewModel> StayDataSource = new Observable<TestViewModel>(TestViewModel.class);		
		public final IntegerObservable StayLayoutId = new IntegerObservable();	
		
		public MainViewModel() {
			StayDataSource.set(vm);
			StayLayoutId.set(R.layout.bindableframelayout_content);
		}
		
		public final Command OnCreate = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				DataSource.set(vm);
				LayoutId.set(R.layout.bindableframelayout_content);
			}
		};

		public final Command OnDelete = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				DataSource.set(null);
				LayoutId.set(0);
			}
		};
		
		public final Command OnTest = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				String msg = "";
				
				Object [] obj = vm.Text.getAllObservers();
				if(obj != null) {
					msg = "" + obj.length;
				} else {
					msg = "No observers";
				}
				
				Count.set(msg);				
			}
		};			
	}
}
