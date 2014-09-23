package gueei.binding.binablelayout.test;

import java.util.ArrayList;
import java.util.List;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainViewModel vm = new MainViewModel();
		this.setAndBindRootView(R.layout.main, vm);
	}
	
	public static class Item {
		public final StringObservable Text = new StringObservable("Test string");
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.listview_content);				
	}
	
	public static class MainViewModel {		
		
		private List<Item> itemList = new ArrayList<Item>();
		
		public final ArrayListObservable<Item> Items = new ArrayListObservable<Item>(Item.class);			
		public final StringObservable Count = new StringObservable("<< click Test to count>>");	
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.listview_content);
		
		public MainViewModel() {
			for( int i=0; i<3; i++) {
				Item item = new Item();
				item.Text.set(item.Text.get() + " " + i);
				itemList.add(item);
			}		
		}
		
		public final Command OnCreate = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {				
				for( int i=0; i<itemList.size(); i++) {
					Items.add(itemList.get(i));
				}											
			}
		};

		public final Command OnDelete = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				Items.clear();
			}
		};
		
		public final Command OnTest = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				String msg = "";
				Object [] obj = itemList.get(0).Text.getAllObservers();
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
