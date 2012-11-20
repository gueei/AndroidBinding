package gueei.binding.collections;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class LazyLoadRootAdapterHelper implements OnScrollListener {

	// Allow a few items which is close to the viewport to stay
	private int extraItems = 2;
	private final AbsListView mView;
	private final LazyLoadAdapter mAdapter;
	private LazyLoadAdapter.Mode mMode;
	private boolean busy;
	
	private int lastFirstVisibleItem = 0;
	private int lastVisibleItemCount = 0;
	// private int lastScrollState = OnScrollListener.SCROLL_STATE_IDLE; // unused

	public LazyLoadRootAdapterHelper(AbsListView view, LazyLoadAdapter adapter, LazyLoadAdapter.Mode mode) {
		mAdapter = adapter;
		view.setOnScrollListener(this);
		mMode = mode;
		mView = view;
	}

	public void setMode(LazyLoadAdapter.Mode mode) {
		mMode = mode;
	}

	
	/**
	 * Callback method to be invoked when the list or grid has been scrolled. This will be
	 * called after the scroll has completed
	 * @param view The view whose scroll state is being reported
	 * @param firstVisibleItem the index of the first visible cell (ignore if
	 *        visibleItemCount == 0)
	 * @param visibleItemCount the number of visible cells
	 * @param totalItemCount the number of items in the list adaptor
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (visibleItemCount > 0) {
			lastFirstVisibleItem = firstVisibleItem;
			lastVisibleItemCount = visibleItemCount;

			int extraFirst = firstVisibleItem - extraItems;
			if (extraFirst < 0) {
				extraFirst = 0;
			}

			int extraVisibleItemsCount = visibleItemCount + extraItems;
			if (extraVisibleItemsCount > totalItemCount) {
				extraVisibleItemsCount = totalItemCount;
			}

			if (!busy) {
				mAdapter.onVisibleChildrenChanged(extraFirst, extraVisibleItemsCount);
			}
		} else {
			lastFirstVisibleItem = 0;
			lastVisibleItemCount = 0;
		}
	}

	public boolean isBusy() {
		return busy;
	}

	

	public void onGetView(int position) {
		if (!busy) {
			mAdapter.onVisibleChildrenChanged(mView.getFirstVisiblePosition(), mView.getLastVisiblePosition() - mView.getFirstVisiblePosition());
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mMode.equals(LazyLoadAdapter.Mode.LoadWhenScrolling) || scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			busy = false;
			mAdapter.onVisibleChildrenChanged(lastFirstVisibleItem, lastVisibleItemCount);
		} else {
			busy = true;
		}
	}
}
