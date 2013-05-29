package gueei.binding.widgets.breadcrumbs;

import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;

public class BreadCrumbsStructure {
	public IObservable<?> root;
	public IObservable<?> menuTextName;
	public IObservable<?> childrenObservableName;	
	public IObservable<?> selectedPositionObservableName;
	
	public Layout template;	
	public IObservable<?> templateIdObservableName;
	
	public Layout wrapperTemplate;		
	public IObservable<?> wrapperTemplateId;

	public IObservable<?> breadCrumbClicked;	
	public IObservable<?> breadCrumbLongClicked;	
	public IObservable<?> breadCrumbSelected;
}
    