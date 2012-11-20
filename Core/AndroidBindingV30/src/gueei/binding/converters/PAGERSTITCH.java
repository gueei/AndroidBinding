package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.PagerAdapterObservable;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.PagerDataSetObserver;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * STITCH multiple pager adapters to one adapter for used in View pagers. <br/>
 * 
 * @usage PagerAdapter pagerAdapter ...
 * @arg pagerAdapter android.support.v4.view.PagerAdapter
 *  
 * @return android.widget.Adapter
 */
public class PAGERSTITCH extends Converter<PagerAdapterObservable>{
	public PAGERSTITCH(IObservable<?>[] dependents) {
		super(PagerAdapterObservable.class, dependents);
	}

	@Override
	public PagerAdapterObservable calculateValue(Object... args) throws Exception {
		CombinedPagerAdapter combine = new CombinedPagerAdapter();
		int length = args.length;
		ArrayList<PagerAdapter> adapters = new ArrayList<PagerAdapter>();
		for (int i=0; i<length; i++){
			if (args[i] instanceof PagerAdapter){
				adapters.add((PagerAdapter)args[i]);
			}
		}
		combine.addAdapter(adapters.toArray(new PagerAdapter[0]));
		
		return combine;
	}
	
	private static class CombinedPagerAdapter extends PagerAdapterObservable{
		private ArrayList<TranslatedAdapter> mTranslated = new ArrayList<TranslatedAdapter>();
		
		private class TranslatedAdapter{
			public int offset;
			public final PagerAdapter adapter;
			public TranslatedAdapter(PagerAdapter adapter){
				this.adapter =adapter;
			}
		}
		
		@Override
        public CharSequence getPageTitle(int position) {
			TranslatedAdapter ta = getAdapterAt(position);
	        return ta.adapter.getPageTitle(position - ta.offset);
        }

		@Override
        public float getPageWidth(int position) {
			TranslatedAdapter ta = getAdapterAt(position);
	        return ta.adapter.getPageWidth(position - ta.offset);
        }

		@Override
        public void setPrimaryItem(ViewGroup container, int position,
                Object object) {
			TranslatedAdapter ta = getAdapterAt(position);
	        ta.adapter.setPrimaryItem(container, position - ta.offset, object);
        }

		@Override
        public void startUpdate(ViewGroup container) {
			for(int i=0; i<mTranslated.size(); i++){
				mTranslated.get(i).adapter.startUpdate(container);
			}
        }

		private TranslatedAdapter getAdapterAt(int position){
			int length = mTranslated.size();
			TranslatedAdapter adapter; 
			for (int i=0; i<length; i++){
				adapter = mTranslated.get(i);
				if(position >= adapter.offset + adapter.adapter.getCount())
					continue;
				else
					return adapter;
			}
			return null;
		}
		
		private int mItemCount;
		
		private PagerDataSetObserver observer = new PagerDataSetObserver(){
			@Override
            public void onDataSetChanged() {
				calculateTranslation();
				notifyDataSetChanged();
            }
		};
		
		public void addAdapter(PagerAdapter[] adapters){
			for(int i=0; i<adapters.length; i++){
				mTranslated.add(new TranslatedAdapter(adapters[i]));
				if (adapters[i] instanceof PagerAdapterObservable){
					((PagerAdapterObservable)adapters[i]).setPagerDataSetObserver(observer);
				}
			}
			calculateTranslation();
			notifyDataSetChanged();
		}
		
		public void addAdapter(PagerAdapter adapter){
			mTranslated.add(new TranslatedAdapter(adapter));
			// No idea why set data set observer is internal
			// hope API is going to update this..
			if (adapter instanceof PagerAdapterObservable){
				((PagerAdapterObservable)adapter).setPagerDataSetObserver(observer);
			}

			calculateTranslation();
			notifyDataSetChanged();
		}
		
		public void remvoeAdapter(PagerAdapter adapter){
			for(int i=0; i<mTranslated.size(); i++){
				if (mTranslated.get(i).adapter.equals(adapter)){
					mTranslated.remove(i);
					if (adapter instanceof PagerAdapterObservable){
						((PagerAdapterObservable)adapter).setPagerDataSetObserver(null);
					}
					break;
				}
			}
			calculateTranslation();
			notifyDataSetChanged();
		}
		
		private void calculateTranslation(){
			int pos = 0;
			mItemCount = 0;
			for(TranslatedAdapter p: mTranslated){
				p.offset = pos;
				pos += p.adapter.getCount();
			}
			mItemCount = pos;
		}
		
		@Override
        public void destroyItem(ViewGroup container, int position, Object object) {
			TranslatedAdapter ta = getAdapterAt(position);
			ta.adapter.destroyItem(container, position - ta.offset, object);
        }

		@Override
        public Object instantiateItem(ViewGroup container, int position) {
			TranslatedAdapter ta = getAdapterAt(position);
			return ta.adapter.instantiateItem(container, position - ta.offset);
        }

		@Override
        public int getCount() {
			return mItemCount;
        }

		@Override
        public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
        }
	
	}
}