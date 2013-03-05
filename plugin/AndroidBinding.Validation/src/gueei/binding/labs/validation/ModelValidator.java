package gueei.binding.labs.validation;

import gueei.binding.IObservable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;

/**
 * ModelValidator changes to be non-static
 * [Enhancement]Model results now can retrieved by observable name
 * [Plan]Implement resource string retrieval (problem due to non-final id in new adt)
 * [Plan]Implement deferred (and probably Async) model validation
 * @author andy
 *
 */
public class ModelValidator {
	
	private final Context mContext;
	private final Object mModel;
	private final Class<?> mResClass;
	
	/**
	 * Constructor
	 * @param context Calling Context
	 * @param model Model to validate
	 * @param resClass Class of the resource, e.g. R.string
	 */
	public ModelValidator(Context context, Object model, Class<?> resClass){
		mContext = context;
		mModel = model;
		mResClass = resClass;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ValidationResult ValidateModel(){
		ValidationResult result = new ValidationResult();
		IAnnoatedPropertyContainer modelContainer;
		if (mModel instanceof IAnnoatedPropertyContainer){
			modelContainer = (IAnnoatedPropertyContainer)mModel;
		}else{
			modelContainer = new ReflectionAnnotatedPropertyContainer(mModel);
		}

		String[] observableNames = modelContainer.getAllObservableNames();
		
		for(String obsName: observableNames){
			try{
				IObservable observable = modelContainer.getObservable(obsName);
				if (observable==null) continue;
				
				Annotation[] annotations = modelContainer.getAnnotations(obsName);
				
				for(Annotation annotation : annotations){
					Method m = annotation.getClass().getMethod("Validator");
					Class<?> validation = (Class<?>)m.invoke(annotation);
					ValidatorBase validator = (ValidatorBase)validation.getConstructor().newInstance();
					if (!validator.Validate(
							mContext, observable.get(), 
							validator.getAcceptedAnnotation().cast(annotation), mModel)){
						result.putValidationError(
								obsName,
								validator.formatErrorMessage(
										mContext, 
										validator.getAcceptedAnnotation().cast(annotation), 
										obsName,
										getErrorMessage(annotation)));
					}
					validator.recycle();
				}
			}catch(Exception e){
				// ignore all errors
				continue;
			}
		}
		
		return result;
	}
	
	/**
	 * Utility method to read error message res / message from validator annotation class
	 * Since annotation does not allow inheritance, we have to do in a dirty way
	 * 
	 * @param annotation The validation annotation object, 
	 * should contain ErrorMessageRes() and/or ErrorMessage() to work, otherwise, nothing will be returned
	 * @return
	 */
	protected String getErrorMessage(Annotation annotation){
		if (mResClass!=null){
			try {
				// Resource name has highest priority
				String resName = annotation.getClass()
						.getMethod("ErrorMessageRes").invoke(annotation).toString();
				
				if (resName.length()>0){
					// We come a long way to achieve this.... since res id is no longer final... 
					int resId = (Integer)mResClass.getField(resName).get(null);
					if (resId>0){
						return mContext.getResources().getString(resId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			return annotation.getClass()
					.getMethod("ErrorMessage")
					.invoke(annotation)
					.toString();
		}catch(Exception e){
			return null;
		}
	}
}
