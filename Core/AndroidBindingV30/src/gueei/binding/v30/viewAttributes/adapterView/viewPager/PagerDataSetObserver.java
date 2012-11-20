package gueei.binding.v30.viewAttributes.adapterView.viewPager;

/**
 * Pager Adapter contains DataSetObserver, but it is marked "Internal". 
 * This is created to route nested Pager event. 
 * If you are creating your own PagerAdapter to use in Android Binding, it is 
 * recommended to have this interface and call onDataSetChanged in your PagerAdapter implementation
 * @author andy
 *
 */
public interface PagerDataSetObserver {
	public void onDataSetChanged();
}
