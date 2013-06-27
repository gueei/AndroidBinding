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
import gueei.binding.menu.MenuItemViemodel;
import gueei.binding.observables.StringObservable;
import gueei.binding.v30.widget.PopupMenuBinderV30;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsClickEvent;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsItemWrapper;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsLongClickEvent;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsSelectedEvent;
import gueei.binding.widgets.breadcrumbs.BreadCrumbsStructure;
import gueei.binding.widgets.breadcrumbs.Utility;

import com.devsmart.android.ui.HorizontalListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.PopupMenu;

public class BreadCrumbs extends HorizontalListView {
	
	// make this configureable by the user
	public final String EMPTY_MENU_ENTRY = "-";
	public final String SELECTED_MENU_ENTRY = "[*] ";
	
	private PopupMenu popup;
	private BreadCrumbsItemWrapper rootWrapper=null;
	
	private BreadCrumbsStructure breadCrumbsStructure;
	private boolean basicSetupDone;
	
	private Command OnBreadCrumbClickedRef;
	private Command OnBreadCrumbLongClickedRef;
	private Command OnBreadCrumbSelectedRef;
	
	public final ArrayListObservable<BreadCrumbsItemWrapper> itemSource = new ArrayListObservable<BreadCrumbsItemWrapper>(BreadCrumbsItemWrapper.class);	
	
	public final Command OnItemClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {		
			Integer pos = Utility.getClickedListPositon(itemSource, args);
			if(pos == null)
				return;
			
			if(OnBreadCrumbClickedRef != null) {
				BreadCrumbsItemWrapper w = itemSource.get(pos);
				BreadCrumbsClickEvent e = new BreadCrumbsClickEvent(pos, w.WrapperBreadCrumbDataSource.get());
						
				OnBreadCrumbClickedRef.Invoke(BreadCrumbs.this, e, args);
				if( e.eventHandled)
					return;
			}
			
			if(args.length >0 && args[0] instanceof View)
				view = (View)args[0];
			
			createChildPopup(view, pos);
		}
	};
	
	public final Command OnItemLongClicked = new Command() {		
		@Override
		public void Invoke(View view, Object... args) {
			Integer pos = Utility.getClickedListPositon(itemSource, args);
			if(pos == null || OnBreadCrumbLongClickedRef == null)
				return;
			
			BreadCrumbsItemWrapper w = itemSource.get(pos);
			BreadCrumbsLongClickEvent e = new BreadCrumbsLongClickEvent(pos, w.WrapperBreadCrumbDataSource.get());
						
			OnBreadCrumbLongClickedRef.Invoke(BreadCrumbs.this, e, args);			
		}
	};		
	
	private Observer observerRoot = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if(prop!=null && prop.get() != null) {
				createRootWrapper(prop.get());	
				rebind();
			}
		}
	};
	
	private CollectionObserver collectionObserver = new CollectionObserver(){
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};
	
	public BreadCrumbs(Context context) {
		super(context, null);
	}

	public BreadCrumbs(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BreadCrumbs(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);	
	}			
	
	@Override
	protected void onDetachedFromWindow() {
		if(popup!=null) {
			popup.dismiss();
		}
		super.onDetachedFromWindow();
	}
	

	public void setBreadCrumbsStructure(BreadCrumbsStructure value) {
		if(breadCrumbsStructure!=null && breadCrumbsStructure.root!=null)
			breadCrumbsStructure.root.unsubscribe(observerRoot);
		
		breadCrumbsStructure = value;
		
		if(breadCrumbsStructure!=null && breadCrumbsStructure.root != null)
			breadCrumbsStructure.root.subscribe(observerRoot);
				
		basicSetup();
		rebind();
	}

	public BreadCrumbsStructure getBreadCrumbsStructure() {
		return this.breadCrumbsStructure;
	}
	
	public ArrayList<Object> getCurrentPath() {
		ArrayList<Object> result = new ArrayList<Object>();
		if(itemSource.size() == 0)
			return result;		
		
		// the last element data is always zero
		for(int i=0; i<itemSource.size()-1; i++) {
			BreadCrumbsItemWrapper w = itemSource.get(i);
			result.add(w.WrapperBreadCrumbDataSource.get());
		}
		
		return result;
	}
	

	public ArrayList<Object> getNodeWrapperPath(BreadCrumbsItemWrapper node) {
		ArrayList<Object> result = new ArrayList<Object>();
		if(itemSource.size() == 0)
			return result;		
		
		for(int i=0; i<itemSource.size(); i++) {
			BreadCrumbsItemWrapper w = itemSource.get(i);
			if(w.equals(node))
				break;
			result.add(w.WrapperBreadCrumbDataSource.get());
		}
		
		return result;
	}
	
	public void setSelectedPosition(BreadCrumbsItemWrapper wrapper, int childPos) {
		int pos = itemSource.indexOf(wrapper);
		Object root=getParentDS(wrapper);

		// delete our children
		removeBreadCrumbs(pos);
		
		@SuppressWarnings("unchecked")
		IObservable<Object> node = (IObservable<Object>)getDSSelectedPositonObervable(root);			
		if(node !=null ) {
			if(childPos>-1)
				node.set(childPos);
			else
				node.set(null);
		}		
		
		insertItemsRecursive(root);	
	}
	
	private Object getParentDS(BreadCrumbsItemWrapper wrapper) {
		Object parent=null;
		
		int pos = itemSource.indexOf(wrapper);
		if(pos < 0)
			return parent;
	
		if(pos>0) {
			parent = wrapper.parent;	
		} else {
			if(breadCrumbsStructure.root != null)
				parent=breadCrumbsStructure.root.get();
		}	
		
		return parent;
	}

	private void removeBreadCrumbs(int pos) {
		for(;;) {
			if(itemSource.size()==0)
				break;
			if(pos > itemSource.size() -1)
				break;
			
			BreadCrumbsItemWrapper last = itemSource.get(itemSource.size()-1);		
			last.setSelectedPositionValue(null); // TODO: this should be configurable if we want to close every childnode
			last.detach();
			if(last.children != null)
				last.children.unsubscribe(collectionObserver);
			itemSource.remove(last);			
		}
	}

	private void basicSetup() {
		if(basicSetupDone)
			return;
		
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "onItemClicked", ".", OnItemClicked);
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "onItemLongClicked", ".", OnItemLongClicked);
		AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "itemSource", ".", itemSource);
		
		basicSetupDone = true;
	}
	
	private void createChildPopup(View view, int pos) {
		if(view == null)
			return;
		
		BreadCrumbsItemWrapper w = itemSource.get(pos);
		Object root=getParentDS(w);

		if( root == null)
			return;
		
		IObservableCollection<?> children = w.children;
		
		ArrayListObservable<MenuItemViemodel> menuItems=new ArrayListObservable<MenuItemViemodel>(MenuItemViemodel.class);
		
		MenuItemViemodel mi = new MenuItemViemodel();
		mi.title = new StringObservable(EMPTY_MENU_ENTRY);
		mi.onClick = createMenuItemClickCmd(w,-1);
		menuItems.add(mi);					
		
		for(int i=0; i < children.size(); i++) {
			IObservable<?> text = getDSText(children.getItem(i));			
			
			StringObservable title;
			if(text==null)
				title = new StringObservable("binding error: menuText field not found, check logcat");
			else
				title = new StringObservable(text.get().toString());						
			
			if(children.getItem(i).equals(w.WrapperBreadCrumbDataSource.get())){
				title.set( SELECTED_MENU_ENTRY + title.get());
			}
			
			mi = new MenuItemViemodel();
			mi.title = title;
			mi.onClick = createMenuItemClickCmd(w,i);
			menuItems.add(mi);			
		}
		
		if(popup!=null) {
			popup.dismiss();
		}
		
		popup = new PopupMenu(view.getContext(), view);				
		PopupMenuBinderV30 menuBinder = new PopupMenuBinderV30();
		menuBinder.bindPopupMenu(popup, menuItems);
		popup.show();
	}
	
	private Command createMenuItemClickCmd(final BreadCrumbsItemWrapper w, final int siblingPos) {
		Command onClick = new Command() {
			
			@Override
			public void Invoke(View view, Object... args) {
				
				Object item=null;
				if(siblingPos>-1) {
					IObservableCollection<?> siblings = getDSChildren(w.parent);				
					item = siblings.getItem(siblingPos);
				}				
			
				if(OnBreadCrumbSelectedRef != null) {
					Object breadCrumbNodeOld = w.WrapperBreadCrumbDataSource.get();
					Object breadCrumbNodeNew = item;
					BreadCrumbsSelectedEvent e = new BreadCrumbsSelectedEvent(BreadCrumbs.this, siblingPos, breadCrumbNodeOld, breadCrumbNodeNew, w);								
					OnBreadCrumbSelectedRef.Invoke(BreadCrumbs.this, e);
					if( e.eventHandled)
						return;
				}				
			
				setSelectedPosition(w, siblingPos);
			}
		};
		
		return onClick;
	}
	
	private void rebind() {
		for(int i=0; i<itemSource.size(); i++) {
			BreadCrumbsItemWrapper w = itemSource.get(i);
			w.detach();
			if(w.children != null)
				w.children.unsubscribe(collectionObserver);

		}
		itemSource.clear();
		
		if(breadCrumbsStructure == null)
			return;
		
		Layout template = null;	
		if(breadCrumbsStructure.wrapperTemplate!=null) {
			template = breadCrumbsStructure.wrapperTemplate;
		}
		if(breadCrumbsStructure.wrapperTemplateId!=null && breadCrumbsStructure.wrapperTemplateId.get() instanceof Integer) {
			int id = (Integer) breadCrumbsStructure.wrapperTemplateId.get();
			template = new SingleTemplateLayout(id);
		}			
		
		try {
			Adapter adapter = gueei.binding.collections.Utility.getSimpleAdapter(getContext(), itemSource, template, null, null, null);
			AttributeBinder.getInstance().bindAttributeWithModel(getContext(), this, "adapter", ".", adapter);
		} catch (Exception e) {
			return;
		}
				
		rebindBreadCrumbsNodeCommands();						
			
		if(breadCrumbsStructure.root != null) {
			createRootWrapper(breadCrumbsStructure.root.get());
			insertItemsRecursive(breadCrumbsStructure.root.get());
		}
	}
	
	private void rebindBreadCrumbsNodeCommands() {		
		OnBreadCrumbClickedRef = null;
		OnBreadCrumbLongClickedRef = null;		
		OnBreadCrumbSelectedRef = null;
		
		if(breadCrumbsStructure == null)
			return;
		
		if(breadCrumbsStructure.breadCrumbClicked != null) {
			if( breadCrumbsStructure.breadCrumbClicked.get() instanceof Command)
				OnBreadCrumbClickedRef = (Command)breadCrumbsStructure.breadCrumbClicked.get();
		}
		
		if(breadCrumbsStructure.breadCrumbLongClicked != null) {
			if( breadCrumbsStructure.breadCrumbLongClicked.get() instanceof Command)
				OnBreadCrumbLongClickedRef = (Command)breadCrumbsStructure.breadCrumbLongClicked.get();
		}
		
		if(breadCrumbsStructure.breadCrumbSelected != null) {
			if( breadCrumbsStructure.breadCrumbSelected.get() instanceof Command)
				OnBreadCrumbSelectedRef = (Command)breadCrumbsStructure.breadCrumbSelected.get();
		}	
	}
	
	protected void createRootWrapper(Object item) {		
		if(rootWrapper!=null) {
			rootWrapper.detach();
			if(rootWrapper.children != null)
				rootWrapper.children.unsubscribe(collectionObserver);
		}
		
		BreadCrumbsItemWrapper w = new BreadCrumbsItemWrapper(this);				
		if(breadCrumbsStructure.template != null)
			w.WrapperBreadCrumbLayoutId.set(breadCrumbsStructure.template.getDefaultLayoutId());		
			
		w.children = getDSChildren(item);	
		
		if( w.children != null )
			w.children.subscribe(collectionObserver);
		
		rootWrapper = w;
	}
	
	private void insertItemsRecursive(Object item) {
		if(item==null)
			return;
		
		BreadCrumbsItemWrapper w = new BreadCrumbsItemWrapper(this);				
		if(breadCrumbsStructure.template != null)
			w.WrapperBreadCrumbLayoutId.set(breadCrumbsStructure.template.getDefaultLayoutId());		

		IObservable<?> selectedPositionObservable = getDSSelectedPositonObervable(item);
		w.setSelectedPositionObservable(selectedPositionObservable);
		IObservableCollection<?> children = getDSChildren(item);	
		
		Object selectedChild = null;
		int pos = getDSSelectedPositon(item);
		if(pos>-1 && children != null && pos < children.size()) {
			selectedChild = children.getItem(pos);
		}
		
		// this is an optional datasource based layout		
		IObservable<?> layoutIdObservable = geDSLayoutIdObservable(selectedChild);
		if(layoutIdObservable!=null) {
			w.setLayoutIdObservable(layoutIdObservable);
		}	
		
		w.parent = item;
		w.WrapperBreadCrumbDataSource.set(selectedChild);
		w.children = children;		
				
		itemSource.add(w);
		
		if( w.children != null )
			w.children.subscribe(collectionObserver);
		
		if(selectedChild!=null)
			insertItemsRecursive(selectedChild);				
	}	
	
	protected void listChanged(CollectionChangedEventArg e, IObservableCollection<?> collection) {		
		switch( e.getAction()) {
			case Add:				
				// this is never need, as we have always visible leafe nodes
				break;	
			case Remove:
				for(Object item : e.getOldItems()) {
					removeItemFromCollection(collection, item, true);
				}
				break;
			case Replace:
				for(Object item : e.getOldItems()) {
					removeItemFromCollection(collection, item, false);
				}				
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

	private void removeNodesFromCollection(IObservableCollection<?> collection) {
		int pos = getItemSourceCollectionChildPositon(collection);
		if(pos<0) {
			rebind();
			return;
		}
		
		BreadCrumbsItemWrapper w = itemSource.get(pos);
		Object root=getParentDS(w);
		
		// we are also deleting the collections node
	
		removeBreadCrumbs(pos);
		
		if(root != null) {			
			insertItemsRecursive(root);
		}
	}

	
	private void removeItemFromCollection(IObservableCollection<?> collection, Object item, boolean removeSelection) {	
		int pos = getItemSourceParentPositon(item);
		if(pos<0)
			return;		
		
		// we delete the selected node and rebuild it
		pos--;			
		
		BreadCrumbsItemWrapper w = itemSource.get(pos);
		Object root=getParentDS(w);				
		
		// get the current child positon
		int selectedChildPos = getDSSelectedPositon(root);
		
		removeBreadCrumbs(pos);

		if(root != null) {			
			// remove our selection from the datasource, as this item was currently deleted		
			@SuppressWarnings("unchecked")
			IObservable<Object> node = (IObservable<Object>)getDSSelectedPositonObervable(root);			
			if(node !=null ) {
				if(removeSelection) {
					node.set(null);
				} else {
					if(selectedChildPos>-1)
						node.set(selectedChildPos);
				}
			}
			
			insertItemsRecursive(root);
		}
		
	}
	
	private int getItemSourceParentPositon(Object item) {
		int pos = -1;
		
		if(item==null)
			return pos;
		
		BreadCrumbsItemWrapper w=null;
		for(int i=0; i<itemSource.size(); i++) {
			w=itemSource.get(i);
			if(item.equals(w.parent)) {
				pos = i;
				break;
			}
		}	
		
		return pos;
	}
	
	private int getItemSourceCollectionChildPositon(IObservableCollection<?> item) {
		int pos = -1;
		
		if(item==null)
			return pos;
		
		BreadCrumbsItemWrapper w=null;
		for(int i=0; i<itemSource.size(); i++) {
			w=itemSource.get(i);
			if(item.equals(w.children)) {
				pos = i;
				break;
			}
		}	
		
		return pos;
	}

	private IObservable<?> geDSLayoutIdObservable(Object obj) {
		if(breadCrumbsStructure.templateIdObservableName == null )
			return null;
		
		IObservable<?> templateIdField = Utility.getObservableByFieldPath(obj, breadCrumbsStructure.templateIdObservableName.get().toString());
		if( templateIdField != null && (templateIdField.get() instanceof Integer))
			return templateIdField;
				
		return null;
	}
	
	private IObservable<?> getDSText(Object obj) {
		if(obj == null || breadCrumbsStructure == null || breadCrumbsStructure.menuTextName == null )
			return null;
		
		IObservable<?> value = Utility.getObservableByFieldPath(obj, breadCrumbsStructure.menuTextName.get().toString());
		return value;
	}
	
	private IObservableCollection<?> getDSChildren(Object obj) {
		IObservable<?> childrenField = Utility.getObservableByFieldPath(obj, breadCrumbsStructure.childrenObservableName.get().toString());
		if( childrenField == null || !(childrenField.get() instanceof IObservableCollection<?>))
			return null;
				
		IObservableCollection<?> children = (IObservableCollection<?>)childrenField.get();
		return children;
	}
	
	private IObservable<?> getDSSelectedPositonObervable(Object obj) {
		if(obj == null || breadCrumbsStructure == null || breadCrumbsStructure.selectedPositionObservableName == null )
			return null;
		
		IObservable<?> value = Utility.getObservableByFieldPath(obj, breadCrumbsStructure.selectedPositionObservableName.get().toString());
		return value;
	}
	
	private int getDSSelectedPositon(Object obj) {
		if(obj == null || breadCrumbsStructure == null || breadCrumbsStructure.selectedPositionObservableName == null )
			return -1;
		
		IObservable<?> value = Utility.getObservableByFieldPath(obj, breadCrumbsStructure.selectedPositionObservableName.get().toString());
		if(value==null || !(value.get() instanceof Integer) )
			return -1;
		return (Integer)value.get();
	}


}
