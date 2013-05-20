package gueei.binding.labs.validation.validators;

import gueei.binding.labs.validation.ValidatorBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

import android.content.Context;


@Retention(RetentionPolicy.RUNTIME)
public @interface RegexMatch{
	public Class<?> Validator() default RegexMatchValidator.class;

	public String Pattern();
	public String ErrorMessage() default "%fieldname% does not match the regext pattern: %pattern%";
	public String ErrorMessageRes() default "";
	
	public class RegexMatchValidator extends ValidatorBase<RegexMatch> {

		@Override
		public Class<RegexMatch> getAcceptedAnnotation() {
			return RegexMatch.class;
		}

		@Override
		protected String doFormatErrorMessage(Context context,
				RegexMatch parameters, String fieldName,
				String errorMessageFormat) {
			return errorMessageFormat
				.replace("%fieldname%", fieldName)
				.replace("%pattern%", parameters.Pattern());
		}

		@Override
		protected boolean doValidate(Context context, Object value,
				RegexMatch parameters, Object model) {
			// That do not violate with required field
			if (value==null) return true;
			if (value instanceof CharSequence){
				return Pattern.matches(parameters.Pattern(), ((CharSequence)value));
			}
			// if not CharSequence, just return true
			return true;
		}

	}
}
