package gueei.binding.viewAttributes.ratingBar;

import android.widget.RatingBar;
import gueei.binding.Binder;
import gueei.binding.listeners.OnRatingBarChangeListenerMulticast;
import gueei.binding.viewAttributes.ViewEventAttribute;

public class OnRatingChangedViewEvent extends ViewEventAttribute<RatingBar> 
	implements RatingBar.OnRatingBarChangeListener {

	public OnRatingChangedViewEvent(RatingBar view) {
		super(view, "onRatingChanged");
	}

	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (fromUser)
			this.invokeCommand(ratingBar, rating, fromUser);
	}

	@Override
	protected void registerToListener(RatingBar view) {
		Binder.getMulticastListenerForView(view, OnRatingBarChangeListenerMulticast.class).register(this);
	}
}
