package gueei.binding;

public interface IReferenceObservableProvider {
	public IObservable<?> getReferenceObservable(int referenceId, String field);
}
