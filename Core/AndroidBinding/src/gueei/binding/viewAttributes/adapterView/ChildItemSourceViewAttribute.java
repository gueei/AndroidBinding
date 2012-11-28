package gueei.binding.viewAttributes.adapterView;

import android.widget.ExpandableListView;
import gueei.binding.ViewAttribute;

/**
 * Item Source for Child items in Expandable List View. 
 * Note that it must be in String (quoted) format. 
 * For example:
 * ItemVM : RowModel
 * + ArrayList<ChildItem> SubItems
 * 
 * then in XML:
 * binding:childItemSource="'SubItems'"
 * 
 * @name childItemSource
 * @widget ExpandableListView
 * @type String
 * @accepts	Object evaluated with toString()
 * @category expandable-list
 * @related 
 * 
 * @author andy
 */
public class ChildItemSourceViewAttribute extends ViewAttribute<ExpandableListView, String> {

	public ChildItemSourceViewAttribute(ExpandableListView view) {
		super(String.class, view, "childItemSource");
	}

	private String mValue;
	
	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue!=null)
			mValue = newValue.toString();
	}

	@Override
	public String get() {
		return mValue;
	}

}
