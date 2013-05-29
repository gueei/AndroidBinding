package gueei.binding.widgets.breadcrumbs;

import java.util.List;

import gueei.binding.widgets.BreadCrumbs;

public class BreadCrumbsSelectedEvent {
	public final int siblingPosition;
	public final Object breadCrumbNodeOld;
	public final Object breadCrumbNodeNew;	
	public boolean eventHandled = false;
	private BreadCrumbs parent;
	private BreadCrumbsItemWrapper nodeRootNew;

	public BreadCrumbsSelectedEvent(BreadCrumbs parent, int siblingPosition, Object breadCrumbNodeOld,  Object breadCrumbNodeNew, BreadCrumbsItemWrapper nodeRootNew) {
		this.parent = parent;
		this.siblingPosition = siblingPosition;
		this.breadCrumbNodeOld = breadCrumbNodeOld;
		this.breadCrumbNodeNew = breadCrumbNodeNew;
		this.nodeRootNew = nodeRootNew;
	}	
	
	public List<Object> getOldFullPath() {
		List<Object> result = parent.getCurrentPath();
		return result;
	}
	
	public List<Object>getNewFullPath() {
		List<Object> result = parent.getNodeWrapperPath(nodeRootNew);
		if(breadCrumbNodeNew !=null)
			result.add(breadCrumbNodeNew);
		return result;
	}
	
	
}
