package gueei.binding.labs.viewAttributes.treeView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.widgets.TreeViewList;

public class TreeEnsureVisibleNodeViewAttribute extends ViewAttribute<TreeViewList, Object> {
			
		public  TreeEnsureVisibleNodeViewAttribute 
			(TreeViewList view) {
			super(Object.class,view, "ensureVisibleNode");
		}
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if(getView()==null) return;
			getView().setEnsureVisible(newValue);
		}

		@Override
		public Object get() {
			return null;
		}
		
		@Override
        protected BindingType AcceptThisTypeAs(Class<?> type) {
			return BindingType.OneWay;
        }
}
