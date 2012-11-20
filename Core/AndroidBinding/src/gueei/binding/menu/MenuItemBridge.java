package gueei.binding.menu;

import gueei.binding.Command;
import gueei.binding.IObservable;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

/**
 * Mock menu item act as bridging bindable attributes
 * with the real menu item
 * Will not keep reference to real menu item since
 * menu item might suddenly detached
 * @author andy
 *
 */
public class MenuItemBridge extends AbsMenuBridge{

	private Command onClickCommand;
	
	private IObservable<?> title, visible, enabled, checked, icon;
		
	public void onCreateOptionItem(Menu menu){
		
	}
	
	public void onPrepareOptionItem(Menu menu){
		MenuItem item = menu.findItem(mId);
		if (item==null) return;
		if (title!=null){
			Object titleObj = title.get();
			if(titleObj!=null){
				// Title don't allow HTML Formatting
				item.setTitle(titleObj.toString());
			}else{
				item.setTitle("");
			}
		}
		if (visible!=null){
			item.setVisible(Boolean.TRUE.equals(visible.get()));
		}
		if (enabled!=null){
			item.setEnabled(Boolean.TRUE.equals(enabled.get()));
		}
		if (checked!=null){
			item.setChecked(Boolean.TRUE.equals(checked.get()));
		}
		if (icon!=null){
			Object iconObj = icon.get();
			if (iconObj!=null){
				if (iconObj instanceof Integer)
					item.setIcon((Integer)iconObj);
				else if (iconObj instanceof Drawable)
					item.setIcon((Drawable)iconObj);
			}else{
				item.setIcon(null);
			}
		}
	}
	
	public MenuItemBridge(int id, AttributeSet attributes,
			Context context, Object model){
		this(id, attributes, context, model, null);
	}
	
	public MenuItemBridge(int id, AttributeSet attributes,
			Context context, Object model, IMenuItemChangedCallback callback) {
		super(id);
		IObservable<?> temp = getObservableFromStatement(context, attributes, "onClick", model, callback);
		if ((temp!=null)&&(temp.get() instanceof Command)){
			onClickCommand = (Command)temp.get();
		}
		temp = getObservableFromStatement(context, attributes, "title", model, callback);
		if ((temp!=null)){
			title = temp;
		}
		temp = getObservableFromStatement(context, attributes, "visible", model, callback);
		if ((temp!=null)){
			visible = temp;
		}
		temp = getObservableFromStatement(context, attributes, "enabled", model, callback);
		if ((temp!=null)){
			enabled = temp;
		}
		temp = getObservableFromStatement(context, attributes, "checked", model, callback);
		if ((temp!=null)){
			checked = temp;
		}
		temp = getObservableFromStatement(context, attributes, "icon", model, callback);
		if ((temp!=null)){
			icon = temp;
		}
	}
	
	public MenuItemBridge(int id, MenuItemViemodel model) {
		super(id);
		if(model == null)
			return;
		IObservable<?> temp = model.onClick;
		if ((temp!=null)&&(temp.get() instanceof Command)){
			onClickCommand = (Command)temp.get();
		}
		temp = model.title;
		if ((temp!=null)){
			title = temp;
		}
		temp = model.visible;
		if ((temp!=null)){
			visible = temp;
		}
		temp = model.enabled;
		if ((temp!=null)){
			enabled = temp;
		}
		temp = model.checked;
		if ((temp!=null)){
			checked = temp;
		}
		temp = model.icon;
		if ((temp!=null)){
			icon = temp;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean output = false;
		if (onClickCommand!=null){
			if(item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo) {
			    AdapterView.AdapterContextMenuInfo cmi = 
				        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo (); 
			    int position = cmi.position;			
			    onClickCommand.InvokeCommand(null, item, position);
			} else {
				onClickCommand.InvokeCommand(null, item);
			}
			output = true;
		}

		if (checked!=null){
			if (Boolean.class.isAssignableFrom(checked.getType()))
				((IObservable<Boolean>)checked).set(!item.isChecked());
		}
		return output;
	}
	
	
}
