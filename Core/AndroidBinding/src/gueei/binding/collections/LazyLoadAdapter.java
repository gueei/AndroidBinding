package gueei.binding.collections;

import android.widget.AbsListView;

public interface LazyLoadAdapter {
	// Whether the load will start during scrolling/ after scrolling stopped
	public enum Mode{
		LoadWhenScrolling, LoadWhenStopped
	}
	
	// Indicates that this adapter is the root adapter attached to parent
	public void setRoot(AbsListView view);
	
	public void setMode(Mode mode);
	
	// First and last visible child of this adapter changed
	// this is suppose the parent will help translating the children position
	public void onVisibleChildrenChanged(int first, int total);
}
