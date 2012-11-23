package com.gueei.demos.markupDemo.viewModels;

import android.view.View;
import gueei.binding.markupDemoICS.R;
import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

public class SwitchableItemTemplate {

	public final ArrayListObservable<ArrayListItem> Items     = new ArrayListObservable<ArrayListItem>(ArrayListItem.class);
	public final ArrayListObservable<String>        Templates = new ArrayListObservable<String>(String.class);

	public SwitchableItemTemplate() {
		for (int i = 0; i < 10; i++) {
			Items.add(new ArrayListItem());
		}
		Templates.add("Template1");
		Templates.add("Template2");
	}

	public final ObjectObservable            SelectedTemplate = new ObjectObservable();
	public final DependentObservable<Layout> ItemTemplate     = new DependentObservable<Layout>(Layout.class, SelectedTemplate) {
		@Override
		public Layout calculateValue(Object... args) throws Exception {
			if (SelectedTemplate.get() == null) {
				return null;
			}
			if (SelectedTemplate.get().equals("Template1")) {
				return new SingleTemplateLayout(R.layout.arraylist_item2);
			}
			return new SingleTemplateLayout(R.layout.arraylist_item1);
		}
	};
	public final DependentObservable<Layout> SpinnerTemplate  = new DependentObservable<Layout>(Layout.class, SelectedTemplate) {
		@Override
		public Layout calculateValue(Object... args) throws Exception {
			if (SelectedTemplate.get() == null) {
				return null;
			}
			if (SelectedTemplate.get().equals("Template1")) {
				return new SingleTemplateLayout(R.layout.simple_spinner_item);
			}
			return new SingleTemplateLayout(R.layout.simple_spinner_item2);
		}
	};

	public class ArrayListItem {

		private static final String                      Prefix     = "Item: ClickCount=";
		public final         Command                     ClickTitle = new Command() {
			public void Invoke(View view, Object... args) {
				ClickCount.set(ClickCount.get() + 1);
			}
		};
		public final         IntegerObservable           ClickCount = new IntegerObservable(0);
		public final         DependentObservable<String> Title      = new DependentObservable<String>(String.class, ClickCount) {
			@Override
			public String calculateValue(Object... args) throws Exception {
				return Prefix + ClickCount.get();
			}
		};

		@Override public String toString() {
			// simple_spinner_item(2).xml has no binding to "Title"
			return Title.get();
		}
	}
}
