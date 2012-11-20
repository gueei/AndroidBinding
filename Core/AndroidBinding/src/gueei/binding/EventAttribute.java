package gueei.binding;


public abstract class EventAttribute<Th> extends Attribute<Th, Command> {
	public EventAttribute(Th host, String attributeName) {
		super(Command.class, host, attributeName);
		registerToListener(host);
	}

	protected abstract void registerToListener(Th host);
	
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
	
	public void invokeCommand(Th host, Object...args){
		if (mCommand!=null)
			mCommand.InvokeCommand(null, host, args);
	}
}
