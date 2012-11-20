package gueei.binding;

import java.util.ArrayList;

public abstract class MulticastListener<Th, T> {
	public abstract void registerToHost(Th host);
	
	protected ArrayList<T> listeners = new ArrayList<T>(0);

	public void removeListener(T listener){
		listeners.remove(listener);
	}
	
	public void register(T listener){
		listeners.add(listener);
	}
	
	public void registerWithHighPriority(T listener){
		listeners.add(0, listener);
	}
	
	private boolean mBroadcasting = true;
	public void nextActionIsNotFromUser(){
		mBroadcasting = false;
	}
	
	protected boolean isFromUser(){
		return mBroadcasting;
	}
	
	protected void clearBroadcastState(){
		mBroadcasting = true;
	}
}
