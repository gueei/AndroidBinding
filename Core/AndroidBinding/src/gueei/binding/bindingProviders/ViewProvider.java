package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.view.AnimationViewAttribute;
import gueei.binding.viewAttributes.view.AssignViewAttribute;
import gueei.binding.viewAttributes.view.BackgroundColorViewAttribute;
import gueei.binding.viewAttributes.view.BackgroundViewAttribute;
import gueei.binding.viewAttributes.view.ContextMenuViewAttribute;
import gueei.binding.viewAttributes.view.EnabledViewAttribute;
import gueei.binding.viewAttributes.view.Layout_HeightViewAttribute;
import gueei.binding.viewAttributes.view.Layout_WidthViewAttribute;
import gueei.binding.viewAttributes.view.OnBindViewAttribute;
import gueei.binding.viewAttributes.view.OnClickViewEvent;
import gueei.binding.viewAttributes.view.OnGainFocusViewEvent;
import gueei.binding.viewAttributes.view.OnKeyViewEvent;
import gueei.binding.viewAttributes.view.OnLongClickViewEvent;
import gueei.binding.viewAttributes.view.OnLostFocusViewEvent;
import gueei.binding.viewAttributes.view.OnTouchViewEvent;
import gueei.binding.viewAttributes.view.SelectedViewAttribute;
import gueei.binding.viewAttributes.view.VisibilityViewAttribute;
import android.view.View;


public class ViewProvider extends BindingProvider {

	@Override
	public ViewAttribute<View, ?> createAttributeForView(View view, String attributeId) {
		if (attributeId.equals("enabled")){
			return new EnabledViewAttribute(view, "enabled");
		}else if (attributeId.equals("visibility")){
			return new VisibilityViewAttribute(view, "visibility");
		}else if (attributeId.equals("background")){
			return new BackgroundViewAttribute(view);
		}else if (attributeId.equals("backgroundColor")){
			return new BackgroundColorViewAttribute(view);
		}else if (attributeId.equals("contextMenu")){
			return new ContextMenuViewAttribute(view);
		}else if (attributeId.equals("onClick")){
			return new OnClickViewEvent(view);
		}else if (attributeId.equals("onLongClick")){
			return new OnLongClickViewEvent(view);
		}else if (attributeId.equals("animation")){
			return new AnimationViewAttribute(view);
		}else if (attributeId.equals("selected")){
			return new SelectedViewAttribute(view);
		}else if (attributeId.equals("onGainFocus")){
			return new OnGainFocusViewEvent(view);
		}else if (attributeId.equals("onLostFocus")){
			return new OnLostFocusViewEvent(view);
		}else if (attributeId.equals("onKey")){
			return new OnKeyViewEvent(view);
		}else if (attributeId.equals("onTouch")){
			return new OnTouchViewEvent(view);			
		}else if (attributeId.equals("layout_height")){
			return new Layout_HeightViewAttribute(view);
		}else if (attributeId.equals("layout_width")){
			return new Layout_WidthViewAttribute(view);
		}else if (attributeId.equals("onBind")){
			return new OnBindViewAttribute(view);
		}else if (attributeId.equals("assign")){
			return new AssignViewAttribute(view);
		}
		return null;
	}
}
