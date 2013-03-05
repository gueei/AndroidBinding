package gueei.binding.labs.validation.validators;

import gueei.binding.IObservable;
import gueei.binding.Utility;
import gueei.binding.labs.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;


@Retention(RetentionPolicy.RUNTIME)
public @interface EqualsTo{
	public Class<?> Validator() default EqualsToValidator.class;
	public String Observable();
	public String ErrorMessage() default "%fieldname% must be the same as %observable%";
	public String ErrorMessageRes() default "";

	public class EqualsToValidator extends ValidatorBase<EqualsTo> {
		@Override
		protected String doFormatErrorMessage(Context context,
				EqualsTo parameters, String fieldName, String errorMessageFormat) {
			return errorMessageFormat
				.replace("%fieldname%", fieldName)
				.replace("%observable%", parameters.Observable());
		}

		@Override
		protected boolean doValidate(Context context, Object value,
				EqualsTo parameters, Object model) {
			try{
				if (value == null) return true;
				IObservable<?> observable = 
						Utility.getObservableForModel(context, parameters.Observable(), model);
				
				if (observable==null) return false;
				Object otherValue = observable.get();
				if ((value instanceof CharSequence) && (otherValue instanceof CharSequence)){
					return compareCharSequence((CharSequence)value, (CharSequence)otherValue);
				}
				return value.equals(otherValue);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public Class<EqualsTo> getAcceptedAnnotation() {
			return EqualsTo.class;
		}

		private boolean compareCharSequence(CharSequence a, CharSequence b){
			if(a==null||b==null) return false;
			if(a.length()!=b.length()) return false;
			for(int i=0; i<a.length(); i++){
				if (a.charAt(i)!= b.charAt(i)) return false;
			}
			return true;
		}
	}
}
