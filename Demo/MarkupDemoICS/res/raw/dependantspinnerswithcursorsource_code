package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.Observable;
import gueei.binding.collections.TrackedCursorCollection;
import gueei.binding.cursor.IdField;
import gueei.binding.cursor.IntegerField;
import gueei.binding.cursor.RowModel;
import gueei.binding.cursor.StringField;
import gueei.binding.observables.IntegerObservable;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * User: =ra=
 * Date: 01.08.11
 * Time: 15:58
 */
public class DependantSpinnersWithCursorSource {
	public final Observable<Object> MasterSelected = new Observable<Object>(Object.class);
	public final Observable<Object> DetSelected    = new Observable<Object>(Object.class);
	public final TrackedCursorCollection<ItemRowModel> MasterCursor;
	public final TrackedCursorCollection<ItemRowModel> DetCursor;
	public final IntegerObservable MasterPosition = new IntegerObservable(-1);
	public final IntegerObservable DetPosition    = new IntegerObservable(0);
	public final DependentObservable<Boolean> DetEnabled;
	private      int     x               = 0;
	public final Command AddItem         = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			ContentValues values = new ContentValues();
			values.put("Name", "Group X " + ((Integer) (++x)).toString());
			mContext.getContentResolver().insert(Uri.parse("content://com.gueei.demos/masters"), values);
		}
	};
	public final Command RemoveItem      = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			mContext.getContentResolver().delete(Uri.parse(
					"content://com.gueei.demos/master/" + ((ItemRowModel) MasterSelected.get()).Id.get().toString()),
												 null, null);
		}
	};
	public final Command ChangeDetCursor = new Command() {
		@Override
		public void Invoke(android.view.View view, Object... args) {
			Uri uri = Uri.parse(
					"content://com.gueei.demos/details/master/" + ((ItemRowModel) MasterSelected.get()).Id.get());
			Cursor cursor = mContext.getContentResolver().query(uri, sProjection, null, null, null);
			((Activity) mContext).startManagingCursor(cursor);
			DetPosition.set(0);
			DetCursor.setCursor(cursor);
		}
	};
	public TrackedCursorCollection<GroupRowModel> Groups;

	public DependantSpinnersWithCursorSource(Activity activity) {
		mContext = activity;
		Cursor masterCursor = mContext.getContentResolver()
									  .query(Uri.parse("content://com.gueei.demos/masters"), sProjection, null, null,
											 null);
		MasterCursor = new TrackedCursorCollection<ItemRowModel>(ItemRowModel.class);
		MasterCursor.setContentObserverTrackingUri(mContext, Uri.parse("content://com.gueei.demos/masters"), false);
		MasterCursor.setCursor(masterCursor);
		activity.startManagingCursor(masterCursor);
		DetCursor = new TrackedCursorCollection<ItemRowModel>(ItemRowModel.class);
		DetEnabled = new DependentObservable<Boolean>(Boolean.class, MasterPosition) {
			@Override
			public Boolean calculateValue(Object... args) throws Exception {
				return DetCursor.size() > 0;
			}
		};
		Groups = new TrackedCursorCollection<GroupRowModel>(GroupRowModel.class);
		Uri trackingUri = Uri.parse("content://com.gueei.demos/masters");
		Cursor groups = mContext.getContentResolver()
								.query(trackingUri, new String[]{"_ID", "Name", "detailsCount"}, null, null, null);
		activity.startManagingCursor(groups);
		Groups.setCursor(groups);
		Groups.setContentObserverTrackingUri(activity, trackingUri, false);
	}

	private final Context mContext;
	private static final String[] sProjection = new String[]{"_ID", "Name"};

	public static class ItemRowModel extends RowModel {
		public IdField     Id   = new IdField("_ID");
		public StringField Name = new StringField("Name");

		@Override public long getId(int defaultId) {
			return Id.get();
		}
	}

	public static class GroupRowModel extends RowModel {
		public IdField      Id            = new IdField("_ID");
		public StringField  Name          = new StringField("Name");
		public IntegerField SubItemsCount = new IntegerField("detailsCount");

		@Override public long getId(int defaultId) {
			return Id.get();
		}
	}
}
