package gueei.binding.demo.pagerplayground.ViewModels;

import java.util.Calendar;

import android.os.Handler;
import gueei.binding.observables.CharSequenceObservable;
import gueei.binding.observables.ObjectObservable;

public class Page {
	public final CharSequenceObservable Title = new CharSequenceObservable("T");
	public final CharSequenceObservable PageNumber = new CharSequenceObservable("P");
	public final ObjectObservable Parent = new ObjectObservable();
	
	private final Handler handler;
	
	private final String mName;
	
	public Page(String name, Object parent){
		Parent.set(parent);
		Title.set("t: " + name);
		mName = name;
		handler = new Handler();
		
		handler.postDelayed(changeTitle, 1000);
	}
	
	private Runnable changeTitle = new Runnable(){
		@Override
		public void run() {
			PageNumber.set(mName + ": " + Calendar.getInstance().getTime().getSeconds() + "");
			handler.postDelayed(changeTitle, 1000);
		}
	};
}
