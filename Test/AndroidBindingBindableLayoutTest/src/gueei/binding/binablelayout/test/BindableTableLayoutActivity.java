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

public class BindableTableLayoutActivity extends BindingActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainViewModel vm = new MainViewModel();
		this.setAndBindRootView(R.layout.bindabletablelayout_activity, vm);
	}
	
	public static class Row {		
		public final ArrayListObservable<Child> Children = new ArrayListObservable<Child>(Child.class);	
		
		public static class Child {
			public final StringObservable Text = new StringObservable();
			public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindabletablelayout_item);
			public final IntegerObservable ColSpan = new IntegerObservable(1);
			
			public final Command Clicked = new Command(){
				@Override
				public void Invoke(View view, Object... args) {
					if(LayoutId.get() == R.layout.bindabletablelayout_item_blue) {
						LayoutId.set(R.layout.bindabletablelayout_item);
					} else {
						LayoutId.set(R.layout.bindabletablelayout_item_blue);
					}
				}
			};
		}
	}	
	
	
	
	
	public static class MainViewModel {		
		
		private List<Row> itemList = new ArrayList<Row>();
		
		public final ArrayListObservable<Row> Rows = new ArrayListObservable<Row>(Row.class);			
		public final StringObservable Count = new StringObservable("<< click Test to count>>");	
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindablelinearlayout_content);
		
		public MainViewModel() {
			for( int k=0; k<4; k++) {
				Row row = new Row();			
				for( int i=0; i<6; i++) {
					Row.Child child = new Row.Child();
					child.Text.set( "C: " + i + " / " + k);
					row.Children.add(child);
				}
				itemList.add(row);
			}
			
			itemList.get(1).Children.get(1).LayoutId.set(R.layout.bindabletablelayout_item_blue);
			
			Row row = new Row();
			row.Children.add(null);
			Row.Child child = new Row.Child();
			child.Text.set("C: 1 / colspan2");		
			child.ColSpan.set(2);
			row.Children.add(child);
			itemList.add(row);		
		}
		
		public final Command OnCreate = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {				
				for( int i=0; i<itemList.size(); i++) {
					Rows.add(itemList.get(i));
				}											
			}
		};

		public final Command OnDelete = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				Rows.clear();
			}
		};
		
		public final Command OnTest = new Command() {
			@Override
			public void Invoke(View arg0, Object... arg1) {
				String msg = "";
				Object [] obj = itemList.get(0).Children.get(0).Text.getAllObservers();
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
