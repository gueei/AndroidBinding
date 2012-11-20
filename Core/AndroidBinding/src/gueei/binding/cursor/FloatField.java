package gueei.binding.cursor;

import android.database.Cursor;

public class FloatField extends CursorField<Float> {

	public FloatField(int columnIndex) {
		super(Float.class, columnIndex);
	}

	public FloatField(String columnName) {
		super(Float.class, columnName);
	}
	@Override
	public Float returnValue(Cursor cursor) {
		return cursor.getFloat(mColumnIndex);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

}
