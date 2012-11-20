package gueei.binding.converters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import gueei.binding.Command;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.observables.SpanObservable.Span;


/**
 * highlights all occurences in a given text with a span
 * 
 * Param 0: TextInput
 * Param 1: Text to search
 * Param 2: SpanCreatorCommand or SpanListCreatorCommand for creating the Spans
 * 
 * @return list of Spans
 */

public class HIGHLIGHT_SPAN extends Converter<Object> {
	
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
			return null;
		
		int length =needle.length();
		
		ArrayList<Span> result = new ArrayList<Span>();
			
		int occurence = 1;
		int index = hey.indexOf(needle);
		while (index >=0){
					
			int start=index;
			int end=start+length;
				
			if( args[2] instanceof SpanListCreatorCommand ) {
				SpanListCreatorCommand cmd = (SpanListCreatorCommand)args[2];
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
						result.add(s);
					}
				}
			} else if( args[2] instanceof SpanCreatorCommand ) {
				SpanCreatorCommand cmd = (SpanCreatorCommand)args[2];
				cmd.Invoke(null, (Object[])new Integer [] { occurence });
				
				Span os = cmd.Span;
				if(os != null) {
					Span s = new Span(os.What,start,end,os.Flags);
					result.add(s);
				}
			} else {
				Span s = new Span(args[2],start,end);	
				result.add(s);
			}
			
			index = hey.indexOf(needle, index+length); 
			occurence++;
		}
		
		if(result.size() == 0)
			return null;
		return result;
	}
}
