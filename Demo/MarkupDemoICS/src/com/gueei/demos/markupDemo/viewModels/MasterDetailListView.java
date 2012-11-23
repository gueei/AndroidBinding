package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.observables.StringObservable;
import android.content.Context;
import android.view.View;

public class MasterDetailListView {
	public final ArrayListObservable<MasterItem> MasterItems = 
		new ArrayListObservable<MasterItem>(MasterItem.class);
	
	public MasterDetailListView(){
		for (int i=0; i<100; i++){
			MasterItems.add(new MasterItem("MasterItem: " + i));
		}
	}
	
	public class MasterItem implements LazyLoadParent{
		public MasterItem(String name){
			Title.set(name);
		}
		
		private int clickCount = 0;
		public final Command ToastTitle = new Command(){
			public void Invoke(View view, Object... args) {
				clickCount ++;
				Title.set("Master: " + clickCount);
			}
		};

		public final StringObservable Title = new StringObservable("Master: ");
		
		public ArrayListObservable<String> DetailItems = 
			new ArrayListObservable<String>(String.class);
		
		public void onLoadChildren(Context context) {
			String[] detail = new String[10];
			for (int i=0; i<10; i++){
				detail[i] = "Detail" + i + " of " + Title.get();
			}
			DetailItems.setArray(detail);
		}
		
		@Override
		public String toString(){
			return Title.get();
		}
	}
}
