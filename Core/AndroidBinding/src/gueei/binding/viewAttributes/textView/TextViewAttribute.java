package gueei.binding.viewAttributes.textView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.TextWatcherMulticast;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * (displayed) Text in Text View
 * Note that you should supply IObservable<CharSequence> to it in order to allow two-way binding 
 * Although it accepts String or other object type, it will simply evaluated as Object.toString() 
 * but changes in the Text View will not reflect in VM unless IObservable<CharSequence> is supplied
 * 
 * @name text
 * @widget TextView
 * @type CharSequence
 * 
 * @accepts	CharSequence
 * @accepts Object

 * @category simple
 * @related http://developer.android.com/reference/android/widget/TextView.html
 * 
 * @author andy
 */
public class TextViewAttribute extends ViewAttribute<TextView, CharSequence> implements TextWatcher {

	private CharSequence mValue = null;
	public TextViewAttribute(TextView view, String attributeName) {
		super(CharSequence.class, view, attributeName);
		if (view instanceof EditText) {
			Binder.getMulticastListenerForView(view, TextWatcherMulticast.class).registerWithHighPriority(this);
		}
	}

	@Override
	public CharSequence get() {
		if(getView()==null) return null;
		return cloneCharSequence(getView().getText());
	}

	private CharSequence cloneCharSequence(CharSequence o) {
		return o.subSequence(0, o.length());
	}

	private boolean compareCharSequence(CharSequence a, CharSequence b) {
		boolean result = false;
		if (a != null) {
			if (b!=null)
				result = a.toString().equals(b.toString());
		}else{
			if (b==null) result = true;
		}
		
		// it's the same text sequence - but if it's a spanable text the look can be different
		if( result == true && a instanceof SpannableString && b instanceof SpannableString) { 
			return a.equals(b);
		}			
		
		return result;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		synchronized (this) {
			CharSequence nVal = "";
			if (null != newValue) {
				if (newValue instanceof CharSequence) {
					nVal = (CharSequence) newValue;
				} else {
					nVal = newValue.toString();
				}
			}
			if (!compareCharSequence(nVal, mValue)) {				
				mValue = cloneCharSequence(nVal);					
				getView().setTextKeepState(cloneCharSequence(nVal));
			}
		}
	}

	public void afterTextChanged(Editable arg0) {
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		synchronized (this) {
			if (compareCharSequence(mValue, arg0))
				return;
			mValue = cloneCharSequence(arg0);

			this.notifyChanged();
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (CharSequence.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		if (type.isAssignableFrom(CharSequence.class))
			return BindingType.TwoWay;
		return BindingType.OneWay;
	}
}
