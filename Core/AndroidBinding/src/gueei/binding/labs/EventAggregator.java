package gueei.binding.labs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import android.content.Context;
import android.os.Bundle;

public class EventAggregator {
	private static WeakHashMap<Context, EventAggregator> instances = 
			new WeakHashMap<Context, EventAggregator>();
	
	public static EventAggregator getInstance(Context context){
		if (!instances.containsKey(context)){
			instances.put(context, new EventAggregator());
		}
		return instances.get(context);
	}
	
	public static void removeRefs(Context context) {
		if(context == null)
			return;		
		if(instances.containsKey(context))
			instances.remove(context);
	}
	
	private HashMap<String, ArrayList<EventSubscriber>>
		EventNameSubscribersMap = new HashMap<String, ArrayList<EventSubscriber>>();
	
	public void publish(final String eventName, final Object publisher, final Bundle data){
		ArrayList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);
		
		// Nobody subscribes this
		if (subscribers==null) return;
		
		int count = subscribers.size();
		
		// All Subscribers removed (maybe GCed)
		if (count==0){
			subscribers.remove(eventName);
			return;
		}
		
		for (int i=0; i<count; i++){
			EventSubscriber subscriber = subscribers.get(i);
			if (subscriber!=null) subscriber.onEventTriggered(eventName, publisher, data);
		}
	}
	
	public void subscribe(final String eventName, final EventSubscriber subscriber){
		ArrayList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);
		if (subscribers==null){
			subscribers = new ArrayList<EventSubscriber>();
			EventNameSubscribersMap.put(eventName, subscribers);
		}
		
		subscribers.add(subscriber);
	}
}
