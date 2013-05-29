package gueei.binding.widgets.treeview;

import gueei.binding.widgets.TreeViewList;

public class TreeNodeLongClickEvent {
	public final int rowPosition;
	public final Object treeNode;
	private TreeViewItemWrapper wrapper;
	private TreeViewList parent;
	
	public TreeNodeLongClickEvent(int rowPosition, TreeViewItemWrapper wrapper, TreeViewList parent) {
		this.rowPosition = rowPosition;
		this.wrapper = wrapper;
		this.treeNode = wrapper.WrapperNodeDataSource.get();	
		this.parent = parent;
	}
	
	public Object [] getNodePath() {
		return parent.getPath(wrapper);
	}
}
