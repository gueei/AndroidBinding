package gueei.binding.converters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import gueei.binding.Command;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.converters.SPAN.SPANHelper;
import gueei.binding.observables.SpanObservable.Span;

/**
 * HIGHLIGHT_SPAN accepts needs three arguments
 * It will return a SpannableString and highlight the given positions
 * 
 * @usage params
 * 
 * @arg hey string
 * @arg needle string
 * @arg SpanCreatorCommand or SpanListCreatorCommand for creating the Spans
 * 
 * @return SpannableString 
 * 
 * @author egandro
 *
 */

public class HIGHLIGHT_SPAN extends Converter<Object> {
	
	private SPANHelper helper = new SPANHelper(this);
	
	public HIGHLIGHT_SPAN(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {		
		if( args.length != 3)
			return null;
		
		if( args[0] == null || args[1] == null || args[2] == null)
			return null;		
		
		String hey = args[0].toString();
		String needle = args[1].toString();
		
		if(needle.length() < 1)
			return args[0];
				
		ArrayList<Span> spanList = new ArrayList<Span>();
		createSpanList(spanList, hey, needle, args[2]);
		
		helper.updateSpanable(args[0], spanList);			
		
		return helper.text;		
	}

	private void createSpanList(ArrayList<Span> spanList, String hey, String needle, Object command) {
		
		int length = needle.length();
		
		int occurence = 1;
		int index = hey.indexOf(needle);
		while (index >=0){
					
			int start=index;
			int end=start+length;
				
			if( command instanceof SpanListCreatorCommand ) {
				SpanListCreatorCommand cmd = (SpanListCreatorCommand)command;
				cmd.Invoke(null, (Object[])new Integer [] { occurence });
				
				List<?> list = cmd.SpanList;				
				if( list != null) {
					for( Object o : list) {
						Span s;
						if(o instanceof Span) {
							Span os = (Span)o;
							s = new Span(os.What,start,end,os.Flags);
						} else {
							s = new Span(o,start,end);
						}
						spanList.add(s);
					}
				}
			} else if(command instanceof SpanCreatorCommand ) {
				SpanCreatorCommand cmd = (SpanCreatorCommand)command;
				cmd.Invoke(null, (Object[])new Integer [] { occurence });
				
				Span os = cmd.Span;
				if(os != null) {
					Span s = new Span(os.What,start,end,os.Flags);
					spanList.add(s);
				}
			} else {
				Span s = new Span(command,start,end);	
				spanList.add(s);
			}
			
			index = hey.indexOf(needle, index+length); 
			occurence++;
		}
	}
	
	public static abstract class SpanCreatorCommand extends Command {
		public Span Span = null;
		
		@Override
		public void Invoke(View view, Object... args) {
			int occurence = 0;
			if( args.length > 0 && args[0] instanceof Integer) {
				occurence = (Integer)args[0];
			}
			Span = onCreateSpan(occurence);
		}
		
		public abstract Span onCreateSpan(int occurence);		
	}
	
	public static abstract class SpanListCreatorCommand extends Command {
		public List<Span> SpanList = null;
		
		@Override
		public void Invoke(View view, Object... args) {
			int occurence = 0;
			if( args.length > 0 && args[0] instanceof Integer) {
				occurence = (Integer)args[0];
			}
			SpanList = onCreateSpanList(occurence);
		}
		
		public abstract List<Span> onCreateSpanList(int occurence);		
	}	
}
