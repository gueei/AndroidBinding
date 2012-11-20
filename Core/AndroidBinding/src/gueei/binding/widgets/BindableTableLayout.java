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
import gueei.binding.utility.WeakList;
import gueei.binding.viewAttributes.templates.LayoutRowChild;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BindableTableLayout extends TableLayout implements IBindableView<BindableTableLayout> {
	private WeakList<Object> currentRowList = null;
	private CollectionObserver collectionObserver = null;
	
	private ObservableCollection<Object> rowList = null;	
	private LayoutRowChild rowChild = null;	
	private boolean updateEnabled = true;	
	
	private Observer observer = new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if( initiators == null || currentRowList == null)
				return;
			int pos = -1;
			
			for(Object i : initiators) {
				pos = currentRowList.indexOf(i);
				if( pos > 0)
					break;
			}
			if( pos < 0)
				return;
			Object parent = currentRowList.get(pos);
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(parent);
			removeRows(list);						
			insertRow(pos, parent);	 
		}
	};
	
	private ObservableMultiplexer<Object> observableChildLayoutID = new ObservableMultiplexer<Object>(observer);
	private ObservableMultiplexer<Object> observableChildSpan = new ObservableMultiplexer<Object>(observer);	
	private ObservableCollectionMultiplexer<Object> observableCollectionRowChildren = new ObservableCollectionMultiplexer<Object>(observer);		
	
	public BindableTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BindableTableLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if(rowList != null) {
			rowList.unsubscribe(collectionObserver);
			collectionObserver = null;
		}
		
		rowList = null;
		observableChildLayoutID.clear();
		observableChildSpan.clear();
		observableCollectionRowChildren.clear();							
		currentRowList= null;
			
		super.onDetachedFromWindow();
	}
	
	private void createItemSourceList(ObservableCollection<Object> newRowList) {		
		if( rowList != null && collectionObserver != null)
			rowList.unsubscribe(collectionObserver);
				
		collectionObserver = null;
		rowList = newRowList;
		
		if(newRowList==null)
			return;

		currentRowList = null;	
		collectionObserver = new CollectionObserver() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCollectionChanged(
					IObservableCollection<?> collection,
					CollectionChangedEventArg args,
					Collection<Object> initiators) {
				rowListChanged(args, (ObservableCollection<Object>)collection);
			}			
		};
		
		rowList.subscribe(collectionObserver);
		newRowList(rowList);
	}	

	private void newRowList(ObservableCollection<Object> rows) {		
		this.removeAllViews();	
		
		observableChildLayoutID.clear();
		observableChildSpan.clear();
		observableCollectionRowChildren.clear();		
						
		if( rows == null) {
			currentRowList= null;
			return;
		}
		
		currentRowList = new WeakList<Object>();
				
		for( int pos=0; pos < rows.size(); pos ++ ) {
			Object item = rows.getItem(pos);
			insertRow(pos, item);
		}
		
		for( int pos=0; pos < rows.size(); pos ++ ) {
			Object item = rows.getItem(pos);
			currentRowList.add(item);
		}			
	}	
	
	private void rowListChanged(CollectionChangedEventArg e, ObservableCollection<Object> rows) {
		if( e == null)
			return;
		
		int pos=-1;
		switch( e.getAction()) {
			case Add:
				pos = e.getNewStartingIndex();
				for(Object row : e.getNewItems()) {
					insertRow(pos, row);
					pos++;
				}
				break;
			case Remove:
				removeRows(e.getOldItems());	
				break;
			case Replace:
				removeRows(e.getOldItems());	
				pos = e.getNewStartingIndex();
				if( pos < 0)
					pos=0;				
				for(Object item : e.getNewItems()) {
					insertRow(pos, item);
					pos++;
				}
				break;
			case Reset:
				newRowList(rows);
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
			currentRowList.add(item);
		}					
	}	

	private ViewAttribute<BindableTableLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ArrayListObservable<?> ))
						return;
					rowList = (ArrayListObservable<Object>)newValue;
					
					if( rowChild != null )
						createItemSourceList(rowList);
				}

				@Override
				public Object get() {
					return rowList;
				}				
	};	
					
	private ViewAttribute<BindableTableLayout, Object> RowChildAttribute =
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "RowChild"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					rowChild = null;
					if( newValue instanceof LayoutRowChild ) {
						rowChild = (LayoutRowChild) newValue;
						if( rowList != null )
							createItemSourceList(rowList);
					}
				}

				@Override
				public Object get() {
					return rowChild;
				}
	};	
	
	private ViewAttribute<BindableTableLayout, Boolean> ItemUpdateEnabledAttribute =
			new ViewAttribute<BindableTableLayout, Boolean>(Boolean.class, BindableTableLayout.this, "UpdateEnabled"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					if( newValue == null ) {
						updateEnabled = true;
					}
					else if( newValue instanceof Boolean ) {
						Boolean value = (Boolean) newValue;
						updateEnabled = value; 
						if(updateEnabled) {
							BindableTableLayout.this.invalidate();
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
	public ViewAttribute<BindableTableLayout, ?> createViewAttribute(String attributeId) {	
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("rowChild")) return RowChildAttribute;
		if (attributeId.equals("updateEnabled")) return ItemUpdateEnabledAttribute;		
		return null;
	}	
	
	private void insertRow(int pos, Object row) {		
		if( rowChild == null )
			return;
		
		IObservable<?> childDataSource = null;			
		InnerFieldObservable<?> ifo = new InnerFieldObservable<Object>(rowChild.getChildDataSource());
		if (ifo.createNodes(row)) {
			childDataSource = ifo;	
		} else {			
			Object rawField = null;
			try {
				rawField = Binder.getSyntaxResolver().getFieldForModel(rowChild.getChildDataSource(), row);
			} catch (SyntaxResolveException e) {
				BindingLog.exception("BindableTableLayout.insertRow", e);
			}
			if (rawField instanceof IObservable<?>)
				childDataSource = (IObservable<?>)rawField;
		}				
		
		TableRow trow = createRow(childDataSource, pos, row);
				 			
		currentRowList.add(pos, row);
		this.addView(trow,pos);		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableRow createRow(IObservable<?> childDataSource, int pos, Object row) {
		TableRow tableRow = new TableRow(getContext());		
		InnerFieldObservable<?> ifo = null;
				
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
				observableCollectionRowChildren.add(childItems,row);
				
				int col = 0;
			    for( Object childItem : childItems) {					
					int colSpan = 1;
					int layoutId = rowChild.getLayoutId();		
					if( layoutId < 1 && rowChild.getLayoutName() != null ) {									
						IObservable<?> observable = null;
						ifo = new InnerFieldObservable<Object>(rowChild.getLayoutName() );
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = null;
							try {
								rawField = Binder.getSyntaxResolver().getFieldForModel(rowChild.getLayoutName(), childItem);
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
								observableChildLayoutID.add(observable, row);	
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
						}						
					}
					
					TableRow.LayoutParams params = null;
					
					// check if there is a colspan
					if( rowChild.getColspanName() != null ) {									
						IObservable<?> observable = null;			
						ifo = new InnerFieldObservable(rowChild.getColspanName());
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = null;
							try {
								rawField = Binder.getSyntaxResolver().getFieldForModel(rowChild.getColspanName(), childItem);
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
								observableChildSpan.add(observable, row);
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
	
	private void removeRows(List<?> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || currentRowList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(currentRowList.toArray()));
		
		for(Object row : deleteList){
			int pos = currentPositionList.indexOf(row);
			currentRowList.remove(row);
			observableChildLayoutID.removeParent(row);
			observableChildSpan.removeParent(row);
			observableCollectionRowChildren.removeParent(row);
			currentPositionList.remove(row);
			if( pos > -1 && pos < this.getChildCount())
				this.removeViewAt(pos);	
		}
	}

}

