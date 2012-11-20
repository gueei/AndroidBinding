package gueei.binding.cursor;

import android.database.Cursor;

public class IntegerField extends CursorField<Integer> {

	public IntegerField(int columnIndex) {
		super(Integer.class, columnIndex);
	}
	
	public IntegerField(String columnName) {
		super(Integer.class, columnName);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public Integer returnValue(Cursor cursor) {
		return cursor.getInt(mColumnIndex);
	}

}
