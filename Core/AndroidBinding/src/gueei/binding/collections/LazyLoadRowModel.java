package gueei.binding.collections;

import gueei.binding.IObservableCollection;

public abstract class LazyLoadRowModel implements ILazyLoadRowModel {
	private boolean displaying = false;
	
	@Override public void display(IObservableCollection<?> collection, int index) {
		if (displaying) return;
		// if (!mapped) return;
		displaying = true;
		onDisplay(collection, index);
	}

	@Override public void hide(IObservableCollection<?> collection, int index) {
		if (!displaying) return;
		displaying = false;
		onHide(collection, index);
	}

	public abstract void onDisplay(IObservableCollection<?> collection, int index);

	public abstract void onHide(IObservableCollection<?> collection, int index);

	protected boolean mapped = false;
	
	@Override
    public void setMapped(boolean mapped) {
		this.mapped = mapped;
    }

	@Override
    public boolean isMapped() {
	    return this.mapped;
    }
}
