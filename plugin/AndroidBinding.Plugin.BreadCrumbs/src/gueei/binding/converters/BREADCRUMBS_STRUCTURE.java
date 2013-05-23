package gueei.binding.converters;

import java.net.BindException;

import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsStructure;

public class BREADCRUMBS_STRUCTURE extends Converter<BreadCrumbsStructure> {
	public BREADCRUMBS_STRUCTURE(IObservable<?>[] dependents) {
		super(BreadCrumbsStructure.class, dependents);
	}
	
	@Override
	public BreadCrumbsStructure calculateValue(Object... args) throws Exception {

		try{
			DynamicObject object = (DynamicObject)args[0];
			
			BreadCrumbsStructure config = new BreadCrumbsStructure();
			
			if (!object.observableExists("root")) return null;
			if (!object.observableExists("children")) return null;
			if (!object.observableExists("menuText")) return null;
			if (!object.observableExists("selectedPosition")) return null;			
			
			config.root = object.getObservableByName("root");
			config.childrenObservableName = object.getObservableByName("children");
			config.menuTextName = object.getObservableByName("menuText");
			config.selectedPositionObservableName = object.getObservableByName("selectedPosition");
			
			
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

			if (object.observableExists("breadCrumbClicked"))
				config.breadCrumbClicked = object.getObservableByName("breadCrumbClicked");
			
			if (object.observableExists("breadCrumbLongClicked"))
				config.breadCrumbLongClicked = object.getObservableByName("breadCrumbLongClicked");
			
			if (object.observableExists("breadCrumbSelected"))
				config.breadCrumbSelected = object.getObservableByName("breadCrumbSelected");			
						
			return config;
		}catch(Exception e){
			return null;
		}
		
	}
}
