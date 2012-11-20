package gueei.binding.v30.viewAttributes.adapterView.viewPager;

import android.support.v4.view.PagerAdapter;

/**
 * Abstract base class for PagerAdapters to properly work for Android Binding
 * This is because the PagerAdapter hides (internal) the dataset observer functions from public
 * 
 * 
 * @author andy
 *
 */
public abstract class PagerAdapterObservable extends PagerAdapter {
	protected PagerDataSetObserver pagerDataSetObserver;
	public void setPagerDataSetObserver(PagerDataSetObserver observer){
		pagerDataSetObserver = observer;
	}
	@Override
    public void notifyDataSetChanged() {
		if (pagerDataSetObserver!=null)
			pagerDataSetObserver.onDataSetChanged();
		
	    super.notifyDataSetChanged();
    }
}
