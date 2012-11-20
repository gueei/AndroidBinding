package gueei.binding.cursor;

import android.database.Cursor;

public class ShortField extends CursorField<Short> {

	public ShortField(int columnIndex) {
		super(Short.class, columnIndex);
	}
	
	public ShortField(String columnName) {
		super(Short.class, columnName);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public Short returnValue(Cursor cursor) {
		return cursor.getShort(mColumnIndex);
	}

}
