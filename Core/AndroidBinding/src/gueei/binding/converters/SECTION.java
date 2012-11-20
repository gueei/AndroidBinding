package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.collections.SingletonAdapter;
import gueei.binding.viewAttributes.templates.Layout;
import android.widget.Adapter;

/**
 * Returns a Single Item Adapter (SingletonAdapter) for the given object. <br/>
 * This is most useful as declaring header/footer templates for list views, or as divider to divide list in sections
 * 
 * @usage object layout
 * @arg object Object 
 * @arg layout gueei.binding.viewAttributes.templates.Layout
 * 
 * @return Adapter
 * 
 */
public class SECTION extends Converter<Adapter> {
	public SECTION(IObservable<?>[] dependents) {
		super(Adapter.class, dependents);
	}

	@Override
	public Adapter calculateValue(Object... args) throws Exception {
		return new SingletonAdapter(this.getContext(), args[0], (Layout)args[1], (Layout)args[1]);
	}
}
