package gueei.binding.viewAttributes;

import gueei.binding.Command;
import gueei.binding.ViewAttribute;
import android.view.View;

public abstract class ViewEventAttribute<T extends View> extends ViewAttribute<T, Command> {
	public ViewEventAttribute(T view, String viewAttributeName) {
		super(Command.class, view, viewAttributeName);
		registerToListener(view);
	}

	/**
	 * Child should implement this and register itself to the respective 
	 * Multicast listener by calling:
	 *     BinderV30.getMulticastListener(view, listenerType).register(this);
	 * @param view
	 */
	protected abstract void registerToListener(T view);
	
	private Command mCommand;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {		
		if (newValue instanceof Command){
			mCommand = (Command)newValue;
		}
	}

	@Override
	public Command get() {
		return mCommand;
	}
	
	public void invokeCommand(View view, Object...args){
		if (mCommand!=null)
			mCommand.InvokeCommand(view, args);
	}
}
