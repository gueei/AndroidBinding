package gueei.binding.kernel;

import gueei.binding.ConstantObservable;
import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.IPropertyContainer;
import gueei.binding.IReferenceObservableProvider;
import gueei.binding.ISyntaxResolver;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observable;
import gueei.binding.Utility;
import gueei.binding.observables.FloatObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.TypedValue;

/**
 * Updates: 8/5/2012 - Reworked exception handling
 * @author andy
 *
 */
public class DefaultSyntaxResolver implements ISyntaxResolver {
	private static final String DEFAULT_CONVERTER_PACKAGE = "gueei.binding.converters.";
	
	private static final Pattern converterPattern = 
			Pattern.compile("^([$a-zA-Z0-9._]+)\\((.+(\\s*?,\\s*.+)*)?\\)", Pattern.DOTALL);
	private static final Pattern dynamicObjectPattern = Pattern.compile("^\\{(.+)\\}$");

	private static final Pattern numberPattern = Pattern.compile("^(\\+|\\-)?[0-9]*(\\.[0-9]+)?$");
	private static final Pattern resourcePattern = Pattern.compile("^@(([\\w\\.]+:)?(\\w+)/\\w+)$");
	private static final Pattern referencePattern = Pattern.compile("^=@?((\\w+:)?(\\w+)/\\w+).(\\w+)$");
	
	/* (non-Javadoc)
     * @see gueei.binding.ISyntaxResolver#constructObservableFromStatement(android.content.Context, java.lang.String, java.lang.Object, gueei.binding.IReferenceObservableProvider)
     */
	public IObservable<?> constructObservableFromStatement(
			final Context context,
			final String bindingStatement, 
			final Object model,
			final IReferenceObservableProvider refProvider)	throws SyntaxResolveException{
		if(bindingStatement == null)return null;
		if (model==null) return null;
		
		IObservable<?> result;
		String statement = bindingStatement.trim();
		
		result = getReferenceObservable(context, statement, refProvider);
		if (result!=null) return result;
		
		result = getConverterFromStatement(context, statement, model, refProvider);
		if (result!=null) return result;
		result = getDynamicObjectFromStatement(context, statement, model, refProvider);
		if (result!=null) return result;
		result = getObservableForModel(context, statement, model);
		if (result==null){
			throw new SyntaxResolveException
				(String.format("Error when resolving statement '%s'", statement));
		}
		return result;
	}
	
	/* (non-Javadoc)
     * @see gueei.binding.ISyntaxResolver#constructObservableFromStatement(android.content.Context, java.lang.String, java.lang.Object)
     */
	public IObservable<?> constructObservableFromStatement(
			final Context context,
			final String bindingStatement, 
			final Object model) throws SyntaxResolveException{
		return constructObservableFromStatement(context, bindingStatement, model, null);
	}

	private IObservable<?> getReferenceObservable(
			final Context context, String statement, 
			IReferenceObservableProvider refProvider){
		if (!statement.startsWith("=")) return null;
		if (refProvider==null) return null;
		
		Matcher m = referencePattern.matcher(statement);
		if ((!m.matches()) || (m.groupCount()<4))
			return null;
		int id = Utility.resolveResourceId(m.group(1), context, m.group(3));
		if (id<=0) return null;
		return refProvider.getReferenceObservable(id, m.group(4));
	}
	
	private Converter<?> getConverterFromStatement(
			final Context context, String statement, Object model,
			final IReferenceObservableProvider refProvider) throws SyntaxResolveException{
		Matcher m = converterPattern.matcher(statement);
		if (!m.matches())
			return null;
		
		String converterName = m.group(1);
		if (!converterName.contains(".")){
			converterName = DEFAULT_CONVERTER_PACKAGE + converterName;
		}
		try {
			String[] arguments = splitArguments(m.group(2));
			int argumentCount = arguments.length;
			IObservable<?>[] obs = new IObservable[argumentCount];
			for (int i=0; i<argumentCount; i++){
				obs[i] = constructObservableFromStatement(context, arguments[i], model, refProvider);
				if (obs[i] == null){
					return null;
				}
			}
			Constructor<?> constructor = Class.forName(converterName).getConstructor(IObservable[].class);
			Converter<?> converter = (Converter<?>)constructor.newInstance((Object)(obs));
			converter.setContext(context);
			return converter;
		} catch (SyntaxResolveException e){
			throw new SyntaxResolveException
				(String.format("Some argument(s) in the converter statement ('%s') has problem", statement), e);
		} catch (NoSuchMethodException e) {
			throw new SyntaxResolveException(
					String.format(
							"Converter '%s' must define the constructor Converter(IObservable<?>...)", converterName),
					e);
		} catch (ClassNotFoundException e) {
			throw new SyntaxResolveException
				(String.format("Converter '%s' not found", converterName), e);
		} catch (Exception e) {
			throw new SyntaxResolveException
				(String.format("Error when resolving statement '%s'", statement), e);
		}
	}

	private IObservable<?> getDynamicObjectFromStatement
		(final Context context, final String statement, final Object model,
				final IReferenceObservableProvider refProvider) throws SyntaxResolveException {
		Matcher m = dynamicObjectPattern.matcher(statement);
		if (!m.matches()) return null;
		
		DynamicObject dynamic = new DynamicObject();
		String[] arguments = splitArguments(m.group(1));
		int argumentCount = arguments.length;
		for (int i=0; i<argumentCount; i++){
			int indexOfEqual = arguments[i].indexOf('=');
			if (indexOfEqual <=0 ) return null;
			String name = arguments[i].substring(0, indexOfEqual).trim();
			String obsStatement = arguments[i].substring(indexOfEqual+1).trim();
			IObservable<?> obs = constructObservableFromStatement(context, obsStatement, model, refProvider);
			if (obs == null){
				return null;
			}
			dynamic.putObservable(name, obs);
		}
		return dynamic;
	}
	
	private String[] splitArguments(String group){
		if (group==null) return new String[0];
		ArrayList<String> arguments = new ArrayList<String>();
		int bracketCount = 0;
		int curlyBraceCount = 0;
		String progress = "";
		int count = group.length();
		boolean singleQuoteMode = false;
		boolean doubleQuoteMode = false;
		char[] chars = group.toCharArray();
		for(int i=0; i<count; i++){
			if (chars[i]=='\''){
				if (singleQuoteMode) singleQuoteMode=false;
				else if (!doubleQuoteMode) singleQuoteMode = true;
			}
			if (chars[i]=='"'){
				if (doubleQuoteMode) doubleQuoteMode=false;
				else  if (!singleQuoteMode) doubleQuoteMode = true;
			}
			if (chars[i]=='(') bracketCount++;
			if (chars[i]==')') bracketCount--;
			if (chars[i]=='{') curlyBraceCount++;
			if (chars[i]=='}') curlyBraceCount--;
			if (	(chars[i] ==',') && 
					(bracketCount<=0) &&
					(curlyBraceCount<=0) &&
					(progress.length()!=0) &&
					!singleQuoteMode && 
					!doubleQuoteMode
				){
				arguments.add(progress.trim());
				progress = "";
				bracketCount=0;
				curlyBraceCount=0;
			}else{
				progress += chars[i];
			}
		}
		if ((bracketCount<=0) && (progress.length()!=0)){
			arguments.add(progress.trim());
		}
		return arguments.toArray(new String[0]);
	}

	/**
	 * get the Observable (either defined in model, or constants) from model
	 * @param fieldName
	 * @param model
	 * @return IObservable
	 * 
	 * The resolving in done in following order:
	 * 1. String (defined in '')
	 * 2. Integer
	 * 3. Resource
	 * 4. InnerField if containing dot (new)
	 * 4. Observable
	 * 6. Constant from field
	 * 7. No more fall back
	 * @throws SyntaxResolveException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private IObservable<?> getObservableForModel(
			Context context, 
			String fieldName, Object model) throws SyntaxResolveException{
		IObservable<?> result = matchString(fieldName);
		if (result!=null) return result;
		result = matchNumber(fieldName);
		if (result!=null) return result;
		result = matchResource(context, fieldName);
		if (result!=null) return result;
		
		if (model instanceof IPropertyContainer){
			try{
				return ((IPropertyContainer)model).getObservableByName(fieldName);
			}catch(Exception e){
				return null;
			}
		}
		if (fieldName.equals(".")){
			return new Observable(model.getClass(), model);
		}

		if (fieldName.contains(".")){
			InnerFieldObservable ifo = new InnerFieldObservable(fieldName);
			if (ifo.createNodes(model))
				return ifo;
			
			return null;
		}
		
		Object rawField = getFieldForModel(fieldName, model);
		if (rawField instanceof IObservable<?>)
			return (IObservable<?>)rawField;
		
		if (rawField!=null){
			return new ConstantObservable(rawField.getClass(), rawField);
		}
		
		// No more fall back
		return null; // new ConstantObservable<String>(String.class, fieldName);
	}
	
	private IObservable<?> matchString(String fieldName){
		if ((fieldName.startsWith("'") && fieldName.endsWith("'")) ||
				(fieldName.startsWith("\"") && fieldName.endsWith("\"")))
			return new ConstantObservable<String>(String.class, 
					fieldName.substring(1, fieldName.length()-1)
						.replace("\'", "'")
						.replace("\\\"", "\""));
		return null;
	}
	
	private IObservable<?> matchNumber(String fieldName){
		Matcher m = numberPattern.matcher(fieldName);
		if (!m.matches()) return null;
		try{
			if (fieldName.contains(".")){
				return new FloatObservable(Float.parseFloat(fieldName));
			}else{
				return new IntegerObservable(Integer.parseInt(fieldName));
			}
		}catch(Exception e){
			return null;
		}
	}
	
	private IObservable<?> matchResource(Context context, String fieldName)
			throws SyntaxResolveException{
		Matcher m = resourcePattern.matcher(fieldName);
		if ((!m.matches())||(m.groupCount()<2)) return null;
		String typeName = m.group(3);

		int id = Utility.resolveResourceId(fieldName, context, typeName);
		if (id<=0){
			throw new SyntaxResolveException
				(String.format("Resource '%s' not found. Maybe typo? ", fieldName)); 
		}
		if ("layout".equals(typeName))
			return new ConstantObservable<Layout>(Layout.class, new SingleTemplateLayout(id));
		if("id".equals(typeName))
			return new ConstantObservable<Integer>(Integer.class, id);
		
		TypedValue outValue = new TypedValue();
		context.getResources().getValue(id, outValue, true);

		if (typeName.startsWith("drawable")||typeName.startsWith("anim")||typeName.startsWith("menu")||typeName.startsWith("raw"))
			return new ConstantObservable<Integer>(Integer.class, id);
		
		switch(outValue.type){
		case TypedValue.TYPE_STRING:
			return new ConstantObservable<String>(String.class, outValue.string.toString());
		case TypedValue.TYPE_DIMENSION:
			return new ConstantObservable<TypedValue>(TypedValue.class, outValue);
		case TypedValue.TYPE_FRACTION:
		case TypedValue.TYPE_FLOAT:
			return new ConstantObservable<Float>(Float.class, outValue.getFloat());
		case TypedValue.TYPE_INT_BOOLEAN:
			return new ConstantObservable<Boolean>(Boolean.class, outValue.data != 0);
		default:
			return new ConstantObservable<Integer>(Integer.class, outValue.data);
		}
	}
	
	/**
	 * Utility method to get the field for model, 
	 * it also accepts IPropertyContainer as model so the field is 
	 * returned by it rather than reflection
	 * @param fieldName
	 * @param model
	 * @return field object
	 * @throws SyntaxResolveException 
	 */
	@Override
    public Object getFieldForModel(String fieldName, Object model) 
			throws SyntaxResolveException{
		try{
			if (model instanceof IPropertyContainer){
				return ((IPropertyContainer)model).getValueByName(fieldName);
			}
			Field field = model.getClass().getField(fieldName);
			return field.get(model);
		}catch(SyntaxResolveException e){
			return null;
		} catch (IllegalAccessException e) {
			throw new SyntaxResolveException
				(String.format("Error with accessing the field '%s' in the Object '%s'. " +
						"Maybe it is not public accessible. ", 
						fieldName, model.toString()), e);
		} catch (NoSuchFieldException e) {
			return null;
		} catch (Exception e) {
			return null;
		} 
	}

	@SuppressWarnings("unchecked")
    @Override
    public <T> T tryEvaluateValue(Context context, String statement, Object model, T defaultValue) {
		try{
			IObservable<?> obs = this.constructObservableFromStatement(context, statement, model);
			return (T)obs.get();
		}catch(Exception e){
			return defaultValue;
		}
    }
}
