package gueei.binding.demo.pagerplayground;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.demo.pagerplayground.ViewModels.Page;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingActivityV30 {
	
	public final ArrayListObservable<Page> Pages = new ArrayListObservable<Page>(Page.class);
	
	public final IntegerObservable CurrentItem = new IntegerObservable(0);
	
	public final Command Remove = new Command(){
		@Override
		public void Invoke(View arg0, Object... arg1) {
			if (arg1[0]==null || !(arg1[0] instanceof Page)) return;
			
			Page p = (Page) arg1[0];
			Pages.remove(p);
		}
	};
	
	public final Command AddPage = new Command(){
		@Override
		public void Invoke(View arg0, Object... arg1) {
			if (arg1[0] == null) return;
			String name = arg1[0].toString();
			Page p = new Page(name, MainActivity.this);
			Pages.add(p);
			
			CurrentItem.set(Pages.size() - 1);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int i=0; i<5; i++){
        	Page p = new Page("page: " + i, this);
        	Pages.add(p);
        }
        
        this.inflateAndBind(R.xml.mainactivity_metadata, this);
    }
}
