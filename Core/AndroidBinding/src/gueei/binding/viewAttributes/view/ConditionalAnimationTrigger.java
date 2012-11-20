package gueei.binding.viewAttributes.view;

import java.util.Collection;

import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;

public class ConditionalAnimationTrigger extends AnimationTrigger implements Observer{
	private IObservable<?> mTrigger, mCondition;
	private IObservable<TriggerType> mType;
	private IObservable<? extends Integer> mId;
	
	public ConditionalAnimationTrigger(IObservable<? extends Integer> id, IObservable<?> trigger, 
			IObservable<TriggerType> type, 
			IObservable<?> condition) {
		mId = id;
		mTrigger = trigger;
		mType = type;
		mCondition = condition;
		mId.subscribe(this);
		mTrigger.subscribe(this);
		mType.subscribe(this);
		mCondition.subscribe(this);
	}

	public Object TriggerValue;
	public Object TriggerCondition;
	public TriggerType Type;
	private Object lastValue;
	private boolean lastTimeLarger = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void evaluateAnimation(){
		if (ConditionalAnimationTrigger.TriggerType.Change.equals(Type)){
			if ((lastValue!=null)&&(!lastValue.equals(TriggerValue)))
				notifyAnimationFire();
			return;
		}
		
		if (TriggerValue == null) return;
		switch (Type){
		case True:
			if (Boolean.TRUE.equals(TriggerValue)) notifyAnimationFire();
			break;
		case Equal:
			if(TriggerValue.equals(TriggerCondition)) notifyAnimationFire();
		case WhenLargerThan:
			if (TriggerValue instanceof Comparable){
				int current = ((Comparable) TriggerValue).compareTo(TriggerCondition);
				if (current>0){
					if (lastTimeLarger) return;
					notifyAnimationFire();
				}else{
					lastTimeLarger = false;
					return;
				}
			}
		case FireWhenLarger:
			if (TriggerValue instanceof Comparable){
				int current = ((Comparable) TriggerValue).compareTo(TriggerCondition);
				if (current>0){
					notifyAnimationFire();
				}else{
					lastTimeLarger = false;
					return;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ConditionalAnimationTrigger createFromDynamicObject(DynamicObject obj){
		if (!(obj.observableExists("id") && obj.observableExists("trigger")))
			return null;
		try {
			IObservable<Integer> id = (IObservable<Integer>)obj.getObservableByName("id");
			IObservable<?> trigger = obj.getObservableByName("trigger");
			IObservable<TriggerType> type = null;
			IObservable<?> condition = null;
			if (obj.observableExists("type")){
				if (obj.observableExists("condition")){
					condition = obj.getObservableByName("condition");
				}else{
					condition = new Observable<Object>(Object.class);
				}
				type = (IObservable<TriggerType>)obj.getObservableByName("type");
			}
			else{
				type = new Observable<TriggerType>(TriggerType.class, TriggerType.True);
			}
			return new ConditionalAnimationTrigger(id, trigger, type, condition);
		} catch (Exception e) {
			return null;
		}
	}

	public void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		setAnimationId((int)mId.get());
		TriggerValue = mTrigger.get();
		Type = mType.get();
		TriggerCondition = mCondition.get();
		evaluateAnimation();
	}
}