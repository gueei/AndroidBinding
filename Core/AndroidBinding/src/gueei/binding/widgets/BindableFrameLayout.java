package gueei.binding.widgets;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import gueei.binding.Binder.InflateResult;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BindableFrameLayout extends FrameLayout implements IBindableView<BindableFrameLayout>{
	
	private int LayoutId = 0;
	private Object dataSource = null;
	private boolean updateEnabled = true;
	private InflateResult inflateResult = null;

	public BindableFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public BindableFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BindableFrameLayout(Context context) {
		super(context);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		LayoutId = 0;
		dataSource = null;
		inflateResult = null;
		super.onDetachedFromWindow();
	}

	private ViewAttribute<?, Object> LayoutIdViewAttribute =
		new ViewAttribute<BindableFrameLayout, Object>(Object.class, BindableFrameLayout.this, "LayoutId"){
			@Override				
			protected void doSetAttributeValue(Object newValue) {
				int newLayoutId = 0;
				
				if( newValue instanceof SingleTemplateLayout) {
					SingleTemplateLayout layout = (SingleTemplateLayout)newValue;
					newLayoutId = layout.getDefaultLayoutId();
				} else {
					if ((newValue != null)&&(newValue.toString().length()>0)){
						try {
							newLayoutId = Integer.parseInt(newValue.toString());
						} catch( Exception e ) {}
					}					 
				}					
				BindableFrameLayout.this.setLayoutId(newLayoutId);																							
			}
							
			@Override
			public Object get() {
				return LayoutId;
			}
	};
		
	
	private ViewAttribute<?, Object> DataSourceViewAttribute =
		new ViewAttribute<BindableFrameLayout, Object>( Object.class, BindableFrameLayout.this, "DataSource"){
			@Override
			protected void doSetAttributeValue(Object newValue) {					
				BindableFrameLayout.this.setDatasource(newValue);				
			}

			@Override
			public Object get() {
				return dataSource;
			}
	};
	
	private ViewAttribute<?, Command> OnLoadAttribute =
		new ViewAttribute<BindableFrameLayout, Command>( Command.class, BindableFrameLayout.this, "OnLoad"){
	
			@Override
			protected void doSetAttributeValue(Object newValue) {
				if (newValue instanceof Command){
					Command loadCmd = (Command)newValue;
					loadCmd.Invoke(BindableFrameLayout.this, new ILayoutLoadEvent() {							
						@Override
						public void setLayoutId(int layoutId) {
							BindableFrameLayout.this.setLayoutId(layoutId);								
						}							
						@Override
						public void setDatasource(Object... dataSource) {
							BindableFrameLayout.this.setDatasource(dataSource);								
						}
					});
				}
			}

			@Override
			public Command get() {
				return null;
			}				
	};	
	
	private ViewAttribute<BindableFrameLayout, Boolean> ItemUpdateEnabledAttribute =
			new ViewAttribute<BindableFrameLayout, Boolean>(Boolean.class, BindableFrameLayout.this, "UpdateEnabled"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					if( newValue == null ) {
						updateEnabled = true;
					}
					else if( newValue instanceof Boolean ) {
						Boolean value = (Boolean) newValue;
						updateEnabled = value; 
						if(updateEnabled) {
							BindableFrameLayout.this.invalidate();
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
	
	public ViewAttribute<?, ?> createViewAttribute(
			String attributeName) {
		if (attributeName.equals("layoutId")) return LayoutIdViewAttribute;
		if (attributeName.equals("dataSource")) return DataSourceViewAttribute;		
		if (attributeName.equals("onLoad")) return OnLoadAttribute;
		if (attributeName.equals("updateEnabled")) return ItemUpdateEnabledAttribute;
		
		return null;
	}

	protected void setDatasource(Object newValue) {
		if(dataSource!=null) 
			inflateResult = null; // inflate new 
		dataSource = newValue;
		rebind();
		refreshDrawableState();
	}

	// TODO: add layout caching
	// TODO: add a binding:useLayoutCaching="true" attribute
	// TODO: add a transition manager
	protected void setLayoutId(int layoutId) {
		if( LayoutId != layoutId ) {
			LayoutId = layoutId;
			inflateResult = null; // inflate new
			rebind();
			refreshDrawableState();
		}		
	}
	
	protected void rebind(){
		BindableFrameLayout.this.removeAllViews();
		if (LayoutId<=0) return;
		if(inflateResult==null||dataSource==null)
			inflateResult= Binder.inflateView(BindableFrameLayout.this.getContext(), LayoutId, BindableFrameLayout.this, false);							
		BindableFrameLayout.this.addView(inflateResult.rootView);
		if (dataSource==null){
			Binder.bindView(BindableFrameLayout.this.getContext(), inflateResult, null);
		}else if (dataSource.getClass().isArray()){
			Object[] sources = (Object[])dataSource;
			for(int i=0; i<sources.length; i++){
				Binder.bindView(BindableFrameLayout.this.getContext(), inflateResult, sources[i]);
			}
		}else{
			Binder.bindView(BindableFrameLayout.this.getContext(), inflateResult, dataSource);
		}
	}
}

