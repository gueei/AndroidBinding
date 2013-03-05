package gueei.binding.labs.validation;

import gueei.binding.labs.validation.validators.Required.RequiredValidator;
import android.content.Context;


/**
 * Update notice: removed SetObservable to make Validator more stateless. 
 * Also required to pass Context to it, since it is sometimes necessary to resolve e.g. Resource String
 * by the validator
 * @author andy
 *
 * @param <Ta> Annotation Interface, which requires the following parameters:
 * 1. Validator() - default to this Validator class
 * 2. ErrorMessageRes() - string default to null
 * 3. ErrorMessage - string
 * 
 * Example:
 * public @interface Required{
 *     public Class<?> Validator() default RequiredValidator.class;
 *     public String ErrorMessageRes() default null;
 *     public String ErrorMessage() default "%fieldname% is required.";
 * }
 * 
 * 
 */
public abstract class ValidatorBase<Ta> {
	public abstract Class<Ta> getAcceptedAnnotation();
	
	/**
	 * Note to subclasses: it must have this constructor with exactly same signature.
	 * The ModelValidator will construct with this version of constructor only
	 * @param value
	 */
	public ValidatorBase(){}
	
	public final boolean Validate(Context context, Object value, Ta parameters, Object model){
		return doValidate(context, value, parameters, model);
	}
	
	protected abstract boolean doValidate(Context context, Object value, Ta parameters, Object model);
	
	public final String formatErrorMessage(Context context, Ta parameters, String fieldName, String errorMessage){
		return doFormatErrorMessage(context, parameters, fieldName, errorMessage);
	}
	
	/**
	 * Format the errorMessage to something meaningful
	 * errorMessageFormat is provided by ModelValidator, but implementation can choose to ignore it
	 * @param context
	 * @param parameters
	 * @param fieldName
	 * @param errorMessageFormat
	 * @return formatted error message
	 */
	protected abstract String doFormatErrorMessage
		(Context context, Ta parameters, String fieldName, String errorMessageFormat);
	/**
	 * Validator should be designed in use-and-dispose manner. So if any resources it is holding, 
	 * it is advised to release it here.
	 */
	public void recycle(){}
}
