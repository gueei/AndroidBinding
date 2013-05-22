package gueei.binding.treeviewdemo.viewModels;

import java.util.Date;

import android.app.Activity;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import gueei.binding.treeviewdemo.R;
import gueei.binding.widgets.treeview.TreeNodeClickEvent;
import gueei.binding.widgets.treeview.TreeNodeLongClickEvent;

public class TreeViewListViewModel {
	
	public final IntegerObservable WrapperTemplateLayoutId = new IntegerObservable(R.layout.tree_list_item_wrapper);
	public final Observable<TreeNode> EnsureVisibleItem = new Observable<TreeNode>(TreeNode.class);	

	public static class TreeNode {
		public final BooleanObservable IsExpanded = new BooleanObservable(false);
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.tree_item);
		public final StringObservable Text = new StringObservable();
		public final ArrayListObservable<TreeNode> Children = new ArrayListObservable<TreeNode>(TreeNode.class);
		
		public String toString() {
			return Text.get() + " cp : " + Children;
		}
	}	
	
	public final ArrayListObservable<TreeNode> Items = new ArrayListObservable<TreeNode>(TreeNode.class);
		
	public TreeViewListViewModel(Activity activity){
		buildDemoData();		
	}
	
	public final Command OnTreeNodeClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {	
			if( args.length != 1)
				return;

			TreeNodeClickEvent e = (TreeNodeClickEvent)args[0];
			if(e==null)
				return;
			
			TreeNode treeNode = (TreeNode)e.treeNode;
			if(treeNode==null)
				return;
/*			
			// treenode 1-1 can't be expand or collapsed by the user
			String name = "TreeNode " + "(" + 0 + " - " + 1 + ")";
			if( treeNode.Text.get().equals(name) )
				e.ignoreExpandCollapse=true;
*/				
		}
	};
	
	public final Command OnTreeNodeLongClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			if( args.length != 1)
				return;
			
			TreeNodeLongClickEvent e = (TreeNodeLongClickEvent)args[0];
			if(e==null)
				return;
		}
	};
	
	public final Command OnDebugInsert = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			TreeNode t= new TreeNode();
			t.Text.set("new node " + (new Date()).getTime());
			//Items.get(0).Children.add(2,t);			
			//Items.get(1).Children.get(1).Children.get(1).Children.add(2,t);
			//Items.add(1,t);
			Items.add(9,t);
		}
	};
	
	public final Command OnDebugRemove = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			//Items.get(0).Children.remove(1);
			//Items.get(0).Children.remove(0);
			Items.remove(1);
		}
	};
	
	public final Command OnDebugClear = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			//Items.get(0).Children.get(1).Children.clear();
			Items.get(0).Children.get(0).Children.clear();
		}
	};	
	
	public final Command OnDebugClearRoot = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			Items.clear();
		}
	};
	
	public final Command OnDebugReplace= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			TreeNode t= new TreeNode();
			t.Text.set("replace node " + (new Date()).getTime());
			//Items.replaceItem(1, t);
			Items.get(0).Children.get(1).Children.replaceItem(2, t);
		}
	};
	
	public final Command OnDebugSwapLayouts= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			
			swapLayoutsRecursive(Items);
		}
	};
	
	public final Command OnDebugChangeWrapperTemplate= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			// TODO;
			// this is currently buggy and sets the tree to an unstabled state			
			// e.g. inserts after a layout change are duplicated
			// we have to bind the wrapperlayoutid to an internal element inside the treevielist
			// and drop the whole tree and rebuild()
			
			if(WrapperTemplateLayoutId.get() == R.layout.tree_list_item_wrapper)
				WrapperTemplateLayoutId.set(R.layout.tree_list_item_wrapper_blue);
			else
				WrapperTemplateLayoutId.set(R.layout.tree_list_item_wrapper);
		}
	};	
	
	public final Command OnDebugExpandCollapse= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {			
			boolean expanded = true;
			if(Items.size() > 0 && Items.get(0).IsExpanded.get())
				expanded = false;
			
			ArrayListObservable<TreeNode> list = Items;
			while(list != null && list.size() > 0) {
				list.get(0).IsExpanded.set(expanded);
				list = list.get(0).Children;
			}
		}
	};	
	
	public final Command OnDebugRebuildTree= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {			
			buildDemoData();
		}		
	};
	
	public final Command OnDebugEnsureVisible= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {			
			EnsureVisibleItem.set(Items.get(2));
		}		
	};		
	
	private void buildDemoData() {		
		Items.clear();
		for (int i = 0; i < 3; i++) {
			TreeNode p1 = new TreeNode();
			if( i % 2 == 0)
				p1.LayoutId.set(R.layout.tree_item2);			
			p1.Text.set( "TreeNode " + "(" + i + ")");			
			Items.add(p1);				
			
			for (int k = 0; k < 3; k++) {
				TreeNode p2 = new TreeNode();
				if( k % 2 == 0)
					p2.LayoutId.set(R.layout.tree_item2);
				p2.Text.set( "TreeNode " + "(" + i + " - " + k + ")");			
				p1.Children.add(p2);
				
				for (int m = 0; m < 3; m++) {
					TreeNode p3 = new TreeNode();
					if( m % 2 == 0)
						p3.LayoutId.set(R.layout.tree_item2);
					p3.Text.set( "TreeNode " + "(" + i + " - " + k + " - " + m + ")");
					p2.Children.add(p3);
				}
			}
		}
		
		Items.get(0).IsExpanded.set(true);
		Items.get(0).Children.get(1).IsExpanded.set(true);
		Items.get(2).Children.get(2).IsExpanded.set(true);
	}

	protected void swapLayoutsRecursive(ArrayListObservable<TreeNode> items) {	
		for( TreeNode n: items) {
			if(n.LayoutId != null && n.LayoutId.get() != null){
				if(n.LayoutId.get() == R.layout.tree_item)
					n.LayoutId.set(R.layout.tree_item2);
				else
					n.LayoutId.set(R.layout.tree_item);
			}
			swapLayoutsRecursive(n.Children);
		}
		
	}
	
	
}
