package gueei.binding.labs.viewAttributes.treeView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.widgets.TreeViewList;
import gueei.binding.widgets.treeview.TreeStructure;

public class TreeStructureViewAttribute extends ViewAttribute<TreeViewList, TreeStructure> {
			
		public  TreeStructureViewAttribute 
			(TreeViewList view) {
			super(TreeStructure.class,view, "treeStructure");
		}
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if(getView()==null) return;
			if (newValue instanceof TreeStructure){
				getView().setTreeStructure((TreeStructure)newValue);
			}
		}

		@Override
		public TreeStructure get() {
			return null;
		}
		
		@Override
        protected BindingType AcceptThisTypeAs(Class<?> type) {
			return BindingType.OneWay;
        }
}
