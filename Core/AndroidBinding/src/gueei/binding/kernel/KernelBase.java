package gueei.binding.kernel;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gueei.binding.AttributeBinder;
import gueei.binding.AttributeCollection;
import gueei.binding.Binder.InflateResult;
import gueei.binding.BindingLog;
import gueei.binding.BindingMap;
import gueei.binding.IBindableView;
import gueei.binding.IKernel;
import gueei.binding.ISyntaxResolver;
import gueei.binding.ViewAttribute;
import gueei.binding.ViewFactory;
import gueei.binding.ViewTag;
import gueei.binding.bindingProviders.AbsSpinnerViewProvider;
import gueei.binding.bindingProviders.AdapterViewProvider;
import gueei.binding.bindingProviders.CompoundButtonProvider;
import gueei.binding.bindingProviders.ExpandableListViewProvider;
import gueei.binding.bindingProviders.ImageViewProvider;
import gueei.binding.bindingProviders.ListViewProvider;
import gueei.binding.bindingProviders.ProgressBarProvider;
import gueei.binding.bindingProviders.RatingBarProvider;
import gueei.binding.bindingProviders.SeekBarProvider;
import gueei.binding.bindingProviders.TabHostProvider;
import gueei.binding.bindingProviders.TextViewProvider;
import gueei.binding.bindingProviders.ViewAnimatorProvider;
import gueei.binding.bindingProviders.ViewProvider;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.listeners.MulticastListenerCollection;
import gueei.binding.listeners.ViewMulticastListener;

public abstract class KernelBase implements IKernel {

	public KernelBase() {
		super();
	}

	@Override
    public ViewAttribute<?, ?> getAttributeForView(View view, String attributeId)
            throws AttributeNotDefinedException {
            	ViewAttribute<?, ?> viewAttribute = null;
            	
            	AttributeCollection collection = getAttributeCollectionOfView(view);
            	if (collection.containsAttribute(attributeId))
            		return collection.getAttribute(attributeId);
            	
            	//  Check if it is custom view, if so, try to look for the attribute in custom view first
            	if (view instanceof IBindableView){
            		viewAttribute = ((IBindableView<?>)view).createViewAttribute(attributeId);
            	}
            	
            	if (viewAttribute==null)
            		viewAttribute = AttributeBinder.getInstance().createAttributeForView(view, attributeId);
            	
            	if (viewAttribute == null) 
            		throw new AttributeNotDefinedException
            			(String.format("The view of type '%s' does not have attribute '%s' defined. ", 
            					view.getClass().getSimpleName(), attributeId));
            	
            	collection.putAttribute(attributeId, viewAttribute);
            	return viewAttribute;
            }

	@Override
    public ViewTag getViewTag(View view) {
    	Object tag = view.getTag();
    	if (tag instanceof ViewTag){
    		return (ViewTag)tag;
    	}
    	ViewTag vtag = new ViewTag();
    	view.setTag(vtag);
    	return vtag;
    }

	@Override
    public AttributeCollection getAttributeCollectionOfView(View view) {
    	ViewTag vt = getViewTag(view);
    	AttributeCollection collection = vt.get(AttributeCollection.class);
    	if (collection!=null)
    		return collection;
    	collection = new AttributeCollection();
    	vt.put(AttributeCollection.class, collection);
    	return collection;
    }

	@Override
    public void putBindingMapToView(View view, BindingMap map) {
    	getViewTag(view).put(BindingMap.class, map);
    }

	@Override
    public BindingMap getBindingMapForView(View view) {
    	if (!getViewTag(view).containsKey(BindingMap.class)){
    		putBindingMapToView(view, new BindingMap());
    	}
    	return getViewTag(view).get(BindingMap.class);
    }

	@Override
    public InflateResult inflateView(Context context, int layoutId, ViewGroup parent,
            boolean attachToRoot) {
            	LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(context);
            	ViewFactory factory = new ViewFactory(inflater);
            	inflater.setFactory(factory);
            	InflateResult result = new InflateResult();
            	result.rootView = inflater.inflate(layoutId, parent, attachToRoot);
            	result.processedViews = factory.getProcessedViews();
            	return result;
            }

	@Override
    public View bindView(Context context, InflateResult inflatedView, Object model) {
    	for(View v: inflatedView.processedViews){
    		AttributeBinder.getInstance().bindView(context, v, model);
    	}
    	return inflatedView.rootView;
    }

	@Override
    public <T extends ViewMulticastListener<?>> T getMulticastListenerForView(
            View view, Class<T> listenerType) {
            	MulticastListenerCollection collection = getViewTag(view).get(MulticastListenerCollection.class);
            	if (collection==null){
            		collection = new MulticastListenerCollection();
            		getViewTag(view).put(MulticastListenerCollection.class, collection);
            	}
            
            	if (collection.containsKey(listenerType)){
            		return collection.get(listenerType);
            	}
            	try {
            		T listener = listenerType.getConstructor().newInstance();
            		listener.registerToView(view);
            		collection.put(listenerType, listener);
            		return listener;
            	} catch (Exception e){
            		BindingLog.exception("BinderV30", e);
            		return null;
            	}
            }

	@Override
    public void init(Application application) {
		syntaxResolver = createSyntaxResolver(application);
		attributeBinder = createAttributeBinder(application);
		registerProviders(attributeBinder);
    }

	protected abstract AttributeBinder createAttributeBinder(Application application);
	protected abstract void registerProviders(AttributeBinder attrBinder);
	protected abstract ISyntaxResolver createSyntaxResolver(Application application);
	
	@Override
    public AttributeBinder getAttributeBinder() {
    	return attributeBinder;
    }
	
	protected AttributeBinder attributeBinder;
	protected ISyntaxResolver syntaxResolver;
	
	@Override
    public ISyntaxResolver getSyntaxResolver() {
		return syntaxResolver;
    }
}