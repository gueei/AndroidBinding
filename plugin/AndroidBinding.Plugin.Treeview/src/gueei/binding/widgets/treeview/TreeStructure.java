package gueei.binding.widgets.treeview;

import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;

public class TreeStructure {
	public IObservable<?> root;
	public IObservable<?> childrenObservableName;
	public IObservable<?> isExpandedObservableName;
	
	public Layout template;	
	public IObservable<?> templateIdObservableName;
	
	public Layout wrapperTemplate;		
	public IObservable<?> wrapperTemplateId;

	public IObservable<?> imgExpanded;
	public IObservable<?> imgCollapsed;
	public IObservable<?> spacerWidth;
	
	public IObservable<?> treeNodeClicked;	
	public IObservable<?> treeNodeLongClicked;	
}
