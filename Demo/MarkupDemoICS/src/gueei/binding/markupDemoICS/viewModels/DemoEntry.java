package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class DemoEntry {
	public final StringObservable Name = 
			new StringObservable();
	
	public final IntegerObservable LayoutId =
			new IntegerObservable();
	public final StringObservable ModelClassName = 
			new StringObservable();
	
	public final ArrayListObservable<RawEntry> Raws = 
			new ArrayListObservable<RawEntry>(RawEntry.class);
	
	public DemoEntry(){}
	
	public DemoEntry(String name, String className, int layoutId){
		Name.set(name);
		ModelClassName.set(className);
		LayoutId.set(layoutId);
	}
}
