package gueei.binding.v30.viewAttributes.adapterView.viewPager;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Adapter;

public class AdapterViewAttribute extends ViewAttribute<ViewPager, PagerAdapter> {
		
		public  AdapterViewAttribute 
			(ViewPager view) {
			super(PagerAdapter.class,view, "adapter");
		}
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if(getView()==null) return;
			if (newValue instanceof PagerAdapter){
				getView().setAdapter((PagerAdapter)newValue);
				return;
			}
			
			if (newValue instanceof Adapter){
				getView().setAdapter(new PagerAdapterBridge((Adapter)newValue));
				return;
			}
		}

		@Override
		public PagerAdapter get() {
			if(getView()==null) return null;
			return getView().getAdapter();
		}
		
		@Override
        protected BindingType AcceptThisTypeAs(Class<?> type) {
			if (Adapter.class.isAssignableFrom(type)) return BindingType.OneWay;
			return super.AcceptThisTypeAs(type);
        }

		private class PagerAdapterBridge extends PagerAdapter{

			private final Adapter mAdapter;
			public PagerAdapterBridge(Adapter adapter){
				mAdapter = adapter;
			}
			
			@Override
	        public int getCount() {
		        return mAdapter.getCount();
	        }

			@Override
	        public boolean isViewFromObject(View arg0, Object arg1) {
		        return arg0.equals(arg1);
	        }
			
			public Object instantiateItem(View collection, int position){
				android.support.v4.view.ViewPager viewPager = (android.support.v4.view.ViewPager)collection;
				View view = mAdapter.getView(position, null, viewPager);
				viewPager.addView(view);
				
				return view;
			}

			@Override
            public void destroyItem(View container, int position, Object object) {
				android.support.v4.view.ViewPager viewPager = (android.support.v4.view.ViewPager)container;
				viewPager.removeView((View)object);
	            return;
            }
		}
}
