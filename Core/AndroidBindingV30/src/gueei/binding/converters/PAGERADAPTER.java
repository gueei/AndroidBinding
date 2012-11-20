package gueei.binding.converters;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.BindingLog;
import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.v30.viewAttributes.adapterView.viewPager.PagerAdapterObservable;
import gueei.binding.viewAttributes.templates.Layout;
import android.view.View;
import android.view.ViewGroup;

/**
 * PAGERADAPTER accepts unlimited number of DynamicObjects
 * It will return a PagerAdapter
 * 
 * @usage params
 * 
 * @arg params DynamicObject...
 * 
 * @item template gueei.binding.viewAttributes.templates.Layout
 * @item source ObservableCollection
 * @item @optional width Float
 * @item @optional widthField String When Set, ignore width
 * @item @optional title String
 * @item @optional titleField String When Set, ignore title
 * 
 * @return PagerAdapterObservable
 * 
 * @author andy
 *
 */
public class PAGERADAPTER extends Converter<PagerAdapterObservable> {
	public PAGERADAPTER(IObservable<?>[] dependents) {
	    super(PagerAdapterObservable.class, dependents);
    }

	@Override
    public PagerAdapterObservable calculateValue(Object... args) throws Exception {
		return new ObsPagerAdapter((DynamicObject)args[0]);
    }
	
	private class ObsPagerAdapter extends PagerAdapterObservable{
		@Override
        public CharSequence getPageTitle(int position) {
			if (dobj.observableExists("titleField")){
				String titleField = dobj.tryGetObservableValue("titleField", null);
				return Binder.getSyntaxResolver().tryEvaluateValue(getContext(), titleField, col.getItem(position), "");
			}else
				return dobj.tryGetObservableValue("title", "");
        }

		@Override
        public float getPageWidth(int position) {
			if (dobj.observableExists("widthField")){
				String widthField = dobj.tryGetObservableValue("widthField", null);
				return Binder.getSyntaxResolver().tryEvaluateValue(getContext(), widthField, col.getItem(position), 1.0f);
			}else
				return dobj.tryGetObservableValue("width", 1.0f);
        }

		private final DynamicObject dobj;
		private final IObservableCollection<?> col;
		
		public ObsPagerAdapter(DynamicObject obj){
			dobj = obj;
			try {
	            col = (IObservableCollection<?>)obj.getObservableByName("source").get();
            } catch (Exception e) {
            	BindingLog.exception("PAGERADAPTER.ObsPagerAdapter.Constructor", e);
            	throw new RuntimeException();
            }
		}
		
		@Override
        public Object instantiateItem(ViewGroup container, int position) {
			try {
				Layout layout;
	            layout = (Layout)dobj.getObservableByName("template").get();
	            InflateResult result = 
	            		Binder.inflateView(container.getContext(), layout.getDefaultLayoutId(), container, false);
	            View root = 
	            		Binder.bindView(container.getContext(), result, col.getItem(position));
	            container.addView(root);
	            return root;
            } catch (Exception e) {
	            BindingLog.exception("PAGERADAPTER", e);
	            return null;
            }
        }

		@Override
        public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
        }

		@Override
        public int getCount() {
			return col.size();
        }

		@Override
        public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
        }
	}
}
