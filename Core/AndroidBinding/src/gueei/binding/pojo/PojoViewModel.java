package gueei.binding.pojo;

/**
 * Interface PojoViewModel must be implemented by any ViewModels in Pojo Style.
 * It must instantiate and keep its own instance of PojoViewModelHelper.
 * notifyPropertyChanged(String) should use getHelper().notifyPropertyChanged(String) in its body
 * Whenever a public property is changed, it need to call 
 * notifyPropertyChanged(PROPERTY_NAME) 
 * for all affected properties
 * @author andy
 *
 */
public interface PojoViewModel {
	PojoViewModelHelper getHelper();
	void notifyPropertyChanged(String propertyName);
}