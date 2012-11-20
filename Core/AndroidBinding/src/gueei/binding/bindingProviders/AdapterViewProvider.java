package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.adapterView.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;


public class AdapterViewProvider extends BindingProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(
			View view, String attributeId) {
		if (!(view instanceof AdapterView))
			return null;
		try {
			if (attributeId.equals("adapter")) {
				return (ViewAttribute<Tv, ?>) new AdapterViewAttribute((AdapterView)view);
			} else if (attributeId.equals("selectedItem")) {
				return (ViewAttribute<Tv, ?>) new SelectedItemViewAttribute((AdapterView)view, "selectedItem");
			} else if (attributeId.equals("selectedPosition")) {
				return (ViewAttribute<Tv, ?>) new SelectedPositionViewAttribute((AdapterView)view);
			} else if (attributeId.equals("selectedObject")) {
				return (ViewAttribute<Tv, ?>) new SelectedObjectViewAttribute((AdapterView)view);								
			} else if (attributeId.equals("clickedItem")){
				ViewAttribute<AdapterView<?>, Object> attr = 
					new ClickedItemViewAttribute((AdapterView)view, "clickedItem");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("longClickedItem")){
				return (ViewAttribute<Tv, ?>) new LongClickedItemViewAttribute((AdapterView)view, "longClickedItem");
			} else if (attributeId.equals("clickedId")){
				ViewAttribute<AdapterView<?>, Long> attr = 
					new ClickedIdViewAttribute((AdapterView)view, "clickedId");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("clickedChild")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ClickedChildViewAttribute((ExpandableListView)view);
			} else if (attributeId.equals("itemSource")){
				if (view instanceof ExpandableListView){
					return (ViewAttribute<Tv, ?>)
						new ExpandableListView_ItemSourceViewAttribute((ExpandableListView)view);
				}
				ItemSourceViewAttribute attr = 
					new ItemSourceViewAttribute((AdapterView)view, "itemSource");
				return (ViewAttribute<Tv, ?>) attr;
			} else if (attributeId.equals("itemTemplate")){
				return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "itemTemplate");
			} else if (attributeId.equals("itemCount")){
				return (ViewAttribute<Tv, ?>)new ItemCountViewAttribute((AdapterView)view);
			} else if (attributeId.equals("spinnerTemplate")){
				return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "spinnerTemplate");
			} else if (attributeId.equals("onItemSelected")){
				return (ViewAttribute<Tv, ?>)new OnItemSelectedViewEvent((AdapterView)view);
			} else if (attributeId.equals("onItemClicked")){
				return (ViewAttribute<Tv, ?>)new OnItemClickedViewEvent((AdapterView)view);
			}else if (attributeId.equals("onItemLongClicked")){
				return (ViewAttribute<Tv, ?>)new OnItemLongClickedViewEvent((AdapterView)view);
			} else if (attributeId.equals("childItemTemplate")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ItemTemplateViewAttribute(view, "childItemTemplate");
			} else if (attributeId.equals("childItemSource")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ChildItemSourceViewAttribute((ExpandableListView)view);
			} else if (attributeId.equals("onChildClick")){
				if (view instanceof ExpandableListView)
					return (ViewAttribute<Tv, ?>)new ExpandableListView_OnChildClickViewEvent((ExpandableListView)view);
			} else if (attributeId.equals("clickActionPosition")) {
				return (ViewAttribute<Tv, ?>) new ClickActionPositionViewAttribute((AdapterView) view);
			}
		} catch (Exception e) {
			// Actually it should never reach this statement
		}
		return null;
	}
}
