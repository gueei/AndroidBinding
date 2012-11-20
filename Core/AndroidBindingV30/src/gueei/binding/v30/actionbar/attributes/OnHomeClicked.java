package gueei.binding.v30.actionbar.attributes;

import android.os.Bundle;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.v30.actionbar.BindableActionBar;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnHomeClicked extends ViewEventAttribute<BindableActionBar> {
	public OnHomeClicked(BindableActionBar view) {
		super(view, "onHomeClicked");
	}

	@Override
	protected void registerToListener(BindableActionBar view) {
		EventAggregator.getInstance(view.getActivity()).subscribe("Clicked(android.R.id.home)", 
				new EventSubscriber(){
					public void onEventTriggered(String eventName,
							Object publisher, Bundle data) {
						OnHomeClicked.this.invokeCommand(getView());
					}
		});
	}
}
