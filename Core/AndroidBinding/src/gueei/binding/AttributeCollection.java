package gueei.binding;

import java.util.HashMap;

public class AttributeCollection {
	
	private HashMap<String, ViewAttribute<?,?>> collection;
	
	public AttributeCollection(){
		collection = new HashMap<String, ViewAttribute<?,?>>(5);
	}
	
	public boolean containsAttribute(String attrId){
		return collection.containsKey(attrId);
	}
	
	public void putAttribute(String attrId, ViewAttribute<?, ?> attribute){
		collection.put(attrId, attribute);
	}
	
	public ViewAttribute<?, ?> getAttribute(String attrId){
		if (collection.containsKey(attrId)){
			return collection.get(attrId);
		}
		return null;
	}
	
	public ViewAttribute<?,?>[] getAllAttributes(){
		return collection.values().toArray(new ViewAttribute<?,?>[0]);
	}
}
