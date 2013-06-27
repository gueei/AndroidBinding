package gueei.binding.breadcrumbsdemo.viewModels;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.breadcrumbsdemo.R;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsClickEvent;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsLongClickEvent;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsSelectedEvent;

public class BreadCrumbsViewModel {
		
	public final StringObservable SelectedNodeText = new StringObservable("select a node");
	public final StringObservable SelectedNodeOldPathText = new StringObservable("-");
	public final StringObservable SelectedNodeNewPathText = new StringObservable("-");
	
	public final IntegerObservable WrapperTemplateLayoutId = new IntegerObservable(R.layout.bread_crumb_item_wrapper);
	
	public static class BreadCrumbNode {
		public final ArrayListObservable<BreadCrumbNode> Children = new ArrayListObservable<BreadCrumbNode>(BreadCrumbNode.class);
		public final IntegerObservable SelectedPosition = new IntegerObservable();		
		public final IntegerObservable LayoutId = new IntegerObservable(R.layout.bread_crumb_item);			
		public final StringObservable Text = new StringObservable();
		

		public String toString() {
			return Text.get() + " cp : " + Children;
		}
	}	
			
	public final Observable<BreadCrumbNode> Root = new Observable<BreadCrumbNode>(BreadCrumbNode.class);
	
	public final Command OnBreadCrumbClicked = new Command() {			
		@Override
		public void Invoke(View view, Object... args) {
			if( args.length < 1)
				return;

			BreadCrumbsClickEvent e = (BreadCrumbsClickEvent)args[0];
			if(e==null)
				return;
			
			BreadCrumbNode breadCrumb = (BreadCrumbNode)e.breadCrumbNode;
			if(breadCrumb==null)
				return;
/*			
			// can't be expand or collapsed by the user
			String name = "Breadcrumb level: 4 - child element : 0";
			if( breadCrumb.Text.get().equals(name) )
				e.eventHandled=true;
*/		
		}
	};

	public final Command OnBreadCrumbLongClicked = new Command() {			
		@Override
		public void Invoke(View view, Object... args) {
			if( args.length < 1)
				return;
			
			BreadCrumbsLongClickEvent e = (BreadCrumbsLongClickEvent)args[0];
			if(e==null)
				return;	
		}
	};
	
	public final Command OnBreadCrumbSelected = new Command() {			
		@Override
		public void Invoke(View view, Object... args) {
			if( args.length < 1)
				return;
			
			BreadCrumbsSelectedEvent e = (BreadCrumbsSelectedEvent)args[0];
			if(e==null)
				return;	
					
			BreadCrumbNode breadCrumb = (BreadCrumbNode)e.breadCrumbNodeNew;
			if(breadCrumb!=null)			
				SelectedNodeText.set("New element:" + breadCrumb.Text.get());
			else
				SelectedNodeText.set("none selected");
			
			List<Object> path = e.getOldFullPath();
			if(path==null){		
				SelectedNodeOldPathText.set("no path");				
			} else {
				StringBuilder sb = new StringBuilder();
				
				sb.append("Path before selection: ");
				for(int i=0;i<path.size();i++) {
					BreadCrumbNode n = (BreadCrumbNode)path.get(i);
					if(n == null )
						continue;
					sb.append(n.Text.get());
					sb.append(" / ");
				}
				SelectedNodeOldPathText.set(sb.toString());
			}
			
			path = e.getNewFullPath();
			if(path==null){		
				SelectedNodeNewPathText.set("no path");				
			} else {
				StringBuilder sb = new StringBuilder();
				
				sb.append("Path after selection: ");
				for(int i=0;i<path.size();i++) {
					BreadCrumbNode n = (BreadCrumbNode)path.get(i);
					if(n == null )
						continue;
					sb.append(n.Text.get());
					sb.append(" / ");
				}
				SelectedNodeNewPathText.set(sb.toString());
			}
		}
	};		
	
	public final Command OnDebugInsert = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			BreadCrumbNode b= new BreadCrumbNode();
			b.Text.set("new node " + (new Date()).getTime());			
			//Root.get().Children.add(2,b);			
			//Root.get().Children.get(1).Children.get(1).Children.add(2,b);
			//Root.get().Children.add(b);		
			Root.get().Children.get(0).Children.get(0).Children.get(0).Children.get(0).Children.add(2,b);
		}
	};	
	
	public final Command OnDebugRemove = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			//Root.get().Children.get(0).Children.get(0).Children.remove(1);
			Root.get().Children.get(0).Children.get(0).Children.remove(0);
			//Root.get().Children.remove(1);
			//Root.get().Children.remove(0);
		}
	};
	
	
	public final Command OnDebugClear = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			//Root.get().Children.get(1).Children.clear();
			Root.get().Children.get(0).Children.clear();
			//Root.get().Children.get(0).Children.get(0).Children.clear();
		}
	};	
	
	public final Command OnDebugClearRoot = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			//Root.set(null);
			Root.get().Children.clear();
		}
	};
	
	public final Command OnDebugReplace= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			BreadCrumbNode b= new BreadCrumbNode();
			b.Text.set("replace node " + (new Date()).getTime());	
			//Root.get().Children.replaceItem(0, b);
			Root.get().Children.replaceItem(1, b);
			//Root.get().Children.get(1).Children.replaceItem(2, b);
		}
	};	
	
	public final Command OnDebugSwapLayouts= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			// TODO: this is currently buggy in the devsmartlib - we have to investigate this
			swapLayoutsRecursive(Root.get());
		}
	};
	
	public final Command OnDebugChangeWrapperTemplate= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			if(WrapperTemplateLayoutId.get() == R.layout.bread_crumb_item_wrapper)
				WrapperTemplateLayoutId.set(R.layout.bread_crumb_item_wrapper_blue);
			else
				WrapperTemplateLayoutId.set(R.layout.bread_crumb_item_wrapper);
		}
	};		
		
	public final Command OnDebugSetSelectedPositon= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {			
			Root.get().SelectedPosition.set(1);
		}
	};			
	
	public final Command OnDebugRebuildTree= new Command() {		
		@Override
		public void Invoke(View view, Object... args) {			
			buildDemoData("Rebuild");
		}
	};		
	
	
	public BreadCrumbsViewModel(Activity activity){
		buildDemoData("BC");		
	}		
	
	private void buildDemoData(String name) {		
		
		Root.set(new BreadCrumbNode());
		Root.get().Text.set("root");
		
		id = 0;
		insertDemoDataRec(name, Root.get(), 3);

		BreadCrumbNode r = Root.get();
		for(;;) {
			if(r==null)
				break;
			
			if(r.Children == null || r.Children.size() < 1) {
				r = null;
			} else {
				r.SelectedPosition.set(0);
				r = r.Children.get(0);
			}
		}
	}

	int id=0;
	private void insertDemoDataRec(String name, BreadCrumbNode root, int level) {
		if(level<0)
			return;
		
		for (int i = 0; i < 3; i++) {		
			id++;
			BreadCrumbNode n = new BreadCrumbNode();
			if( i % 2 == 0)
				n.LayoutId.set(R.layout.bread_crumb_item2);
			int c = level;
			n.Text.set( "id: " + id + " " + name + " level: " + c + " - child : " + i );						
			root.Children.add(n);
			insertDemoDataRec(name, n, level-1);
		}
	}
	
	protected void swapLayoutsRecursive(BreadCrumbNode root) {	
		for( BreadCrumbNode b: root.Children ) {
			if(b.LayoutId != null && b.LayoutId.get() != null){
				if(b.LayoutId.get() == R.layout.bread_crumb_item)
					b.LayoutId.set(R.layout.bread_crumb_item2);
				else
					b.LayoutId.set(R.layout.bread_crumb_item);
			}
			swapLayoutsRecursive(b);
		}
		
	}
	
}
