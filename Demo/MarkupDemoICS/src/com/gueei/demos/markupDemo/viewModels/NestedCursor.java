package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.collections.CursorCollection;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.cursor.IdField;
import gueei.binding.cursor.IntegerField;
import gueei.binding.cursor.RowModel;
import gueei.binding.cursor.StringField;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class NestedCursor {
	public static class ContactRowModel extends RowModel implements LazyLoadParent{
		public StringField Title = new StringField(ContactsContract.Contacts.DISPLAY_NAME);
		public IdField Id = new IdField(ContactsContract.Contacts._ID);
		public CursorCollection<EmailRowModel> Emails = 
			new CursorCollection<EmailRowModel>(EmailRowModel.class);
		
		public void onLoadChildren(Context context) {
			Cursor c = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
					new String[]{Id.get().toString()}, null);
			Emails.setCursor(c);
		}
	}
	
	public static class EmailRowModel extends RowModel{
		public StringField Address = 
			new StringField(ContactsContract.CommonDataKinds.Email.DATA);
		public IntegerField Type = new IntegerField(ContactsContract.CommonDataKinds.Email.TYPE);
	}
	
	public CursorCollection<ContactRowModel> Contacts = new 
			CursorCollection<ContactRowModel>(ContactRowModel.class);
	
	public NestedCursor(Activity activity){
		Cursor contact = activity.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, 
				new String[]{
						ContactsContract.Contacts._ID,
						ContactsContract.Contacts.DISPLAY_NAME
				}, null, null, null);
		activity.startManagingCursor(contact);
		Contacts.setCursor(contact);
	}
}
