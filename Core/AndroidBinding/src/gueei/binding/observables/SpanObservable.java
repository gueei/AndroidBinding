package gueei.binding.observables;

import gueei.binding.Observable;

public class SpanObservable extends Observable<SpanObservable.Span> {
	
	public static class Span {
		public Object What=null;
		public int Start=0;
		public int End=0;
		public int Flags=0;
		
		public Span() {
		}
		
		public Span(Object what){
			this.What = what;
		}
		
		public Span(Object what, int start, int end){
			this.What = what;
			this.Start = start;
			this.End = end;
		}		
		
		public Span(Object what, int start, int end, int flags){
			this.What = what;
			this.Start = start;
			this.End = end;
			this.Flags = flags;
		}
				
	}	
	
	public SpanObservable() {
		super(Span.class);
	}
	
	public SpanObservable(Span value){
		super(Span.class, value);
	}
	
	public SpanObservable(Object what){
		super(Span.class, new Span(what));
	}
	
	public SpanObservable(Object what, int start, int end){
		super(Span.class, new Span(what, start, end));
	}
	
	public SpanObservable(Object what, int start, int end, int flags){
		super(Span.class, new Span(what, start, end, flags));
	}
}
