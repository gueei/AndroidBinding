package gueei.binding.markupDemoICS.viewModels;

import android.app.Activity;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;

public class ShowDemoViewModel {
	public final Observable<CharSequence> DemoName = new Observable<CharSequence>(CharSequence.class);
	public final IntegerObservable DemoLayout =
			new IntegerObservable(0);
	public final Observable<Object> DemoVm = new Observable<Object>(Object.class);
	public final IntegerObservable DemoLayoutRaw =
			new IntegerObservable(0);
	public final ArrayListObservable<RawEntry> Raws = 
			new ArrayListObservable<RawEntry>(RawEntry.class);
	
	public ShowDemoViewModel(Activity activity, DemoEntry entry){
		DemoName.set(entry.Name.get());
	    DemoLayout.set(entry.LayoutId.get());
	    DemoVm.set(createVm(activity, entry.ModelClassName.get()));
	    Raws.setAll(entry.Raws);
	}
	
	private Object createVm(Activity activity, String demoClassName){
		try {
			return Class.forName(demoClassName).newInstance();
		} catch (Exception e) {
			try {
				return Class.forName(demoClassName).getConstructor(Activity.class).newInstance(activity);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
