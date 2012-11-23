package com.gueei.demos.markupDemo.viewModels;

import android.view.View;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.markupDemoICS.R;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class BindableLinearLayout {
	public final ArrayListObservable<Item> Items = new ArrayListObservable<Item>(Item.class);			
	
	public class Item {
		public final StringObservable Name = new StringObservable();
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bindablelinearlayout_item);
				
		public final Command Clicked = new Command(){
			@Override
			public void Invoke(View view, Object... args) {
				if(LayoutId.get() == R.layout.bindablelinearlayout_item_blue) {
					LayoutId.set(R.layout.bindablelinearlayout_item);
				} else {
					LayoutId.set(R.layout.bindablelinearlayout_item_blue);
				}
			}
		};		
	}		
		
	public BindableLinearLayout() {	
		for( int k=0; k<3; k++) {
			Item item = new Item();
			item.Name.set( "Item " + k);
			if( k % 2 == 0)
				item.LayoutId.set(R.layout.bindablelinearlayout_item_blue);
			Items.add(item);
		}		
	}
	
}
