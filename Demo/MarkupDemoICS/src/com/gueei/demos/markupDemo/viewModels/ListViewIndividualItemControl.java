package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import android.view.View;

public class ListViewIndividualItemControl {
	public final ArrayListObservable<ArrayListItem> Items = 
		new ArrayListObservable<ArrayListItem>(ArrayListItem.class);

	public final Command EnableAll = new Command(){
		public void Invoke(View view, Object... args) {
			for(ArrayListItem item : Items){
				item.Enabled.set(true);
			}
		}
	};
	
	public final Command ToggleItem = new Command(){
		public void Invoke(View view, Object... args) {
			if (args.length > 0 && args[0] instanceof ArrayListItem){
				((ArrayListItem)args[0]).Enabled.toggle();
			}
		}		
	};
	
	public ListViewIndividualItemControl(){
		for(int i=0; i<30; i++){
			Items.add(new ArrayListItem());
		}
	}
	
	public class ArrayListItem{
		public final BooleanObservable Enabled = new BooleanObservable(true);
	}
}
