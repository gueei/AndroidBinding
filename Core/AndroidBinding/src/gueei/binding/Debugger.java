package gueei.binding;

import java.lang.reflect.Field;

import android.util.Log;
import android.view.View;

public class Debugger {
	public static void graphView(View view, int level, Printer printer, Object caller){
		if (view == null) return;
		if (printer==null) printer = new Printer(level);
		if (level<=0) return;
		
		printer.println("view: " + view.toString(), level);
		
		AttributeCollection collection = Binder.getAttributeCollectionOfView(view);
		for (ViewAttribute<?,?> attr : collection.getAllAttributes()){
			if (attr == caller) continue;
			graphAttribute(attr, level-1, printer, view);
		}
	}
	
	public static void graphAttribute(ViewAttribute<?, ?> attr, int level,
			Printer printer, Object caller) {
		if (attr == null)return;
		if (printer==null) printer = new Printer(level);
		if (level <=0) return;
		printer.println("attribute: " + attr + "\tvalue: " + attr.get(), level);
		IObservable<?> observable = attr.getBindedObservable();
		if (observable==null){
			printer.println("not binded", level);
			return;
		}
		if (attr.getView()!=caller){
			graphView(attr.getView(), level-1, printer, attr.mBridge);
		}
		if (observable!=caller){
			graphObservable(observable, level-1, printer, attr.mBridge);
		}
	}

	public static void graphObservable(IObservable<?> observable, int level,
			Printer printer, Object caller) {
		if (observable == null)return;
		if (printer==null) printer = new Printer(level);
		if (level <=0) return;
		printer.println("observable: " + observable+ "\tvalue: " + observable.get(), level);
		for(Observer observer: observable.getAllObservers()){
			if (observer==caller) continue;
			graphObject(observer, level-1, printer, observable);
		}
	}

	public static void graphObject(Object object, int level,
			Printer printer, Object caller) {
		if (object == null)return;
		if (printer==null) printer = new Printer(level);
		if (level <=0) return;
		if (object instanceof IObservable){
			graphObservable((IObservable<?>)object, level, printer, caller);
			return;
		}
		printer.println("object: " + object.getClass().getName() + "\ttoString: " + object.toString(), level);
		
		// get all fields
		for(Field f: object.getClass().getDeclaredFields()){
			if (!IObservable.class.isAssignableFrom(f.getType()))
				continue;
			try {
				IObservable<?> obs = (IObservable<?>)f.get(object);
				if (obs==caller) continue;
				graphObservable(obs, level-1, printer, object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class Printer{
		final int mTotalLevel;
		public Printer(int totalLevel){
			mTotalLevel = totalLevel;
		}
		public void println(String message, int level){
			String output = "";
			for(int i=mTotalLevel; i>level; i--){
				output += "\t";
			}
			Log.v("BinderV30", output + message);
		}
		public void printSeparator(int level){
			println("-----------------------", level);
		}
	}
}
