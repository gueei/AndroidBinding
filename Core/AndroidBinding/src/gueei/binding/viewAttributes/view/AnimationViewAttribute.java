package gueei.binding.viewAttributes.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.content.res.Resources;
import gueei.binding.BindingType;
import gueei.binding.DynamicObject;
import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.view.AnimationTrigger.TriggerListener;

public class AnimationViewAttribute extends ViewAttribute<View, AnimationTrigger> implements TriggerListener {

	public AnimationViewAttribute(View view) {
		super(AnimationTrigger.class, view, "animation");
	}

	private AnimationTrigger mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (mValue!=null)
			mValue.removeTriggerListener(this);
		if (newValue instanceof DynamicObject){
			mValue = ConditionalAnimationTrigger.createFromDynamicObject((DynamicObject)newValue);
			mValue.setTriggerListener(this);
			return;
		}
		if (newValue instanceof AnimationTrigger){
			mValue = (AnimationTrigger)newValue;
			mValue.setTriggerListener(this);
			return;
		}
	}

	@Override
	public AnimationTrigger get() {
		return mValue;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (AnimationTrigger.class.isAssignableFrom(type))
			return BindingType.OneWay;
		if (AnimationTrigger.class.equals(type))
			return BindingType.OneWay;
		if (DynamicObject.class.isAssignableFrom(type))
			return BindingType.OneWay;
		return BindingType.NoBinding;
	}

	public void fireAnimation(AnimationTrigger trigger) {
		try{
			Animation anim = AnimationUtils.loadAnimation(getView().getContext(), trigger.getAnimationId());
			getView().startAnimation(anim);
		}catch(Resources.NotFoundException e){
			return;
		}
	}
}
