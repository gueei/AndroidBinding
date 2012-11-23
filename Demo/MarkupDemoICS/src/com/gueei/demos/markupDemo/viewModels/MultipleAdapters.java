package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class MultipleAdapters {
	public MultipleAdapters(Activity activity){
		for(int i=1; i<11; i++){
			SectionAList.add("A.Item: " + i);
			SectionBList.add("B.Item: " + i);
			SectionCList.add("C.Item: " + i);
		}
	}
	
	public ArrayListObservable<String> SectionAList = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String> SectionBList = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String> SectionCList = new ArrayListObservable<String>(String.class);
	
	public Observable<Object> ClickedItem = new Observable<Object>(Object.class); 
	public Command ToastItem = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText
				(view.getContext(), 
						"You Clicked: " + ClickedItem.get().toString(), 
						Toast.LENGTH_SHORT).show();
		}
	};
}
