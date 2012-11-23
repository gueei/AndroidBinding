package com.gueei.demos.markupDemo.viewModels;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.SpanObservable.Span;
import gueei.binding.observables.StringObservable;
import gueei.binding.observables.SpanObservable;
import gueei.binding.converters.HIGHLIGHT_SPAN.SpanListCreatorCommand;

public class TextViewSpan {
	
	public final StringObservable SimpleText = new StringObservable("a simple text");
	public final SpanObservable SimpleSpan = new SpanObservable(new BackgroundColorSpan(Color.RED), 2, 8);
	
	public final StringObservable ListText = new StringObservable("a text with a span list");	
	public final ArrayListObservable<Span> ListSpan = new ArrayListObservable<SpanObservable.Span>(Span.class);
	
	public final StringObservable HighLight = new StringObservable("a test text to text highlighting");
	public final StringObservable TextToHighLight = new StringObservable("text");
	
	public TextViewSpan() {
		Span s;
		
		ListSpan.clear();
		
		s = new Span(new BackgroundColorSpan(Color.GREEN), 1, 3);
		ListSpan.add(s);
		
		s = new Span(new StrikethroughSpan(), 10, 20);
		ListSpan.add(s);
		
		s = new Span(new BackgroundColorSpan(Color.RED), 4, 5);
		ListSpan.add(s);
		
		s = new Span(new BackgroundColorSpan(Color.YELLOW), 6, 8);
		ListSpan.add(s);		
	}
	
	public final SpanListCreatorCommand OnCreateHighlightSpanList = new SpanListCreatorCommand() {

		@Override
		public List<Span> onCreateSpanList(int occurence) {

			ArrayList<Span> list = new ArrayList<Span>();
			
			Span s = new Span(new BackgroundColorSpan(Color.YELLOW));
			list.add(s);

			s = new Span(new UnderlineSpan());
			list.add(s);
			
			s = new Span(new ForegroundColorSpan(Color.BLUE));
			list.add(s);
			
			return list;
		}
		
	};
	
	public final Command OnChangeSpans = new Command(){
		public void Invoke(View view, Object... args) {			
			SimpleText.set("a SIMPLE text");
			SimpleSpan.set(new Span(new BackgroundColorSpan(Color.BLUE), 2, 8));									
			ListSpan.add(new Span(new BackgroundColorSpan(Color.BLUE), 10, 20));
			HighLight.set("this is a very new text to show text highlighting in all texts");
		}
	};
}