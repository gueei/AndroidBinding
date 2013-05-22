package gueei.binding.widgets;

import java.util.ArrayList;
import java.util.Collection;

import gueei.binding.AttributeBinder;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.widgets.treeview.TreeNodeClickEvent;
import gueei.binding.widgets.treeview.TreeNodeLongClickEvent;
import gueei.binding.widgets.treeview.TreeStructure;
import gueei.binding.widgets.treeview.TreeViewItemWrapper;
import gueei.binding.widgets.treeview.Utility;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TreeViewList extends ListView {
	
	public static final int DEFAULT_SPACER_WIDTH = 40;
	
	private static boolean smoothScrollEnsureVisible = false;
	
	private TreeStructure treeStructure;
	private boolean basicSetupDone;
	private int spacerWidth = DEFAULT_SPACER_WIDTH;
	
	private Command OnTreeNodeClickedRef;
	private Command OnTreeNodeLongClickedRef;

	public final ArrayListObservable<TreeViewItemWrapper> itemSource = new ArrayListObservable<TreeViewItemWrapper>(TreeViewItemWrapper.class);
		
	private Observer observerSpacerWidth = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			// TODO: implement this
		}
	};
	
	private CollectionObserver collectionObserver = new CollectionObserver(){
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};		
	
	public final Command OnItemClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {		
			Integer pos = Utility.getClickedListPositon(itemSource, args);
			if(pos == null)
				return;
			
			if(OnTreeNodeClickedRef != null) {
				TreeViewItemWrapper w = itemSource.get(pos);
				TreeNodeClickEvent e = new TreeNodeClickEvent(pos, w, TreeViewList.this);
						
				OnTreeNodeClickedRef.Invoke(TreeViewList.this, e);
				if( e.ignoreExpandCollapse)
					return;
			}
			
			handleExpandCollapse(pos, true);
		}
	};
	
	public final Command OnItemLongClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			Integer pos = Utility.getClickedListPositon(itemSource, args);
			if(pos == null || OnTreeNodeLongClickedRef == null)
				return;
			
			TreeViewItemWrapper w = itemSource.get(pos);				
			TreeNodeLongClickEvent e = new TreeNodeLongClickEvent(pos, w, TreeViewList.this);		
			
			OnTreeNodeLongClickedRef.Invoke(TreeViewList.this, e);
		}
	};			
	
	public TreeViewList(Context context) {
		super(context);
	}	

	public Object[] getPath(TreeViewItemWrapper leaf) {
		if(leaf==null)
			return null;
		
		ArrayList<Object> pathList = new ArrayList<Object>();
		
		TreeViewItemWrapper w = leaf;
		int level = w.level;
		
		int pos = itemSource.indexOf(w);
		for(int i=pos; i > -1; i--) {
			w = itemSource.getItem(i);
			if( w.level < level ) {
				pathList.add(0, w.WrapperNodeDataSource.get());
				level = w.level;
			}
		}
		
		return pathList.toArray();
	}

	public TreeViewList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TreeViewList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);	
	}	
	
	public void setTreeStructure(TreeStructure value) {
		synchronized (this) {
			if( treeStructure != null ) {
				if( treeStructure.spacerWidth != null )
					treeStructure.spacerWidth.unsubscribe(observerSpacerWidth);
			}
			
			treeStructure = value;
			
			if( treeStructure != null ) {
				if( treeStructure.spacerWidth != null )
					treeStructure.spacerWidth.subscribe(observerSpacerWidth);
			}
			
			basicSetup();
			rebind();
		}
	}
	
	public void expandCollapseFromDataSource(Object newState, TreeViewItemWrapper node) {
		if(node==null)
			return;
		
		if(node.expandedState != null && node.expandedState.equals(newState))
			return;
		
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w = itemSource.get(i);
			if(w.equals(node)){
				handleExpandCollapse(i, false);
				break;
			}
		}
	}
	
	public static void setSmoothScrollEnsureVisible(boolean value) {
		smoothScrollEnsureVisible = value;
	}
	
	private void basicSetup() {
		if(basicSetupDone)
			return;
		
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "onItemClicked", ".", OnItemClicked);
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "onItemLongClicked", ".", OnItemLongClicked);
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "itemSource", ".", itemSource);
		
		basicSetupDone = true;
	}
	
	private void rebind() {
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w = itemSource.get(i);
			w.detach();
		}
		itemSource.clear();
		
		if(treeStructure == null)
			return;
		
		if(treeStructure.wrapperTemplate!=null)
			AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "itemTemplate", ".", treeStructure.wrapperTemplate.getDefaultLayoutId());
		if(treeStructure.wrapperTemplateId!=null && treeStructure.wrapperTemplateId.get() instanceof Integer)
			AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "itemTemplate", ".", treeStructure.wrapperTemplateId.get());
		
		rebindTreeNodeCommands();						
		setSpacerWidth();
		
		IObservableCollection<?> collection = (IObservableCollection<?>) treeStructure.root.get();
		collection.subscribe(collectionObserver);
		
		int pos =0;
		for( int i=0; i < collection.size(); i++) {
			pos += insertNode(collection, collection.getItem(i), pos, 0);		
		}
	}
	
	private void rebindTreeNodeCommands() {
		OnTreeNodeClickedRef = null;
		OnTreeNodeLongClickedRef = null;		
		
		if(treeStructure == null)
			return;
		
		if(treeStructure.treeNodeClicked != null) {
			if( treeStructure.treeNodeClicked.get() instanceof Command)
				OnTreeNodeClickedRef = (Command)treeStructure.treeNodeClicked.get();
		}
		
		if(treeStructure.treeNodeLongClicked != null) {
			if( treeStructure.treeNodeLongClicked.get() instanceof Command)
				OnTreeNodeLongClickedRef = (Command)treeStructure.treeNodeLongClicked.get();
		}		
	}
	
	private void setSpacerWidth() {
		spacerWidth = DEFAULT_SPACER_WIDTH;
		
		if(treeStructure != null && treeStructure.spacerWidth != null) {
			try {
				Integer width = Integer.parseInt(treeStructure.spacerWidth.get().toString().trim());
				spacerWidth = width;
			} catch(Exception e) { }
		}
	}
	
	private IObservable<?> getDSExpanded(Object obj) {
		if(obj == null || treeStructure == null || treeStructure.isExpandedObservableName == null )
			return null;
		
		IObservable<?> value = Utility.getObservableByFieldPath(obj, treeStructure.isExpandedObservableName.get().toString());
		return value;
	}
	
	private IObservableCollection<?> getDSChildren(Object obj) {
		IObservable<?> childrenField = Utility.getObservableByFieldPath(obj, treeStructure.childrenObservableName.get().toString());
		if( childrenField == null || !(childrenField.get() instanceof IObservableCollection<?>))
			return null;
				
		IObservableCollection<?> children = (IObservableCollection<?>)childrenField.get();
		return children;
	}
	
	private IObservable<?> geDSLayoutIdObservable(Object obj) {
		if(treeStructure.templateIdObservableName == null )
			return null;
		
		IObservable<?> templateIdField = Utility.getObservableByFieldPath(obj, treeStructure.templateIdObservableName.get().toString());
		if( templateIdField != null && (templateIdField.get() instanceof Integer))
			return templateIdField;
				
		return null;
	}		
	
	private int insertNodesFromCollection(TreeViewItemWrapper w, IObservableCollection<?> root) {
		int count=0;
		if(root == null)
			return count;
		
		int from = itemSource.indexOf(w);
		if( from< 0)
			return count;			
		
		from++;
		for( int i=0; i < root.size(); i++) {
			int c = insertNode(root, root.getItem(i), from, w.level+1);
			count += c;
			from += c;			
		}
		
		return count;		
	}	
	
	private int insertNode(IObservableCollection<?> root, Object item, int pos, int level) {
		int count = 1;
		
		TreeViewItemWrapper w = new TreeViewItemWrapper(this);		
		w.WrapperImage.set(null);
		w.WrapperSpace.set(spacerWidth * (level+1));
		
		if(treeStructure.template != null)
			w.WrapperNodeLayoutId.set(treeStructure.template.getDefaultLayoutId());
		
		// this is an optional datasource based layout		
		IObservable<?> layoutIdObservable = geDSLayoutIdObservable(item);
		if(layoutIdObservable!=null) {
			w.setLayoutIdObservable(layoutIdObservable);
		}
		
		w.WrapperNodeDataSource.set(item);		
		w.setIsExpandedObservable(getDSExpanded(item));		
		w.level = level;
		itemSource.add(pos, w);
		
		w.children = getDSChildren(item);		
		
		if( w.children != null )
			w.children.subscribe(collectionObserver);
		
		if( w.children != null && w.children.size() > 0) {			
			if(w.isExpanded() != null && w.isExpanded() == true) {
				w.WrapperImage.set(treeStructure.imgExpanded.get());		
				
				int childCount = insertNodesFromCollection(w, w.children);
				count += childCount;
			} else {
				w.WrapperImage.set(treeStructure.imgCollapsed.get());
			}
		}			
		
		return count;
	}
	
	private void removeItemRecursiveFromCollection(IObservableCollection<?> collection, Object item) {

		int pos = -1;
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w = itemSource.get(i);
			if(w.WrapperNodeDataSource.get().equals(item)) {
				pos = i;
				break;
			}
		}
		
		if(pos>-1) {
			int level = -1;
			TreeViewItemWrapper w = itemSource.get(pos);
			w.detach();
			level = w.level;			
			removeChildrenFromNode(w);
			itemSource.remove(pos);
			
			if(pos>0) {
				// check if we have to remove the icon
				w = itemSource.get(pos-1);
				if(w.level < level && w.children != null && w.children.size() == 0) {
					w.WrapperImage.set(null);
				}
			}			
		}		
	}
	
	private void removeNodesFromCollection(IObservableCollection<?> collection) {
				
		if(treeStructure != null && treeStructure.root != null && 
				treeStructure.root.get() != null && treeStructure.root.get().equals(collection)) {
		
			for(int i=0; i<itemSource.size(); i++) {
				TreeViewItemWrapper w = itemSource.get(i);
				w.detach();
				if(w.level == 0)
					removeChildrenFromNode(w);
			}
			
			itemSource.clear();						
		} else {
			int pos = -1;
			for(int i=0; i<itemSource.size(); i++) {
				TreeViewItemWrapper w = itemSource.get(i);
				if(w.children != null && w.children.get().equals(collection)) {
					pos = i;
					break;
				}
			}
			
			if(pos>-1) {
				TreeViewItemWrapper w = itemSource.get(pos);			
				removeChildrenFromNode(w);
				w.WrapperImage.set(null);			
			}
		}
	}
	
	private void removeChildrenFromNode(TreeViewItemWrapper node) {					
		int from = itemSource.indexOf(node);
		int removeChildCount=0;
				
		if( from < 0)
			throw new IndexOutOfBoundsException();
		
		for(int i=from+1; i < itemSource.size(); i++) {
			TreeViewItemWrapper w = itemSource.get(i);
			if(node.level >= w.level)
				break;
			removeChildCount++;
		}
		
		if(removeChildCount >0) {
			for(int k=0; k<removeChildCount; k++) {
				TreeViewItemWrapper w = itemSource.get(from+1);
				if(w!=null && w.children!=null)
					w.children.unsubscribe(collectionObserver);
				w.detach();
				itemSource.remove(from+1);
			}
		}
	}
	
	private void handleExpandCollapse(int pos, boolean setNewValue) {
		TreeViewItemWrapper w = itemSource.get(pos);
		if( w == null )
			return;
		
		if( !isDSLeaf(w.WrapperNodeDataSource.get()))
			return;
		
		boolean newState=false;
		Boolean wrapperState = w.isExpanded();
		if( wrapperState != null && wrapperState == true )
			newState = true;
		
		IObservableCollection<?> children = getDSChildren(w.WrapperNodeDataSource.get());
		if( children != null && children.size() > 0) {
			if(setNewValue) {
				if(newState) {				
					removeChildrenFromNode(w);				
					setNodeExpanded(w, false, setNewValue);
				} else {					
					insertNodesFromCollection(w, (IObservableCollection<?>)children);
					setNodeExpanded(w, true, setNewValue);
				}
			} else {
				if(newState) {									
					insertNodesFromCollection(w, (IObservableCollection<?>)children);
					setNodeExpanded(w, true, setNewValue);
				} else {					
					removeChildrenFromNode(w);				
					setNodeExpanded(w, false, setNewValue);
				}
			}
		}	
		
		//dumpTree();
	}	
	
	private boolean isDSLeaf(Object obj) {
		IObservable<?> childrenField = Utility.getObservableByFieldPath(obj, treeStructure.childrenObservableName.get().toString());
		if( childrenField == null)
			return true;
		
		if(!(childrenField.get() instanceof IObservableCollection<?>))
			return false;
				
		IObservableCollection<?> children = (IObservableCollection<?>)childrenField.get();
		if( children != null && children.size() > 0)
			return true;
			
		return false;
	}	
	
	private void setNodeExpanded(TreeViewItemWrapper w, Boolean newValue, boolean setNewValue) {
		if( newValue == null) {
			w.WrapperImage.set(null);
			return;
		}

		if(newValue)
			w.WrapperImage.set(treeStructure.imgExpanded.get());
		else
			w.WrapperImage.set(treeStructure.imgCollapsed.get());	
		
		if(setNewValue)
			w.setExpanded(newValue);		
	}
	
	
	private void listChanged(CollectionChangedEventArg e, IObservableCollection<?> collection) {			
		int level = 0;		
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w=itemSource.get(i);
			if(collection.equals(w.children)) {
				level = w.level+1;
			}
		}		
		
		switch( e.getAction()) {
			case Add:				
				listChangedAddItems(e, collection, level);
				break;	
			case Remove:
				for(Object item : e.getOldItems()) {
					removeItemRecursiveFromCollection(collection, item);
				}
				break;
			case Replace:
				for(Object item : e.getOldItems()) {
					removeItemRecursiveFromCollection(collection, item);
				}				
				listChangedAddItems(e, collection, level);
				break;
			case Reset:		
				removeNodesFromCollection(collection);
				break;
			case Move:
				// currently the observable array list doesn't create this action
				throw new IllegalArgumentException("move not implemented");
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}		
			
	}
	
	private void listChangedAddItems(CollectionChangedEventArg e, IObservableCollection<?> collection, int level) {
		
		int pos = e.getNewStartingIndex();
		if( pos < 0)
			pos=0;
		
		SiblingPos sibling;		
		TreeViewItemWrapper w=null;
		
		for(Object item : e.getNewItems()) {
			sibling=findSiblingPos(collection, pos);	
			if(!sibling.isNew) {
				insertNode(collection, item, sibling.pos, level);
			} else {
				if(sibling.pos>0)
					w = itemSource.get(sibling.pos-1);
				if( w.isExpanded() != null && w.isExpanded() == true ) {
					// the node is defined "expanded" by the datasource, set and change the icon
					if(w!=null && (w.WrapperImage.get() == null || !w.WrapperImage.get().equals(treeStructure.imgExpanded.get())))
						w.WrapperImage.set(treeStructure.imgExpanded.get());
					insertNode(collection, item, sibling.pos, level);														
				} else {
					//the node is defined "collapsed" by the datasource, just setup the icon
					if(w!=null && (w.WrapperImage.get() == null || !w.WrapperImage.get().equals(treeStructure.imgCollapsed.get())))
						w.WrapperImage.set(treeStructure.imgCollapsed.get());
				}
			}
			pos++;
		}
	}

	private static class SiblingPos {
		public int pos=-1;
		public boolean isNew=false;
	}

	private SiblingPos findSiblingPos(IObservableCollection<?> collection, int offset) {
		SiblingPos result = new SiblingPos();
		int level = -1;
		int start = -1;
		int count = -1;
		
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w=itemSource.get(i);
			if(collection.equals(w.children)) {
				start = i;
				level = w.level;
				break;
			}
		}
		
		if(start > -1 && level > -1) {
			level++;
			for(int i=start+1; i<itemSource.size(); i++) {
				TreeViewItemWrapper w=itemSource.get(i);
				if(level == w.level) {
					// a sibling					
					result.pos = i;
					count++;
					if(count == offset)
						return result;
				}
					
				if(w.level < level) {
					// last in row
					if(offset+1 == collection.size()) {
						result.pos = i;
						if(count == -1)
							result.isNew = true;
						return result;
					}
					// no items, yet visible
					if(count==-1) {
						result.pos = start;						
						return result;
					}
					throw new IndexOutOfBoundsException();
				}
			}
			
			result.pos = itemSource.size();
			result.isNew = true;
			return result;
		}
		
		if(treeStructure != null && treeStructure.root != null && 
				treeStructure.root.get() != null && treeStructure.root.get().equals(collection)) {
			// insert at the root collection
			count = -1;
			level = 0;
			
			for(int i=0; i<itemSource.size(); i++) {
				TreeViewItemWrapper w=itemSource.get(i);
				if(level == w.level) {
					// a sibling					
					result.pos = i;
					count++;
					if(count == offset)
						return result;
				}
			}
			
			// last in row
			result.pos = itemSource.size();
			return result;
		}
		
		throw new IndexOutOfBoundsException();
		
	}
	
	private void ensureVisible(Object node) {
		if(node==null)
			return;
				
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w = itemSource.get(i);
			if(node.equals(w.WrapperNodeDataSource.get())) {
				if(smoothScrollEnsureVisible)
					Utility.ensureVisibleSmoothScroll(this, i);
				else
					Utility.ensureVisible(this, i);
				return;
			}
		}
	}

	public void setEnsureVisible(Object node) {
		ensureVisible(node);
	}


/*
	private void dumpTree() {
		for(int i=0; i<itemSource.size(); i++) {
			TreeViewItemWrapper w =itemSource.get(i);
			android.util.Log.v("xxx", "t:" + w.toString());
		}
	}
*/	
}
