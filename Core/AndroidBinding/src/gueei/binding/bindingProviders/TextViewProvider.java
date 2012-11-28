package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.textView.CheckedClickableTextViewAttribute;
import gueei.binding.viewAttributes.textView.CheckedTextViewAttribute;
import gueei.binding.viewAttributes.textView.CompoundDrawableViewAttribute;
import gueei.binding.viewAttributes.textView.MaxLinesViewAttribute;
import gueei.binding.viewAttributes.textView.MinLinesViewAttribute;
import gueei.binding.viewAttributes.textView.OnTextChangedViewEvent;
import gueei.binding.viewAttributes.textView.TextColorViewAttribute;
import gueei.binding.viewAttributes.textView.TextViewAttribute;
import gueei.binding.viewAttributes.textView.TypefaceViewAttribute;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;


public class TextViewProvider extends BindingProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View>ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (view instanceof CheckedTextView) {
			if (attributeId.equals("checked")){
				CheckedTextViewAttribute attr = new CheckedTextViewAttribute((CheckedTextView)view);
				return (ViewAttribute<Tv, ?>) attr;
			}		
			if (attributeId.equals("checkedClickable")){
				CheckedClickableTextViewAttribute attr = new CheckedClickableTextViewAttribute((CheckedTextView)view);
				return (ViewAttribute<Tv, ?>) attr;
			}				
		}		
		if (!(view instanceof TextView)) return null;
		if (attributeId.equals("text")){
			TextViewAttribute attr = new TextViewAttribute((TextView)view, "text");
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("minLines")){
			return (ViewAttribute<Tv, ?>) new MinLinesViewAttribute((TextView)view);
		}
		if (attributeId.equals("maxLines")){
			return (ViewAttribute<Tv, ?>) new MaxLinesViewAttribute((TextView)view);
		}
		if (attributeId.equals("textColor")){
			TextColorViewAttribute attr = new TextColorViewAttribute((TextView)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("onTextChanged")){
			if (view instanceof EditText){
				return (ViewAttribute<Tv, ?>) (new OnTextChangedViewEvent((EditText)view));
			}
		}
		if (attributeId.equals("typeface")){
			TypefaceViewAttribute attr = new TypefaceViewAttribute((TextView)view);
			return (ViewAttribute<Tv, ?>) attr;
		}
		if (attributeId.equals("drawableLeft")){
			CompoundDrawableViewAttribute attr = new CompoundDrawableViewAttribute((TextView)view, "drawableLeft");
			return (ViewAttribute<Tv, ?>) attr;
		}	
		if (attributeId.equals("drawableTop")){
			CompoundDrawableViewAttribute attr = new CompoundDrawableViewAttribute((TextView)view, "drawableTop");
			return (ViewAttribute<Tv, ?>) attr;
		}		
		if (attributeId.equals("drawableRight")){
			CompoundDrawableViewAttribute attr = new CompoundDrawableViewAttribute((TextView)view, "drawableRight");
			return (ViewAttribute<Tv, ?>) attr;
		}		
		if (attributeId.equals("drawableBottom")){
			CompoundDrawableViewAttribute attr = new CompoundDrawableViewAttribute((TextView)view, "drawableBottom");
			return (ViewAttribute<Tv, ?>) attr;
		}					
		
		return null;
	}
}