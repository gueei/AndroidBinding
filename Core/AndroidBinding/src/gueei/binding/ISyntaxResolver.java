package gueei.binding;

import android.content.Context;

public interface ISyntaxResolver {

	public IObservable<?> constructObservableFromStatement(
	        final Context context, final String bindingStatement,
	        final Object model, final IReferenceObservableProvider refProvider)
	        throws SyntaxResolveException;

	public IObservable<?> constructObservableFromStatement(
	        final Context context, final String bindingStatement,
	        final Object model) throws SyntaxResolveException;

	public abstract Object getFieldForModel(String fieldName, Object model)
            throws SyntaxResolveException;

	public static class SyntaxResolveException extends Exception{
		private static final long serialVersionUID = -5339580312141946507L;

		public SyntaxResolveException() {
			super();
		}

		public SyntaxResolveException(String detailMessage,
				Throwable throwable) {
			super(detailMessage, throwable);
		}

		public SyntaxResolveException(String detailMessage) {
			super(detailMessage);
		}

		public SyntaxResolveException(Throwable throwable) {
			super(throwable);
		}		
	}
	
	public <T> T tryEvaluateValue(Context context, String statement, Object model, T defaultValue);
}