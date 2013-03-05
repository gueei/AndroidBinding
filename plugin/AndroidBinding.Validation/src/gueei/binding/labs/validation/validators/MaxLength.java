package gueei.binding.labs.validation.validators;

import gueei.binding.labs.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;


@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength{
	public Class<?> Validator() default MaxLengthValidator.class;
	
	public int Length();

	public String ErrorMessage() default "%fieldname% cannot have more than %length% characters";
	public String ErrorMessageRes() default "";
	
	public class MaxLengthValidator extends ValidatorBase<MaxLength> {

		@Override
		public Class<MaxLength> getAcceptedAnnotation() {
			return MaxLength.class;
		}

		@Override
		protected String doFormatErrorMessage(Context context,
				MaxLength parameters, String fieldName,
				String errorMessageFormat) {
			return 
				errorMessageFormat
				.replace("%fieldname%", fieldName)
				.replace("%length%", parameters.Length() + "");
		}

		@Override
		protected boolean doValidate(Context context, Object value,
				MaxLength parameters, Object model) {
			if (value==null) return false;
			if (!(value instanceof CharSequence)) return true;
			return ((CharSequence)value).length() <= parameters.Length();
		}
	}
}
