package gueei.binding.markupDemoICS.widget;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

public class CodeView extends WebView implements IBindableView<CodeView> {

	public CodeView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	public CodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CodeView(Context context) {
		super(context);
	}

	@Override
	public ViewAttribute<? extends View, ?> createViewAttribute(
			String attributeId) {
		if ("resourceId".equals(attributeId)){
			return resId;
		}else if ("resourceType".equals(attributeId)){
			return resType;
		}else if ("title".equals(attributeId)){
			return title;
		}
		return null;
	}

	private final ResourceIdViewAttribute resId = 
			new ResourceIdViewAttribute(this, "resourceId");
	
	private final ResourceTypeViewAttribute resType = 
			new ResourceTypeViewAttribute(this, "resourceType");
	
	private final TitleViewAttribute title = 
			new TitleViewAttribute(this, "resourceType");
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		renderView();
		super.onDraw(canvas);
	}

	private void renderView(){
		if (isDirty){
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final Handler handler = new Handler();
        	(new Thread(){
				@Override
                public void run() {
			        try {
						XhtmlRendererFactory.getRenderer(resType.get()).highlight(title.get(), 
								getContext().getResources().openRawResource(resId.get()), 
								bos, "UTF-8", false);
						
						handler.post(new Runnable(){
							public void run() {
						        loadData(bos.toString(), "text/html", "UTF-8");
							}
						});
						
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
        	}).run();
        	
	        isDirty = false;
		}
	}
	
	private boolean isDirty = false;
	
	private class ResourceIdViewAttribute extends ViewAttribute<CodeView, Integer>{
		public ResourceIdViewAttribute(CodeView view,
				String attributeName) {
			super(Integer.class, view, attributeName);
		}

		private int mValue = 0;
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if (newValue!=null){
				mValue = Integer.parseInt(newValue.toString());
				isDirty = true;
				invalidate();
			}
		}

		@Override
		public Integer get() {
			return mValue;
		}
	}

	private class ResourceTypeViewAttribute extends ViewAttribute<CodeView, String>{
		public ResourceTypeViewAttribute(CodeView view,
				String attributeName) {
			super(String.class, view, attributeName);
		}

		private String mValue = "xml";
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if (newValue!=null){
				mValue = newValue.toString();
				isDirty = true;
				invalidate();
			}
		}

		@Override
		public String get() {
			return mValue;
		}
	}
	
	private class TitleViewAttribute extends ViewAttribute<CodeView, String>{
		public TitleViewAttribute(CodeView view,
				String attributeName) {
			super(String.class, view, attributeName);
		}

		private String mValue = "xml";
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if (newValue!=null){
				mValue = newValue.toString();
				isDirty = true;
				invalidate();
			}
		}

		@Override
		public String get() {
			return mValue;
		}
	}
}
