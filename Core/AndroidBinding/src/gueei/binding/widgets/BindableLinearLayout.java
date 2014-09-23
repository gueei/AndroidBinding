package gueei.binding.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
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
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.LayoutItem;
import gueei.binding.Observer;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BindableLinearLayout extends LinearLayout implements IBindableView<BindableLinearLayout>, IBindableLayout {
	private ArrayList<Object> mCurrentList = null;
	private IObservableCollection<?> mItemList = null;
	private LayoutItem mLayoutItem = null;
	private SparseArray<BindableLayoutContent> mItemMap = new SparseArray<BindableLayoutContent>(); 
	
	private CollectionObserver mCollectionObserver = new CollectionObserver() {
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};

	private ObservableMultiplexer<Object> mObservableItemsLayoutID = new ObservableMultiplexer<Object>(new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if( initiators == null || initiators.size() < 1)
				return;
			Object parent = initiators.toArray()[0];
			int pos = mCurrentList.indexOf(parent);						
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(parent);
			removeAllItems(list);
			bindItem(pos, parent);	 
		}
	});
	
	public BindableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BindableLinearLayout(Context context) {
		super(context);
	}
	
	@Override
	public void unbind () {		
		synchronized(this){
			if( mItemList != null && mCollectionObserver != null)
				mItemList.unsubscribe(mCollectionObserver);
					
			mObservableItemsLayoutID.clear();
			mCurrentList = null;
			mItemList=null;		
			
			for(int i = 0; i < mItemMap.size(); i++) {
				BindableLayoutContent content = mItemMap.valueAt(i);
			    if(content==null)
			    	continue;
			    Binder.unbindView(getContext(), content.getInflateResult());
			}
			
			removeAllViews();
		}
	}
		
	@Override
	protected void onDetachedFromWindow() {
		unbind();
		super.onDetachedFromWindow();
	}
	
	protected void bind(IObservableCollection<?> newList) {
		synchronized(this){
			if(newList==null)
				return;
			
			mItemList = newList;
			mItemList.subscribe(mCollectionObserver);		
			mCurrentList = new ArrayList<Object>();
			
			for(int pos=0; pos < newList.size(); pos ++) {
				Object item = newList.getItem(pos);
				bindItem(pos, item);
				mCurrentList.add(item);
			}
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
					bindItem(pos, item);
					pos++;
				}
				break;
			case Remove:
				removeAllItems(e.getOldItems());	
				break;
			case Replace:
				removeAllItems(e.getOldItems());	
				pos = e.getNewStartingIndex();
				if( pos < 0)
					pos=0;
				for(Object item : e.getNewItems()) {
					bindItem(pos, item);
					pos++;
				}
				break;
			case Reset:
				unbind();
				bind(collection);
				break;
			case Move:
				// currently the observable array list doesn't create this action
				throw new IllegalArgumentException("move not implemented");
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}
		
		if( collection == null)
			return;	
		
		mCurrentList = new ArrayList<Object>();
		for( pos=0; pos < collection.size(); pos ++ ) {
			Object item = collection.getItem(pos);
			mCurrentList.add(item);
		}		
	}	
	
	private ViewAttribute<BindableLinearLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ObservableCollection<?> ))
						return;

					if( mLayoutItem != null ) {
						unbind();
						bind((ObservableCollection<Object>)newValue);
					}
				}

				@Override
				public Object get() {
					return mItemList;
				}				
	};	

	private ViewAttribute<BindableLinearLayout, Object> ItemLayoutAttribute =
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemLayout"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					mLayoutItem = null;
					if( newValue instanceof LayoutItem ) {
						mLayoutItem = (LayoutItem) newValue;
					}else if (newValue instanceof Layout){
						mLayoutItem = new LayoutItem(((Layout)newValue).getDefaultLayoutId());
					}else if (newValue instanceof Integer){
						mLayoutItem = new LayoutItem((Integer)newValue);
					}else{
						mLayoutItem = new LayoutItem(newValue.toString());
					}
					
					if( mItemList != null ) {
						unbind();
						bind(mItemList);
					}
				}

				@Override
				public Object get() {
					return mLayoutItem;
				}
	};		

	@Override
	public ViewAttribute<BindableLinearLayout, ?> createViewAttribute(String attributeId) {	
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("itemLayout")) return ItemLayoutAttribute;
		return null;
	}
	
	private void removeAllItems(List<?> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || mCurrentList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(mCurrentList.toArray()));
		
		for(Object item : deleteList){
			int pos = currentPositionList.indexOf(item);			
			mObservableItemsLayoutID.removeParent(item);
			currentPositionList.remove(item);
			if( pos > -1 && pos < this.getChildCount()){
				BindableLayoutContent content = mItemMap.valueAt(pos);
			    if(content!=null)
			    	Binder.unbindView(getContext(), content.getInflateResult());
				removeViewAt(pos);				    					
			}
		}
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void bindItem(int pos, Object item) {		
		if( mLayoutItem == null )
			return;
		
		int layoutId = mLayoutItem.getLayoutId();		
		if( layoutId < 1 && mLayoutItem.getLayoutName() != null ) {									
			IObservable<?> observable = null;			
			InnerFieldObservable ifo = new InnerFieldObservable(mLayoutItem.getLayoutName());
			if (ifo.createNodes(item)) {
				observable = ifo;										
			} else {			
				Object rawField;
				try {
					rawField = Binder.getSyntaxResolver().getFieldForModel(mLayoutItem.getLayoutName(), item);
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
				mObservableItemsLayoutID.add(observable, item);	
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
			InflateResult inflateResult = Binder.inflateView(getContext(), layoutId, this, false);
			
			BindableLayoutContent content = new BindableLayoutContent();
			content.setInflateResult(inflateResult);
			mItemMap.append(pos, content);
			
			for(View view: inflateResult.processedViews){
				AttributeBinder.getInstance().bindView(getContext(), view, item);
			}			
			child = inflateResult.rootView;
		}
										
		addView(child,pos);
	}
		
}
