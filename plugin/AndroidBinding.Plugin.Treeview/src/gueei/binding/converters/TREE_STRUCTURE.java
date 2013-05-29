package gueei.binding.converters;

import java.net.BindException;

import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.widgets.treeview.TreeStructure;

public class TREE_STRUCTURE extends Converter<TreeStructure> {
	public TREE_STRUCTURE(IObservable<?>[] dependents) {
		super(TreeStructure.class, dependents);
	}

	@Override
	public TreeStructure calculateValue(Object... args) throws Exception {

		try{
			DynamicObject object = (DynamicObject)args[0];
			
			TreeStructure config = new TreeStructure();
			
			if (!object.observableExists("root")) return null;
			if (!object.observableExists("children")) return null;
			if (!object.observableExists("isExpanded")) return null;
			
			config.root = object.getObservableByName("root");
			config.childrenObservableName = object.getObservableByName("children");
			config.isExpandedObservableName = object.getObservableByName("isExpanded");
			
			if (object.observableExists("template"))
				config.template = ((Layout)object.getObservableByName("template").get());
			
			if (object.observableExists("templateId"))
				config.templateIdObservableName = object.getObservableByName("templateId");

			if(config.template == null && config.templateIdObservableName == null)
				throw new BindException("no template or templateId");			
			
			if (object.observableExists("wrapperTemplate"))
				config.wrapperTemplate = ((Layout)object.getObservableByName("wrapperTemplate").get());
			
			if (object.observableExists("wrapperTemplateId"))
				config.wrapperTemplateId = object.getObservableByName("wrapperTemplateId");

			if(config.wrapperTemplate == null && config.wrapperTemplateId == null)
				throw new BindException("no wrapperTemplate or wrapperTemplateId");
											
			if (object.observableExists("imgExpanded"))
				config.imgExpanded = object.getObservableByName("imgExpanded");
			
			if (object.observableExists("imgCollapsed"))
				config.imgCollapsed = object.getObservableByName("imgCollapsed");		

			if (object.observableExists("spacerWidth"))
				config.spacerWidth = object.getObservableByName("spacerWidth");							
			
			if (object.observableExists("treeNodeClicked"))
				config.treeNodeClicked = object.getObservableByName("treeNodeClicked");
			
			if (object.observableExists("treeNodeLongClicked"))
				config.treeNodeLongClicked = object.getObservableByName("treeNodeLongClicked");
						
			return config;
		}catch(Exception e){
			return null;
		}
		
	}
}
