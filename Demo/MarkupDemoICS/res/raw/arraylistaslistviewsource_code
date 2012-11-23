package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import android.view.View;

public class ArrayListAsListViewSource {
	public final ArrayListObservable<ArrayListItem> Items = 
		new ArrayListObservable<ArrayListItem>(ArrayListItem.class);
	
	public ArrayListAsListViewSource(){
	}
	
	public final Command AddItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Items.add(new ArrayListItem());
		}
	};

	public final Command RemoveItem = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (Items.size()>0)
				Items.remove(Items.size() - 1);
		}
	};
	
	public class ArrayListItem{
		private static final String Prefix = "Item: ClickCount=";
		public final BooleanObservable Enabled = new BooleanObservable(false);
		
		public final Command ClickTitle = new Command(){
			public void Invoke(View view, Object... args) {
				ClickCount.set(ClickCount.get()+1);
			}
		};

		public final IntegerObservable ClickCount = new IntegerObservable(0);
		public final DependentObservable<String> Title = new DependentObservable<String>(String.class, ClickCount){
			@Override
			public String calculateValue(Object... args) throws Exception {
				return Prefix + ClickCount.get();
			}
		};
	}
}
