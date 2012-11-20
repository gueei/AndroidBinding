package gueei.binding.cursor;

import gueei.binding.BindingLog;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 12:12
 */
public class RowModelFactory<T extends ICursorRowModel> implements IRowModelFactory<T> {
	private final Class<T> mRowModelType;

	public RowModelFactory(Class<T> rowModelType) {
		mRowModelType = rowModelType;
	}

	@Override
	public T createInstance() {
		try {
			return mRowModelType.newInstance();
		}
		catch (Exception e) {
			BindingLog.exception("RowModelFactory.createInstance", e);
			return null;
		}
	}
}
