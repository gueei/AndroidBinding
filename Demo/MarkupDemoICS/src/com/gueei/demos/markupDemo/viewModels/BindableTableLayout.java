package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.markupDemoICS.R;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class BindableTableLayout {
	public class Row {		
		public final ArrayListObservable<Child> Children = new ArrayListObservable<Child>(Child.class);		
		public class Child {
			public final StringObservable Name = new StringObservable();
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
	
	public final ArrayListObservable<Row> Rows = new ArrayListObservable<Row>(Row.class);
		
	
	public BindableTableLayout() {
		
		for( int k=0; k<4; k++) {
			Row row = new Row();			
			for( int i=0; i<6; i++) {
				Row.Child child = row.new Child();
				child.Name.set( "C: " + i + " / " + k);
				row.Children.add(child);
			}
			Rows.add(row);
		}
		
		Rows.get(1).Children.get(1).LayoutId.set(R.layout.bindabletablelayout_item_blue);
		
		Row row = new Row();
		row.Children.add(null);
		Row.Child child = row.new Child();
		child.Name.set("C: 1 / colspan2");		
		child.ColSpan.set(2);
		row.Children.add(child);
		Rows.add(row);	
		
	}	
}
