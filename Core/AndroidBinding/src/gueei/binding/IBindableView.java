package gueei.binding;

import android.view.View;

/*
 * IBindableView is the interface that supports Custom Views
 * (For views that is not supported in Android-Binding)
 * For any custom view, you should implement this interface 
 * in order for the Attribute BinderV30 to recognize it.
 * 
 * By Design, the Attribute BinderV30 would put custom ViewAttributes to higher
 * precedence, for example, if you want to override the default behavior
 * of the "visibility" tag, you can capture that value and provide another ViewAttribute
 * associated with that tag. In this case, Attribute BinderV30 will stop looking for any 
 * other "parent" to create this attribute. 
 */
public interface IBindableView<T extends View & IBindableView<T>> {
	/**
	 * Each View Attribute should be created once only. 
	 * The Custom View is suppose to create and return the designated attribute here
	 * Once it is created, it will be maintained by the Binder, just like other system ViewAttributes
	 * If you want to override the default behavior of other ViewAttributes, you can return it here
	 * or else, returning null will pass the control to super classes' implementation.
	 * To access the view Attribute, you can either maintain a reference to the attribute, or use
	 * Binder.getAttributeForView(View, AttributeId);
	 * @param attributeId : Attribute Id
	 * @return the ViewAttribute, or null if don't want to handle
	 */
	public ViewAttribute<? extends View, ?> createViewAttribute(String attributeId);
}