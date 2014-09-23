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
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.utility.ObservableCollectionMultiplexer;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.viewAttributes.templates.LayoutRowChild;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BindableTableLayout extends TableLayout implements IBindableView<BindableTableLayout>, IBindableLayout  {
	private ArrayList<Object> mCurrentRowList = null;
	private SparseArray<List<BindableLayoutContent>> mItemMapList = null;
	private CollectionObserver mCollectionObserver = null;
	
	private ObservableCollection<Object> mRowList = null;	
	private LayoutRowChild mRowChild = null;	
	
	private Observer mObserver = new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if( initiators == null || mCurrentRowList == null)
				return;
			int pos = -1;
			
			for(Object i : initiators) {
				pos = mCurrentRowList.indexOf(i);
				if( pos > 0)
					break;
			}
			if( pos < 0)
				return;
			Object rowData = mCurrentRowList.get(pos);
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(rowData);
			unbindRow(list);						
			addTablteRow(pos, rowData);	 
		}
	};
	
	private ObservableMultiplexer<Object> mObservableChildLayoutID = new ObservableMultiplexer<Object>(mObserver);
	private ObservableMultiplexer<Object> mObservableChildSpan = new ObservableMultiplexer<Object>(mObserver);	
	private ObservableCollectionMultiplexer<Object> mObservableCollectionRowChildren = new ObservableCollectionMultiplexer<Object>(mObserver);		
	
	public BindableTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BindableTableLayout(Context context) {
		super(context);
	}	
	
	@Override
	public void unbind () {		
		synchronized(this){
			if(mRowList != null) {
				mRowList.unsubscribe(mCollectionObserver);
				mCollectionObserver = null;
			}
			
			if(mItemMapList != null && mItemMapList.size() > 0) {
				for(int i = mItemMapList.size()-1; i > -1; i--) {
					unbindAndRemove(i);
				}
			}
			
			mItemMapList = null;
			mRowList = null;
			mObservableChildLayoutID.clear();
			mObservableChildSpan.clear();
			mObservableCollectionRowChildren.clear();							
			mCurrentRowList= null;
							
			removeAllViews();
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		unbind();
		super.onDetachedFromWindow();
	}
	
	private void bind(ObservableCollection<Object> newRowList) {				
		mRowList = newRowList;
		
		if(newRowList==null)
			return;
	
		mCollectionObserver = new CollectionObserver() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCollectionChanged(
					IObservableCollection<?> collection,
					CollectionChangedEventArg args,
					Collection<Object> initiators) {
				rowListChanged(args, (ObservableCollection<Object>)collection);
			}			
		};
		
		mRowList.subscribe(mCollectionObserver);
		
		mObservableChildLayoutID.clear();
		mObservableChildSpan.clear();
		mObservableCollectionRowChildren.clear();		
		
		mCurrentRowList = new ArrayList<Object>();
		mItemMapList = new SparseArray<List<BindableLayoutContent>>();
				
		for( int pos=0; pos < newRowList.size(); pos ++ ) {
			Object rowData = newRowList.getItem(pos);
			addTablteRow(pos, rowData);
			mCurrentRowList.add(rowData);
		}			
	}	
	
	private void rowListChanged(CollectionChangedEventArg e, ObservableCollection<Object> rows) {
		if( e == null)
			return;
		
		int pos=-1;
		switch( e.getAction()) {
			case Add:
				pos = e.getNewStartingIndex();
				for(Object rowData : e.getNewItems()) {
					addTablteRow(pos, rowData);
					pos++;
				}
				break;
			case Remove:
				unbindRow(e.getOldItems());	
				break;
			case Replace:
				unbindRow(e.getOldItems());	
				pos = e.getNewStartingIndex();
				if( pos < 0)
					pos=0;				
				for(Object item : e.getNewItems()) {
					addTablteRow(pos, item);
					pos++;
				}
				break;
			case Reset:
				unbind();
				bind(rows);
				break;
			case Move:
				// currently the observable array list doesn't create this action
				throw new IllegalArgumentException("move not implemented");				
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}
		
		if( rows == null)
			return;	
		
		for( pos=0; pos < rows.size(); pos ++ ) {
			Object item = rows.getItem(pos);
			mCurrentRowList.add(item);
		}					
	}	

	private ViewAttribute<BindableTableLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ArrayListObservable<?> ))
						return;
					mRowList = (ArrayListObservable<Object>)newValue;
					
					if( mRowChild != null ) {
						ObservableCollection<Object> list = mRowList;
						unbind();
						mRowList = list;
						bind(mRowList);
					}
				}

				@Override
				public Object get() {
					return mRowList;
				}				
	};	
					
	private ViewAttribute<BindableTableLayout, Object> RowChildAttribute =
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "RowChild"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					mRowChild = null;
					if( newValue instanceof LayoutRowChild ) {
						mRowChild = (LayoutRowChild) newValue;
						if( mRowList != null ){
							ObservableCollection<Object> current = mRowList;
							unbind();
							mRowList = current;
							bind(mRowList);
						}
					}
				}

				@Override
				public Object get() {
					return mRowChild;
				}
	};	
	

	@Override
	public ViewAttribute<BindableTableLayout, ?> createViewAttribute(String attributeId) {	
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("rowChild")) return RowChildAttribute;	
		return null;
	}	
	
	private void addTablteRow(int pos, Object rowData) {	
		synchronized(this){
			if( mRowChild == null )
				return;
			
			IObservable<?> childDataSource = null;			
			InnerFieldObservable<?> ifo = new InnerFieldObservable<Object>(mRowChild.getChildDataSource());
			if (ifo.createNodes(rowData)) {
				childDataSource = ifo;	
			} else {			
				Object rawField = null;
				try {
					rawField = Binder.getSyntaxResolver().getFieldForModel(mRowChild.getChildDataSource(), rowData);
				} catch (SyntaxResolveException e) {
					BindingLog.exception("BindableTableLayout.insertRow", e);
				}
				if (rawField instanceof IObservable<?>)
					childDataSource = (IObservable<?>)rawField;
			}				
			
			TableRow trow = createAndBindTableRow(childDataSource, pos, rowData);				 		
			mCurrentRowList.add(pos, rowData);
			
			addView(trow,pos);	
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableRow createAndBindTableRow(IObservable<?> childDataSource, int pos, Object rowData) {
		TableRow tableRow = new TableRow(getContext());		
		InnerFieldObservable<?> ifo = null;
		
		List<BindableLayoutContent> list = new ArrayList<BindableLayoutContent>();
		mItemMapList.append(pos, list);
				
		if( childDataSource == null) {	
			TextView textView = new TextView(getContext());
			textView.setText("binding error - row: " + pos + " has no child datasource - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			tableRow.addView(textView);
		} else {			
			Object dataSource = childDataSource.get();	
								
			if( dataSource instanceof ArrayListObservable<?>) {
				ArrayListObservable<?> childItems = (ArrayListObservable<?>)dataSource;
				
				// we might have to change the current row if the child datasource changes
				mObservableCollectionRowChildren.add(childItems,rowData);
				
				int col = 0;
			    for( Object childItem : childItems) {					
					int colSpan = 1;
					int layoutId = mRowChild.getLayoutId();		
					if( layoutId < 1 && mRowChild.getLayoutName() != null ) {									
						IObservable<?> observable = null;
						ifo = new InnerFieldObservable<Object>(mRowChild.getLayoutName() );
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = null;
							try {
								rawField = Binder.getSyntaxResolver().getFieldForModel(mRowChild.getLayoutName(), childItem);
							} catch (SyntaxResolveException e) {
								BindingLog.exception("BindableTableLayout.createRow", e);
							}
							if (rawField instanceof IObservable<?>)
								observable = (IObservable<?>)rawField;
							else if (rawField!=null)
								observable= new ConstantObservable(rawField.getClass(), rawField);
						}
						
						if( observable != null) {											
							Object obj = observable.get();
							if(obj instanceof Integer) {
								mObservableChildLayoutID.add(observable, rowData);	
								layoutId = (Integer)obj;
							}
						}
					}
										
					View child = null;
					
					if( childItem != null ) {
						if( layoutId < 1 ) {
							TextView textView = new TextView(getContext());
							textView.setText("binding error - pos: " + pos + " has no layout - please check binding:itemPath or the layout id in viewmodel");
							textView.setTextColor(Color.RED);
							child = textView;
						} else {		
							Binder.InflateResult result = Binder.inflateView(getContext(), layoutId, tableRow, false);
							for(View view: result.processedViews){
								AttributeBinder.getInstance().bindView(getContext(), view, childItem);
							}
							child = result.rootView;	
							
							BindableLayoutContent content = new BindableLayoutContent();
							content.setInflateResult(result);
							list.add(content);
						}						
					}
					
					TableRow.LayoutParams params = null;
					
					// check if there is a colspan
					if( mRowChild.getColspanName() != null ) {									
						IObservable<?> observable = null;			
						ifo = new InnerFieldObservable(mRowChild.getColspanName());
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = null;
							try {
								rawField = Binder.getSyntaxResolver().getFieldForModel(mRowChild.getColspanName(), childItem);
							} catch (SyntaxResolveException e) {
								BindingLog.exception("BindableTableLayout.createRow", e);
							}
							if (rawField instanceof IObservable<?>)
								observable = (IObservable<?>)rawField;
							else if (rawField!=null)
								observable= new ConstantObservable(rawField.getClass(), rawField);
						}
						
						if( observable != null) {											
							Object obj = observable.get();
							if(obj instanceof Integer) {
								mObservableChildSpan.add(observable, rowData);
								colSpan = (Integer)obj;																	
							}
						}
						
					}					
	
					if( tableRow.getLayoutParams() != null ) {
						params = new TableRow.LayoutParams(tableRow.getLayoutParams());
					}
					
					if( child != null ) {
						// ViewGroup.MarginLayoutParams ctor doesn't honor the margins
						// so this is a workaround - we have to use the child - not the row
						
						TableRow.LayoutParams rowParams =  (TableRow.LayoutParams)child.getLayoutParams();	
						if( rowParams != null ) {
							ViewGroup.MarginLayoutParams margins = new ViewGroup.MarginLayoutParams(rowParams);
							margins.setMargins(rowParams.leftMargin, rowParams.topMargin, 
											   rowParams.rightMargin, rowParams.bottomMargin);
	
							params = new TableRow.LayoutParams(margins);
						} 
					}
														
					if( child != null ) {						
						if( params == null ) {
							tableRow.addView(child);
						} else {
							params.span = colSpan;
							params.column = col;
							tableRow.setLayoutParams(params);											
							tableRow.addView(child, params);
						}
					}

					col = col + colSpan;
				}		
			    			    
			}						
		}

		return tableRow;
	}
	
	private void unbindRow(List<?> deleteList) {
		synchronized(this){
			if( deleteList == null || deleteList.size() == 0 || mCurrentRowList == null)
				return;
			
			ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(mCurrentRowList.toArray()));
			
			for(Object row : deleteList){
				int pos = currentPositionList.indexOf(row);
				mCurrentRowList.remove(row);
				mObservableChildLayoutID.removeParent(row);
				mObservableChildSpan.removeParent(row);
				mObservableCollectionRowChildren.removeParent(row);
				currentPositionList.remove(row);
				if( pos > -1 && pos < getChildCount()) {
					unbindAndRemove(pos);
				}
			}
		}
	}

	private void unbindAndRemove(int pos) {
		List<BindableLayoutContent> list = mItemMapList.get(pos);
		
		if(list != null) {
			for(int i = 0; i < list.size(); i++) {
				BindableLayoutContent content = list.get(i);
			    if(content==null)
			    	continue;
			    Binder.unbindView(getContext(), content.getInflateResult());
			}
		}
		
		removeViewAt(pos);	
	}

}

