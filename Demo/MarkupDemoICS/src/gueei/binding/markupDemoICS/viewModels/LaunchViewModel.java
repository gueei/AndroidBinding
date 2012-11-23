package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.markupDemoICS.R;
import gueei.binding.serialization.ViewModelParceler;
import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;

public class LaunchViewModel {
	private Activity mContext;
	
	public final ActionBar ActionBarViewModel;
	
	public final Observable<DemoCategory> SelectedCategory =
			new Observable<DemoCategory>(DemoCategory.class);
	
	public final ArrayListObservable<DemoCategory> Categories =
			new ArrayListObservable<DemoCategory>(DemoCategory.class);

	public final Observable<Object> Demo = new Observable<Object>(Object.class);
	
	public final Command CategorySelected = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			SelectedCategory.get().showFirstDemo();
		}
	};
	
	public LaunchViewModel(Activity context){
		mContext = context;

		parseDemos();
		ActionBarViewModel = new ActionBar();

		EventAggregator.getInstance(context)
			.subscribe("ShowDemo", new EventSubscriber(){
				@Override
				public void onEventTriggered(String eventName,
						Object publisher, Bundle data) {
					DemoEntry entry = new DemoEntry();
					ViewModelParceler.restoreViewModel(data, entry);
					showDemo(entry);
				}
			});
	}
	
	private void showDemo(DemoEntry entry){
		Demo.set(new ShowDemoViewModel(mContext, entry));
	}
	
	private void parseDemos(){
		DemoCategory current = null;
		DemoEntry entry = null;
		XmlResourceParser parser = mContext.getResources().getXml(R.xml.demos);
		try {
			int eventType = parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				switch(eventType){
				case XmlResourceParser.START_TAG:
						if (parser.getName().equals("category") && current == null){
							current = new DemoCategory(mContext, parser.getAttributeValue(null, "name"));
						}
						if (parser.getName().equals("entry")){
							if (current==null)
								throw new Exception();
							entry = new DemoEntry(
									parser.getAttributeValue(null, "name"),
									resolveVM(parser.getAttributeValue(null, "vm")),
									resolveLayout(parser.getAttributeValue(null, "layout")));
							current.Entries.add(entry);
						}else if (parser.getName().equals("raw")){
							if (entry!=null){
								entry.Raws.add(new RawEntry(
										parser.getAttributeValue(null, "title"),
										resolveRaw(parser.getAttributeValue(null, "name")),
										parser.getAttributeValue(null, "type")
										));
							}
						}
					break;
				case XmlResourceParser.END_TAG:
						if (parser.getName().equals("category")){
							if (current==null)
								throw new Exception();
							Categories.add(current);
							current = null;
						}
					break;
				}
				eventType = parser.next();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int resolveRaw(String name) throws Exception{
		if (name.startsWith(".")){
			Class<?> rawClass = gueei.binding.markupDemoICS.R.raw.class;
			return rawClass.getField(name.substring(1)).getInt(null);
		}
		else{
			Class<?> rawClass = R.raw.class;
			return rawClass.getField(name).getInt(null);
		}
    }

	private int resolveLayout(String name) throws Exception{
		if (name.startsWith(".")){
			Class<?> layoutClass = gueei.binding.markupDemoICS.R.layout.class;
			return layoutClass.getField(name.substring(1)).getInt(null);
		}
		else{
			Class<?> layoutClass = R.layout.class;
			return layoutClass.getField(name).getInt(null);
		}
	}
	
	private String resolveVM(String name) throws Exception{
		String pkgName = "gueei.binding.markupDemoICS.viewModels.";
		if (name.startsWith("."))
			pkgName = "com.gueei.demos.markupDemo.viewModels";
		return pkgName + name;
	}
}
