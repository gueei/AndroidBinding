package gueei.binding;

import gueei.binding.utility.WeakList;
import android.view.View;

public abstract class Command extends Observable<Command> {
	public Command() {
		super(Command.class);
	}

	@Override
	public Command get() {
		return this;
	}

	public void InvokeCommand(View view, Object... args){
		for(Object l: listeners.toArray()){
			((CommandListener)l).onBeforeInvoke();
		}
		Invoke(view, args);
		for(Object l: listeners.toArray()){
			((CommandListener)l).onAfterInvoke();
		}
	}
	
	public abstract void Invoke(View view, Object... args);
	
	WeakList<CommandListener> listeners = new WeakList<CommandListener>();
	
	public void addCommandListener(CommandListener l){
		listeners.add(l);
	}
	
	public void removeCommandListener(CommandListener l){
		listeners.remove(l);
	}
	
	public interface CommandListener{
		public void onBeforeInvoke();
		public void onAfterInvoke();
	}
}
