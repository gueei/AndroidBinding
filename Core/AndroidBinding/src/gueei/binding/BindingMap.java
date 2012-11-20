package gueei.binding;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

public class BindingMap{
	private HashMap<String, NameEntry> mEntries = new HashMap<String, NameEntry>();
	
	public String put(String key, String value) {
		mEntries.put(key, new NameEntry(value));
		return null;
	}

	public boolean containsKey(String key){
		if (!mEntries.containsKey(key)) return false;
		return !mEntries.get(key).handled;
	}
	
	public String get(String key){
		return get(key, false);
	}

	public String get(String key, boolean inclHandled){
		NameEntry entry = mEntries.get(key);
		if (entry==null) return null;
		
		return !entry.handled || inclHandled ? entry.name : null;
	}

	public String[] getAllKeys(){
		int len = mEntries.size();
		String[] result = new String[len];
		int i=0; 
		for(String s: mEntries.keySet()){
			result[i] = s;
			i ++;
		}
		return result;
	}
	
	public String[] getAllEntries(){
		int len = mEntries.size();
		String[] result = new String[len];
		int i=0;
		for(NameEntry e : mEntries.values())
		{
			result[i] = e.name;
			i ++;
		}
		return result;
	}
	
	public boolean isEmpty(){
		return mEntries.isEmpty();
	}
	
	public void setAsHandled(String key){
		NameEntry entry = mEntries.get(key);
		if (entry==null) return;
		entry.handled = true;
	}
	
	private class NameEntry{
		public NameEntry(String name){
			this.name = name;
		}
		public String name;
		public boolean handled = false;
		
		@Override
		public String toString(){
			return name + "_handled=" + handled;
		}
	}
	
	public Hashtable<String,String> getMapTable(){
		Hashtable<String,String> table = new Hashtable<String,String>();
		for(Entry<String,NameEntry> ne: mEntries.entrySet()){
			table.put(ne.getKey(), ne.getValue().name);
		}
		return table;
	}
}