package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.widget.TextView;


public class TextLinesViewAttribute extends ViewAttribute<TextView, Integer> {
	public static enum Mode{
		MinLines, MaxLines
	}
	
	private Mode mode = Mode.MinLines;

	public TextLinesViewAttribute(TextView view, Mode mode) {
		super(Integer.class, view, getAttributeName(mode));
		this.mode = mode;
	}

	private static String getAttributeName(Mode mode) {
		if( mode == Mode.MinLines )
			return "minLines";
		else
			return "maxLines";
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			getView().setMaxLines(1);
			return;
		}
		if (newValue instanceof Integer){
			if( mode == Mode.MinLines )
				getView().setMinLines((Integer)newValue);
			else
				getView().setMaxLines((Integer)newValue);
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Integer get() {
		return null;
	}
}
