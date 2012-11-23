package com.gueei.demos.markupDemo.viewModels;
/**
 * User: =ra=
 * Date: 20.07.11
 * Time: 20:36
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import gueei.binding.Command;
import gueei.binding.collections.LazyLoadParent;
import gueei.binding.collections.TrackedCursorCollection;
import gueei.binding.cursor.*;

public class CursorContentUpdate {
	public TrackedCursorCollection<GroupsRowModel> Groups;

	public CursorContentUpdate(Activity activity) {
		mContext = activity;
		Groups = new TrackedCursorCollection<GroupsRowModel>(GroupsRowModel.class);
		
		Uri trackingUri = Uri.parse("content://com.gueei.demos/masters");
		Cursor groups = mContext.getContentResolver()
								.query(trackingUri, new String[]{"_ID", "Name", "detailsCount"}, null, null, null);
		activity.startManagingCursor(groups);
		Groups.setCursor(groups);
		Groups.setContentObserverTrackingUri(activity, trackingUri, false);
	}

	public final Command AddSubItem    = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			ContentValues values = new ContentValues();
			values.put("Name", "Child for Group 1");
			values.put("masterID", "1");
			mContext.getContentResolver().insert(Uri.parse("content://com.gueei.demos/details"), values);
		}
	};
	public final Command RemoveSubItem = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			mContext.getContentResolver().delete(Uri.parse("content://com.gueei.demos/details"),
												 "detail._ID=(SELECT min(_ID) FROM detail WHERE" + " masterID=1)",
												 null);
		}
	};
	public final Command RestoreData   = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			mContext.getContentResolver().insert(Uri.parse("content://com.gueei.demos/restore"), null);
		}
	};
	private final Context mContext;

	public static class GroupsRowModel extends RowModel implements LazyLoadParent {
		public StringField  Name          = new StringField("Name");
		public IdField      Id            = new IdField("_ID");
		public IntegerField SubItemsCount = new IntegerField("detailsCount");
		public TrackedCursorCollection<SubItemRowModel> SubItems;

		@Override public long getId(int defaultId) {
			return Id.get();
		}

		public void onLoadChildren(Context context) {
			SubItems = new TrackedCursorCollection<SubItemRowModel>(SubItemRowModel.class);
			Uri trackingUri = Uri.parse("content://com.gueei.demos/details");
			Cursor subItems = context.getContentResolver()
					.query(trackingUri, new String[]{"_ID", "Name", "masterID"}, "detail.masterID=?",
						   new String[]{Id.get().toString()}, null);
			((Activity) context).startManagingCursor(subItems);
			SubItems.setCursor(subItems);
			SubItems.setContentObserverTrackingUri(context, trackingUri, false);
		}
	}

	public static class SubItemRowModel extends RowModel {
		public IdField     Id    = new IdField("_ID");
		public StringField Name  = new StringField("Name");
		public LongField   Group = new LongField("MasterID");

		@Override public long getId(int defaultId) {
			return Id.get();
		}
	}
}
