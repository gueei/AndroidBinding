package gueei.binding.cursor;

import android.database.Cursor;

public class StringField extends CursorField<String> {

	public StringField(int columnIndex) {
		super(String.class, columnIndex);
	}

	public StringField(String columnName) {
		super(String.class, columnName);
	}
	
	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public String returnValue(Cursor cursor) {
		return cursor.getString(mColumnIndex);
	}

}
