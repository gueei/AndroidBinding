package com.example.androidbinding.plugin.abs.sample;

import com.actionbarsherlock.view.Menu;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import gueei.binding.plugin.abs.BindingABSActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BindingABSActivity {
	
	public final StringObservable HelloWorld = 
			new StringObservable("Hello world from Android-Binding");
	
	public final Command SayGoodBye = 
			new Command(){
				@Override
                public void Invoke(View arg0, Object... arg1) {
					HelloWorld.set("Good bye :)");
                }
	};

	public final Command HelloAgain = 
			new Command(){
				@Override
                public void Invoke(View arg0, Object... arg1) {
					HelloWorld.set("Hello Again");
                }
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inflateAndBind(R.xml.mainactivity_metadata, this);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    return super.onCreateOptionsMenu(menu);
    }
}
