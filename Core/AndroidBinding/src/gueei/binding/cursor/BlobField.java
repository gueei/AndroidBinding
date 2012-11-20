package gueei.binding.cursor;

import android.database.Cursor;

public class BlobField extends CursorField<byte[]> {
	public BlobField(int columnIndex) {
		super(byte[].class, columnIndex);
	}
	
	public BlobField(String columName) {
		super(byte[].class, columName);
	}

	@Override
	public void saveValue(Cursor cursor) {
	}

	@Override
	public byte[] returnValue(Cursor cursor) {
		return cursor.getBlob(mColumnIndex);
	}
}
