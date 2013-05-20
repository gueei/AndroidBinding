package gueei.binding.labs.validation.validators;

import gueei.binding.labs.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;

@Retention(RetentionPolicy.RUNTIME)
public @interface Required{
	public Class<?> Validator() default RequiredValidator.class;

	public String ErrorMessage() default "%fieldname% is required";
	public String ErrorMessagRes() default "";
	
	public class RequiredValidator extends ValidatorBase<Required> {

		@Override
		public Class<Required> getAcceptedAnnotation() {
			return Required.class;
		}

		@Override
		protected String doFormatErrorMessage(Context context,
				Required parameters, String fieldName, String errorMessageFormat) {
			return errorMessageFormat.replace("%fieldname%", fieldName);
		}

		@Override
		protected boolean doValidate(Context context, Object value,
				Required parameters, Object model) {
			if (value==null) return false;
			if (Boolean.FALSE.equals(value)) return false;
			if (value instanceof CharSequence){
				if (((CharSequence) value).length() == 0) return false;
			}
			return true;
		}
	}
}
