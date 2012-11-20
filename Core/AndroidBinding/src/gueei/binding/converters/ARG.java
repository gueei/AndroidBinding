package gueei.binding.converters;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Passing extra arguments to command.
 * Since command's invoke is invoke(View, args), by using this, it will shift the original args to right, 
 * so that the extra arguments supplied will always be the first few arguments when command is invoked. <br/>
 * For example, if you bind onChecked="Fire", your Fire command will receive (View, boolean) 
 * (the same as http://developer.android.com/reference/android/widget/CompoundButton.OnCheckedChangeListener.html) <br/>
 * But if you bind onChecked="ARG(Fire, 1, 2)", you will receive (View, integer, integer, boolean) in the invoke <br/>
 * You can supply any number of extra arguments
 * 
 * @usage command arg ...
 * 
 * @arg command gueei.binding.Command
 * @arg arg Object
 * 
 * @return command gueei.binding.Command
 * 
 * @author andytsui
 *
 */
public class ARG extends Converter<Command>{
	private Command mCommand;
	private Object[] mArgs;
	
	private Command mOut = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (mCommand!=null)
				mCommand.Invoke(view, mArgs); // Original Argument will not include
		}
	};
	
	public ARG(IObservable<?>[] dependents) {
		super(Command.class, dependents);
	}

	@Override
	public Command calculateValue(Object... args) throws Exception {
		if (args.length<1) return null;
		if (args[0] instanceof Command){
			mCommand = (Command)args[0];
		}
		if (args.length>1){
			mArgs = new Object[args.length-1];
			System.arraycopy(args, 1, mArgs, 0, args.length-1);
		}
		return mOut;
	}
}