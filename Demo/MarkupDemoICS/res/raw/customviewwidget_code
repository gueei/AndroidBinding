package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import android.content.Context;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

// Custom View must implement IBindableView
// and override getViewAttribute
public class CustomViewWidget extends TextView implements IBindableView<CustomViewWidget>{
	
	private char PasswordMask;

	public CustomViewWidget(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomViewWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomViewWidget(Context context) {
		super(context);
	}

	private ViewAttribute<CustomViewWidget, CharSequence> PasswordMaskViewAttribute =
			new ViewAttribute<CustomViewWidget, CharSequence>( CharSequence.class, CustomViewWidget.this, "PasswordMask"){
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if ((newValue == null)||(newValue.toString().length()<1)){
						CustomViewWidget.this.setTransformationMethod(null);
						PasswordMask = 0;
						return;
					}
					
					PasswordMask = newValue.toString().charAt(0);
					
					CustomViewWidget.this.setTransformationMethod(new ReplacementTransformationMethod(){
						@Override
						protected char[] getOriginal() {
							return CustomViewWidget.this.getText().toString().toCharArray();
						}

						@Override
						protected char[] getReplacement() {
							int length = CustomViewWidget.this.getText().length();
							char[] output = new char[length];
							for(int i=0; i<length; i++){
								output[i] = PasswordMask;
							}
							return output;
						}
					});
				}

				@Override
				public CharSequence get() {
					return String.valueOf(PasswordMask);
				}
	};
	
	public ViewAttribute<CustomViewWidget, ?> createViewAttribute(
			String attributeName) {
		if (attributeName.equals("mask")) return PasswordMaskViewAttribute;
		return null;
	}
}