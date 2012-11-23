package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.CursorCollection;
import gueei.binding.cursor.IdField;
import gueei.binding.cursor.RowModel;
import gueei.binding.cursor.StringField;
import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class ListViewWithCursorSource {
	public ListViewWithCursorSource(Activity activity) throws Exception{
		Cursor c = activity.getContentResolver().query(
				MediaStore.Audio.Media.INTERNAL_CONTENT_URI, 
				new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE}, 
				null, null, null);
		activity.startManagingCursor(c);
		MusicList.setCursor(c);
	}
	
	public final CursorCollection<MusicItem> MusicList = 
		new CursorCollection<MusicItem>(MusicItem.class);
	
	public final CursorCollection<MusicItem> MusicList2 = 
		new CursorCollection<MusicItem>(MusicItem.class);
	public final Observable<Object> ClickedItem = 
		new Observable<Object>(Object.class);
	
	public final Command ItemClickedCommand = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(view.getContext(),((MusicItem)ClickedItem.get()).Title.get(), Toast.LENGTH_SHORT)
				.show();
		}
	};

	public final Command ItemLongClickedCommand = new Command(){
		public void Invoke(View view, Object... args) {
			Toast.makeText(view.getContext(),"Long clicked: " + ((MusicItem)ClickedItem.get()).Title.get(), Toast.LENGTH_SHORT)
				.show();
		}
	};
	public static class MusicItem extends RowModel{
		public final IdField Id = new IdField(MediaStore.Audio.Media._ID);
		public final StringField Title = new StringField(MediaStore.Audio.Media.TITLE);
	}
}
