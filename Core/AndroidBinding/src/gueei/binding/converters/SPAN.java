package gueei.binding.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.text.Spannable;
import android.text.SpannableString;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.observables.SpanObservable.Span;

/**
 * SPAN accepts needs two arguments
 * It will return a SpannableString
 * 
 * @usage params
 * 
 * @arg input text string
 * @arg args Object (Span or List of Spans)
 * 
 * @return SpannableString 
 * 
 * @author egandro
 *
 */
public class SPAN extends Converter<Object> {
	
	private SPANHelper helper = new SPANHelper(this);

	public SPAN(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... args) throws Exception {		
		if(args.length == 0) return null;
		if(args.length != 2) return args[0];				
		if(args[0] == null) return args[0];												
		
		helper.updateSpanable(args[0], args[1]);			
		
		return helper.text;
	}
	
	public static class SPANHelper {	
		Converter<?> parent;
		Object oldValue;
		ObservableCollection<?> collection;
		SpannableString text;
		
		public SPANHelper(Converter<?> parent) {
			this.parent = parent;
		}
		
		private CollectionObserver attrObserver = new CollectionObserver(){
			@Override
			public void onCollectionChanged(IObservableCollection<?> collection,
					CollectionChangedEventArg args, Collection<Object> initiators) {	
				updateSpanable(null, collection);
				parent.notifyChanged();
			}
		};
		
		public void updateSpanable(Object textValue, Object newValue) {
			if(newValue == null)
				return;
			
			if(textValue != null) {
				if( textValue instanceof SpannableString)
					text = (SpannableString)textValue;
				else
					text = new SpannableString(textValue.toString());
			}
			
			if(text == null)
				return;
			
			int length = text.length();
			
			if(textValue == null && oldValue != null) {
				if(oldValue instanceof Span ) {
					text.removeSpan(((Span) oldValue).What);
				} else if( oldValue instanceof List) {
					@SuppressWarnings("rawtypes")
					List list = (List)oldValue;				
					for( Object o : list) {
						if(o instanceof Span)
							text.removeSpan(((Span) o).What);	
					}
				}
			}	
			
			if(collection != null) {
				collection.unsubscribe(attrObserver);
				collection = null;
			}
			
			if( newValue instanceof Span ) {
				Span s = (Span) newValue;
				if(s.What != null)
					safeSetSpan(text,s,length);
				oldValue = newValue;
			} else if(newValue instanceof List) {
				ArrayList<Object> newList = new ArrayList<Object>();
				oldValue = newList;
				
				List<?> list = (List<?>)newValue;		
				
				for( Object o : list) {
					if( o instanceof Span) {
						Span s = (Span) o;
						if(s.What != null) {
							if( safeSetSpan(text,s,length) )						
								newList.add(s);
						}
					}
				}			
				
				if(newValue instanceof ObservableCollection<?>) {
					collection = (ObservableCollection<?>)newValue;
					collection.subscribe(attrObserver);
				}
				
				// we need to create a new spanable - the set() of the textview only updates in case of a change
				if(list != null)
					text = new SpannableString(text);
			} else {
				oldValue = null;
			}
		}

		private boolean safeSetSpan(Spannable sText, Span s, int length) {
			if(sText == null || s == null || length < 1)
				return false;
			
			int start = s.Start;
			int end = s.End;
			
			if(start == 0 && end == 0)
				end = length;
			
			if(start > length)
				return false;		
			if(end > length)
				return false;			
			if(end < start)
				return false;
			
			sText.setSpan(s.What, start, end, s.Flags);
			return true;
		}		
	}	
	
}
