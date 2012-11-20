package gueei.binding.viewAttributes.ratingBar;


import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnRatingBarChangeListenerMulticast;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;


public class RatingViewAttribute extends ViewAttribute<RatingBar, Float>
	implements OnRatingBarChangeListener{

	public RatingViewAttribute(RatingBar view) {
		super(Float.class, view, "rating");
		Binder.getMulticastListenerForView(view, OnRatingBarChangeListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue == null){
			getView().setRating(0);
			return;
		}
		if (newValue instanceof Float){
			getView().setRating((Float)newValue);
			return;
		}
	}

	@Override
	public Float get() {
		if(getView()==null) return null;
		return getView().getRating();
	}

	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (fromUser) this.notifyChanged();
	}
}