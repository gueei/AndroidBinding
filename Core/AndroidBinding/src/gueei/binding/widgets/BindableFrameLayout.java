package gueei.binding.widgets;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import gueei.binding.Binder.InflateResult;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


// TODO: add a transition manager

public class BindableFrameLayout extends FrameLayout implements IBindableView<BindableFrameLayout>, IBindableLayout{
	
	private BindableLayoutContent mBindableLayoutContent = new BindableLayoutContent();

	public BindableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BindableFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BindableFrameLayout(Context context) {
		super(context);
	}
	
	@Override
	public void unbind () {	
		unbind(true);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		unbind(false);
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
				return mBindableLayoutContent.getLayoutId();
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
				return mBindableLayoutContent.getDataSource();
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
		
	public ViewAttribute<?, ?> createViewAttribute(
			String attributeName) {
		if (attributeName.equals("layoutId")) return LayoutIdViewAttribute;
		if (attributeName.equals("dataSource")) return DataSourceViewAttribute;		
		if (attributeName.equals("onLoad")) return OnLoadAttribute;
		
		return null;
	}

	protected void setDatasource(Object dataSource) {	
		synchronized(this){
			if(dataSource != null && mBindableLayoutContent.getDataSource() == dataSource)
				return;
			
			int layoutId = mBindableLayoutContent.getLayoutId();		
			unbind(true);		
			mBindableLayoutContent.setDataSource(dataSource);
			mBindableLayoutContent.setLayoutId(layoutId);
					
			if(dataSource == null)
				return;
			
			bind(true);	
		}
	}

	protected void setLayoutId(int layoutId) {
		synchronized(this){
			if( mBindableLayoutContent.getLayoutId() == layoutId )
				return;
			
			Object dataSource = mBindableLayoutContent.getDataSource();
			unbind(true);
			mBindableLayoutContent.setDataSource(dataSource);
			mBindableLayoutContent.setLayoutId(layoutId);
			bind(true);
		}
	}
	
	protected void bind(boolean addView){
		if (mBindableLayoutContent.getLayoutId()<=0)
			return;
		
		if(mBindableLayoutContent.getInflateResult()==null||mBindableLayoutContent.getDataSource()==null) {
			InflateResult result = Binder.inflateView(getContext(), mBindableLayoutContent.getLayoutId(), this, false);
			mBindableLayoutContent.setInflateResult(result);
		}
		
		if(addView)
			addView(mBindableLayoutContent.getInflateResult().rootView);
		
		if (mBindableLayoutContent.getDataSource()==null){
			Binder.bindView(getContext(), mBindableLayoutContent.getInflateResult(), null);
		}else if (mBindableLayoutContent.getDataSource().getClass().isArray()){
			Object[] sources = (Object[])mBindableLayoutContent.getDataSource();
			for(int i=0; i<sources.length; i++){
				Binder.bindView(getContext(), mBindableLayoutContent.getInflateResult(), sources[i]);
			}
		}else{
			Binder.bindView(getContext(), mBindableLayoutContent.getInflateResult(), mBindableLayoutContent.getDataSource());
		}

		refreshDrawableState();
	}

	protected void unbind(boolean removeAllViews){
		InflateResult inflateResult = mBindableLayoutContent.getInflateResult();
		Binder.unbindView(getContext(), inflateResult);
		mBindableLayoutContent.clear();		
		if(removeAllViews)			
			removeAllViews();
	}
}

