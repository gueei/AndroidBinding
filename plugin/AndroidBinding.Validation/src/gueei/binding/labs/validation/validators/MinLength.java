package gueei.binding.labs.validation.validators;

import gueei.binding.labs.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;


@Retention(RetentionPolicy.RUNTIME)
public @interface MinLength{
	public Class<?> Validator() default MinLengthValidator.class;
	
	public int Length();

	public String ErrorMessage() default "%fieldname% needs have at least %length% characters";
	public String ErrorMessageRes() default "";
	
	public class MinLengthValidator extends ValidatorBase<MinLength> {

		@Override
		public Class<MinLength> getAcceptedAnnotation() {
			return MinLength.class;
		}

		@Override
		protected String doFormatErrorMessage(Context context,
				MinLength parameters, String fieldName,
				String errorMessageFormat) {
			return 
				errorMessageFormat
				.replace("%fieldname%", fieldName)
				.replace("%length%", parameters.Length() + "");
		}

		@Override
		protected boolean doValidate(Context context, Object value,
				MinLength parameters, Object model) {
			if (value==null) return false;
			if (!(value instanceof CharSequence)) return true;
			return ((CharSequence)value).length() >= parameters.Length();
		}
	}
}
