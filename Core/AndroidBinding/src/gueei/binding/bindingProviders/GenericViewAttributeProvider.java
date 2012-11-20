package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.GenericViewAttribute;

import java.lang.reflect.Method;

import android.view.View;


public class GenericViewAttributeProvider extends BindingProvider {

	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, String attributeId) {
		return tryCreateGenericViewAttribute(view, attributeId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    private ViewAttribute<View, ?> tryCreateGenericViewAttribute(View view,
            String attributeId) {
		try{
			String capAttrib = attributeId.substring(0, 1).toUpperCase() + attributeId.substring(1);
		
			Method getter = view.getClass().getMethod("get" + capAttrib);
			Method setter = null;
			try{
				setter = view.getClass().getMethod("set" + capAttrib, getter.getReturnType());
			}catch(Exception e){}
			
			return new GenericViewAttribute(getter.getReturnType(), view, attributeId, getter, setter);
		}catch(Exception e){
			return null;
		}
    }
}
