package gueei.binding.v30.actionbar.attributes;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.actionbar.BindableActionBar;

public class Title extends ViewAttribute<BindableActionBar, CharSequence> {

	public Title(BindableActionBar view) {
		super(CharSequence.class, view, "title");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue==null){
			getHost().getActionBar().setTitle("");
			return;
		}
		if (newValue instanceof CharSequence){
			getHost().getActionBar().setTitle((CharSequence)newValue);
			return;
		}
		getHost().getActionBar().setTitle(newValue.toString());
	}

	@Override
	public CharSequence get() {
		return getHost().getActionBar().getTitle();
	}

	@Override
    protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
    }
}
