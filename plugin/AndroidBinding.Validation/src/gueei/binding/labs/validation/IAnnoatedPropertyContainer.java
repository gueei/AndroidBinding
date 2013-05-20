package gueei.binding.labs.validation;

import gueei.binding.IObservable;
import java.lang.annotation.Annotation;

public interface IAnnoatedPropertyContainer {
	public String[] getAllObservableNames();
	public IObservable<?> getObservable(String observableName) throws Exception;
	public Annotation[] getAnnotations(String observableName) throws Exception;
}
