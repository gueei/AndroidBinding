package com.gueei.demos.markupDemo.viewModels;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Filter;
import gueei.binding.Observable;
import gueei.binding.collections.TrackedCursorCollection;
import gueei.binding.cursor.*;
import gueei.binding.observables.StringObservable;

/**
 * User: =ra=
 * Date: 09.08.11
 * Time: 9:46
 */
public class FilterCursorList {

	private final Context mContext;
	private final Uri mTrackingUri = Uri.parse("content://com.gueei.demos/masters");
	private Cursor mCursorSrc;

	public FilterCursorList(Activity activity) {
		mContext = activity;
		CursorSrc = new TrackedCursorCollection<FilteredCursorRowModel>(FilteredCursorRowModel.class);
		mCursorSrc = mContext.getContentResolver().query(mTrackingUri, new String[]{"_ID", "Name", "detailsCount"}, null, null, null);
		activity.startManagingCursor(mCursorSrc);
		CursorSrc.setCursor(mCursorSrc);
		CursorSrc.setContentObserverTrackingUri(mContext, mTrackingUri, false);
	}

	public final StringObservable FilterText = new StringObservable();
	public final TrackedCursorCollection<FilteredCursorRowModel> CursorSrc;
	public final Observable<Filter> CursorFilter = new Observable<Filter>(Filter.class, new Filter() {
		private Cursor filteredCursor;
		private static final char S_QUOTES = '\'';
		private static final char S_PERC_SIGN = '%'; //!! view model fails on percentage sign

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (constraint == null || constraint.length() == 0) {
				return null;
			}
			String filterString =
					"lower(Name) like " + S_QUOTES + S_PERC_SIGN + constraint.toString().toLowerCase() + S_PERC_SIGN + S_QUOTES;
			filteredCursor = mContext.getContentResolver()
									 .query(mTrackingUri, new String[]{"_ID", "Name", "detailsCount"}, filterString, null, null);
			((Activity) mContext).startManagingCursor(filteredCursor);
			FilterResults result = new FilterResults();
			result.count = filteredCursor.getCount();
			result.values = filteredCursor;
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (results == null) {
				CursorSrc.setCursor(mCursorSrc);
			}
			else {
				CursorSrc.setCursor(filteredCursor);
			}
			CursorSrc.setContentObserverTrackingUri(mContext, mTrackingUri, false);
		}
	});

	public static class FilteredCursorRowModel extends RowModel {

		public IdField      Id            = new IdField("_ID");
		public StringField  Name          = new StringField("Name");
		public IntegerField SubItemsCount = new IntegerField("detailsCount");

		@Override
		public long getId(int defaultId) {
			return Id.get();
		}
	}
}