package gueei.binding.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.ISyntaxResolver.SyntaxResolveException;
import gueei.binding.InnerFieldObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.utility.WeakList;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.LayoutItem;
import gueei.binding.Observer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * BindableLinearLayout have three attributes
 * 
 * 
 *
 */
public class BindableLinearLayout extends LinearLayout implements IBindableView<BindableLinearLayout> {
	private WeakList<Object> currentList = null;
	private CollectionObserver collectionObserver = new CollectionObserver() {
		@Override
		public void onCollectionChanged(
				IObservableCollection<?> collection,
				CollectionChangedEventArg args,
				Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};

	
	private ObservableCollection<Object> itemList = null;
	private LayoutItem layout = null;
	private boolean updateEnabled = true;

	private ObservableMultiplexer<Object> observableItemsLayoutID = new ObservableMultiplexer<Object>(new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if( initiators == null || initiators.size() < 1)
				return;
			Object parent = initiators.toArray()[0];
			int pos = currentList.indexOf(parent);						
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(parent);
			removeItems(list);
			insertItem(pos, parent);	 
		}
	});
	
	public BindableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BindableLinearLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
	}
		
	@Override
	protected void onDetachedFromWindow() {
		observableItemsLayoutID.clear();
		currentList = null;
		super.onDetachedFromWindow();
	}
	
	private void createItemSourceList(ObservableCollection<Object> newList) {		
		if( itemList != null && collectionObserver != null)
			itemList.unsubscribe(collectionObserver);
		
		itemList = newList;
		
		if(newList==null)
			return;

		currentList = null;	
		
		itemList.subscribe(collectionObserver);
		newList(newList);
	}	
	
	private void newList(IObservableCollection<?> list) {
		this.removeAllViews();	
		
		observableItemsLayoutID.clear();
						
		if( list == null) {
			currentList = null;
			return;
		}
		
		currentList = new WeakList<Object>();
		
		for( int pos=0; pos < list.size(); pos ++ ) {
			Object item = list.getItem(pos);
			insertItem(pos, item);
		}
		
		for( int pos=0; pos < list.size(); pos ++ ) {
			Object item = list.getItem(pos);
			currentList.add(item);
		}
	}

	private void listChanged(CollectionChangedEventArg e, IObservableCollection<?> collection) {
		if( e == null)
			return;
		
		int pos=-1;
		switch( e.getAction()) {
			case Add:
				pos = e.getNewStartingIndex();
				for(Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Remove:
				removeItems(e.getOldItems());	
				break;
			case Replace:
				removeItems(e.getOldItems());	
				pos = e.getNewStartingIndex();
				if( pos < 0)
					pos=0;
				for(Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Reset:
				newList(collection);
				break;
			case Move:
				// currently the observable array list doesn't create this action
				throw new IllegalArgumentException("move not implemented");
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}
		
		if( collection == null)
			return;	
		
		currentList = new WeakList<Object>();
		for( pos=0; pos < collection.size(); pos ++ ) {
			Object item = collection.getItem(pos);
			currentList.add(item);
		}		
	}	
	
	private ViewAttribute<BindableLinearLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ObservableCollection<?> ))
						return;

					if( layout != null )
						createItemSourceList((ObservableCollection<Object>)newValue);
				}

				@Override
				public Object get() {
					return itemList;
				}				
	};	

	private ViewAttribute<BindableLinearLayout, Object> ItemLayoutAttribute =
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemLayout"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					layout = null;
					if( newValue instanceof LayoutItem ) {
						layout = (LayoutItem) newValue;
					}else if (newValue instanceof Layout){
						layout = new LayoutItem(((Layout)newValue).getDefaultLayoutId());
					}else if (newValue instanceof Integer){
						layout = new LayoutItem((Integer)newValue);
					}else{
						layout = new LayoutItem(newValue.toString());
					}
					
					if( itemList != null )
						createItemSourceList(itemList);
				}

				@Override
				public Object get() {
					return layout;
				}
	};	
	
	private ViewAttribute<BindableLinearLayout, Boolean> ItemUpdateEnabledAttribute =
			new ViewAttribute<BindableLinearLayout, Boolean>(Boolean.class, BindableLinearLayout.this, "UpdateEnabled"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					if( newValue == null ) {
						updateEnabled = true;
					}
					else if( newValue instanceof Boolean ) {
						Boolean value = (Boolean) newValue;
						updateEnabled = value; 
						if(updateEnabled) {
							BindableLinearLayout.this.invalidate();
						}
					}
				}

				@Override
				public Boolean get() {
					return updateEnabled;
				}
	};	


	@Override
	protected void onDraw(Canvas canvas) {
		if( !updateEnabled )
			return;
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
		if( !updateEnabled )
			return;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public ViewAttribute<BindableLinearLayout, ?> createViewAttribute(String attributeId) {	
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("itemLayout")) return ItemLayoutAttribute;
		if (attributeId.equals("updateEnabled")) return ItemUpdateEnabledAttribute;
		return null;
	}
	
	private void removeItems(List<?> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || currentList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(currentList.toArray()));
		
		for(Object item : deleteList){
			int pos = currentPositionList.indexOf(item);			
			observableItemsLayoutID.removeParent(item);
			currentPositionList.remove(item);
			if( pos > -1 && pos < this.getChildCount())
				this.removeViewAt(pos);			
		}
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertItem(int pos, Object item) {		
		if( layout == null )
			return;
		
		int layoutId = layout.getLayoutId();		
		if( layoutId < 1 && layout.getLayoutName() != null ) {									
			IObservable<?> observable = null;			
			InnerFieldObservable ifo = new InnerFieldObservable(layout.getLayoutName());
			if (ifo.createNodes(item)) {
				observable = ifo;										
			} else {			
				Object rawField;
				try {
					rawField = Binder.getSyntaxResolver().getFieldForModel(layout.getLayoutName(), item);
				} catch (SyntaxResolveException e) {
					BindingLog.exception("BindableLinearLayout.insertItem()", e);
					return;
				}
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>)rawField;
				else if (rawField!=null)
					observable= new ConstantObservable(rawField.getClass(), rawField);
			}
			
			if( observable != null) {	
				observableItemsLayoutID.add(observable, item);	
				Object obj = observable.get();
				if(obj instanceof Integer)
					layoutId = (Integer)obj;
			}
		}
		
		View child = null;
		
		if( layoutId < 1 ) {
			TextView textView = new TextView(getContext());
			textView.setText("binding error - pos: " + pos + " has no layout - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			child = textView;
		} else {		
			Binder.InflateResult result = Binder.inflateView(getContext(), layoutId, this, false);
			for(View view: result.processedViews){
				AttributeBinder.getInstance().bindView(getContext(), view, item);
			}
			child = result.rootView;
		}
		 											
		this.addView(child,pos);
	}
	
}
