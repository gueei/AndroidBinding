package gueei.binding.viewAttributes.view;

public class AnimationTrigger {

	public AnimationTrigger() {
	}

	public interface TriggerListener{
		public void fireAnimation(AnimationTrigger trigger);
	}

	private TriggerListener mListener;

	public void setTriggerListener(TriggerListener listener) {
		mListener = listener;
	}

	public void removeTriggerListener(TriggerListener listener) {
		mListener = null;
	}

	private int mAnimationId;

	public enum TriggerType{
		True,
		Equal, 
		Change,
		WhenLargerThan,
		FireWhenLarger
	}

	public void notifyAnimationFire() {
		if (mListener!=null)
			mListener.fireAnimation(this);
	}

	public int getAnimationId() {
		return mAnimationId;
	}

	public void setAnimationId(int animationId) {
		mAnimationId = animationId;
	}
}