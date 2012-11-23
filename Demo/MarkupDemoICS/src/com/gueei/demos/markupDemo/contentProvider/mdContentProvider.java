package com.gueei.demos.markupDemo.contentProvider;
/**
 * User: =ra=
 * Date: 18.07.11
 * Time: 22:04
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class mdContentProvider extends ContentProvider {

	private static final String DATABASE_NAME                        = "markupDemo.db";
	private static final int    DATABASE_VERSION                     = 1;
	private static final int    MASTER_TABLE_GROUPS_COUNT            = 20;
	private static final int    DETAIL_RECORD_COUNT_FOR_MASTER_GROUP = 2;

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		private final Context sContext;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			sContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.beginTransaction();
			try {
				db.execSQL("CREATE TABLE master(_ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE " +
						   "NOT " + "NULL," + "Name TEXT," +
						   "detailsCount INTEGER NOT NULL DEFAULT 0)");
				db.execSQL("CREATE TABLE detail(_ID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE " +
						   "NOT " + "NULL," + "" + "" + "Name TEXT," +
						   "masterID INTEGER NOT NULL)");
				db.execSQL(
						"CREATE TRIGGER masterReCalcDelete AFTER DELETE ON detail FOR EACH ROW " +
						"BEGIN " +
						"UPDATE master Set detailsCount=(select count(*) from detail where " +
						"detail.masterID=OLD" + ".masterID)" + "WHERE master._ID=OLD.masterID;" +
						"END");
				db.execSQL(
						"CREATE TRIGGER masterReCalcInsert AFTER INSERT ON detail FOR EACH ROW " +
						"BEGIN " +
						"UPDATE master Set detailsCount=(select count(*) from detail where " +
						"detail.masterID=NEW" + ".masterID)" + "WHERE  master._ID=NEW.masterID;" +
						"DELETE from detail WHERE NOT EXISTS (SELECT 1 FROM master WHERE detail" +
						".masterID=master" + "._ID);END");
				db.execSQL(
						"CREATE TRIGGER masterReCalcUpdate AFTER UPDATE ON detail FOR EACH ROW " +
						"WHEN NEW" + ".masterID!=OLD.masterID " +
						"BEGIN UPDATE master Set detailsCount=(select count(*) from detail " +
						"where detail" + ".masterID=NEW.masterID)" +
						"WHERE master._ID=NEW.masterID;" +
						"UPDATE master Set detailsCount=(select count(*) from detail where " +
						"detail.masterID=OLD" + ".masterID)" +
						"WHERE  master._ID=OLD.masterID;END");
				db.execSQL("CREATE TRIGGER masterInsert  AFTER INSERT ON master FOR EACH ROW " +
						   "BEGIN " +
						   "UPDATE master Set detailsCount=(select count(*) from detail where " +
						   "detail.masterID=NEW" + "._ID)" + "WHERE  master._ID=NEW._ID;END");
				fillWithData(db);
				db.setTransactionSuccessful();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			finally {
				db.endTransaction();
			}
		}

		@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

		public void fillWithData(SQLiteDatabase db) {
			// master
			db.execSQL("delete from detail");
			String master =
					"insert or replace into master (Name,_ID) values ('Group [GRP]'," + "[GRP])";
			String detail = "insert into detail (Name,masterID) values ('Item [GRP].[ITEM]'," +
							"[GRP])";
			for (int group = 1; group <= MASTER_TABLE_GROUPS_COUNT; ++group) {
				String grpInsCmd = master.replace("[GRP]", ((Integer) group).toString());
				db.execSQL(grpInsCmd);
				for (int item = 1; item <= DETAIL_RECORD_COUNT_FOR_MASTER_GROUP; ++item) {
					String itmInsCmd = detail.replace("[GRP]", ((Integer) group).toString());
					itmInsCmd = itmInsCmd.replace("[ITEM]", ((Integer) item).toString());
					db.execSQL(itmInsCmd);
				}
			}
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection,
						final String[] selectionArgs, final String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String orderBy = null;
		String groupBy = null;
		String having = null;
		switch (sUriMatcher.match(uri)) {
		case DETAIL_ITEM:
			qb.appendWhere("detail._ID=" + uri.getPathSegments().get(1));
		case DETAIL_DIR:
			qb.setTables("detail");
			qb.setProjectionMap(sDetailProjectionMap);
			orderBy = DETAIL_SORT_ORDER;
			break;
		case DETAIL_MASTER_DIR:
			qb.appendWhere("detail.masterID=" + uri.getPathSegments().get(2));
			qb.setTables("detail");
			qb.setProjectionMap(sDetailProjectionMap);
			orderBy = DETAIL_SORT_ORDER;
			break;
		case MASTER_ITEM:
			qb.appendWhere("master._ID=" + uri.getPathSegments().get(1));
		case MASTER_DIR:
			qb.setTables("master");
			qb.setProjectionMap(sMasterProjectionMap);
			orderBy = MASTER_SORT_ORDER;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (!TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}
		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor =
				qb.query(db, projection, selection, selectionArgs, groupBy, having, orderBy);
		// Tell the cursor what uri to watch, so it knows when its source data changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override public String getType(final Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case DETAIL_ITEM:
			return CONTENT_TYPE_PART_ITEM + "detail";
		case DETAIL_DIR:
		case DETAIL_MASTER_DIR:
			return CONTENT_TYPE_PART_DIR + "detail";
		case MASTER_ITEM:
			return CONTENT_TYPE_PART_ITEM + "master";
		case MASTER_DIR:
			return CONTENT_TYPE_PART_DIR + "master";
		case RESTORE_ALL:
			return CONTENT_TYPE_PART_ITEM + "restore";
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override public Uri insert(final Uri uri, final ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		}
		else {
			values = new ContentValues();
		}
		if (values.containsKey("_ID") == true) {
			values.remove("_ID");
		}
		if (values.containsKey("Name") == false) {
			values.put("Name", "Wasn't set");
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = 0;
		switch (sUriMatcher.match(uri)) {
		case DETAIL_DIR:
			if (values.containsKey("masterID") == false) {
				throw new SQLException(
						"detail.masterID is not set. Failed to insert row into " + uri);
			}
			rowId = db.insert("detail", null, values);
			if (rowId > 0) {
				Uri xUri = Uri.parse(
						"content://" + AUTHORITY + "/detail/" + ((Long) rowId).toString());
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/detail/#"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/details"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/details/master/#"),
									  null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/master/#"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/masters"), null);
				return xUri;
			}
			else {
				throw new SQLException("Failed to insert row into " + uri);
			}
		case MASTER_DIR:
			if (values.containsKey("detailsCount") == true) {
				values.remove("detailsCount");
			}
			rowId = db.insert("master", null, values);
			if (rowId > 0) {
				Uri xUri = Uri.parse(
						"content://" + AUTHORITY + "/master/" + ((Long) rowId).toString());
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/master/#"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/masters"), null);
				return xUri;
			}
			else {
				throw new SQLException("Failed to insert row into " + uri);
			}
		case RESTORE_ALL:
			db.beginTransaction();
			try {
				mOpenHelper.fillWithData(db);
				db.setTransactionSuccessful();
				Uri xUri = Uri.parse("content://" + AUTHORITY + "/restore/1");
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/detail/#"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/details"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/details/master/#"),
									  null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/master/#"), null);
				getContext().getContentResolver()
						.notifyChange(Uri.parse("content://" + AUTHORITY + "/masters"), null);
				return xUri;
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			finally {
				db.endTransaction();
			}
		default:
			throw new IllegalArgumentException("Insert: Unknown URI " + uri);
		}
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		int count = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String where = null;
		switch (sUriMatcher.match(uri)) {
		case DETAIL_ITEM: {
			where = "detail._ID=" + uri.getPathSegments().get(1);
		}
		case DETAIL_DIR:
			if (null != where) {
				where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			}
			else {
				where = selection;
			}
			count = db.delete("detail", where, selectionArgs);
			break;
		case DETAIL_MASTER_DIR:
			where = "detail.masterID=" + uri.getPathSegments().get(2);
			where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			count = db.delete("detail", where, selectionArgs);
			break;
		case MASTER_ITEM: {
			where = "master._ID=" + uri.getPathSegments().get(1);
		}
		case MASTER_DIR:
			if (null != where) {
				where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			}
			else {
				where = selection;
			}
			count = db.delete("master", where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Delete: Unknown URI " + uri);
		}
		if (count > 0) {
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/detail/#"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/details"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/details/master/#"),
								  null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/master/#"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/masters"), null);
		}
		return count;
	}

	@Override
	public int update(final Uri uri, final ContentValues initialValues, final String selection,
					  final String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		}
		else {
			values = new ContentValues();
		}
		if (values.containsKey("_ID") == true) {
			throw new SQLException(
					"Value <_ID> shouldn't be changed. Failed to update row in " + uri);
		}
		if (values.containsKey("detailsCount") == true) {
			values.remove("detailsCount");
		}
		String where = null;
		switch (sUriMatcher.match(uri)) {
		case DETAIL_ITEM: {
			where = "detail._ID=" + uri.getPathSegments().get(1);
		}
		case DETAIL_DIR:
			if (null != where) {
				where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			}
			else {
				where = selection;
			}
			count = db.update("detail", values, where, selectionArgs);
			break;
		case DETAIL_MASTER_DIR:
			where = "detail.masterID=" + uri.getPathSegments().get(2);
			where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			count = db.update("detail", values, where, selectionArgs);
			break;
		case MASTER_ITEM: {
			where = "master._ID=" + uri.getPathSegments().get(1);
		}
		case MASTER_DIR:
			if (null != where) {
				where += (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			}
			else {
				where = selection;
			}
			count = db.update("master", values, where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Update: Unknown URI " + uri);
		}
		if (count > 0) {
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/detail/#"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/details"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/details/master/#"),
								  null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/master/#"), null);
			getContext().getContentResolver()
					.notifyChange(Uri.parse("content://" + AUTHORITY + "/masters"), null);
		}
		return count;
	}

	private static final String CONTENT_TYPE_PART_DIR  = "vnd.android.cursor.dir/com.gueei" +
														 ".demos.";
	private static final String CONTENT_TYPE_PART_ITEM =
			"vnd.android.cursor.item/com.gueei" + ".demos.";
	public static final  String AUTHORITY              = "com.gueei.demos";
	// URIs constants
	private static final int    DETAIL_ITEM            = 0;
	private static final int    DETAIL_DIR             = 1;
	private static final int    DETAIL_MASTER_DIR      = 2;
	private static final int    MASTER_ITEM            = 3;
	private static final int    MASTER_DIR             = 4;
	private static final int    RESTORE_ALL            = 5;
	// Sort Orders
	private static final String DETAIL_SORT_ORDER      = "detail.masterID ASC,detail._ID ASC";
	private static final String MASTER_SORT_ORDER      = "master._ID ASC";
	// Uri Matcher
	private static final UriMatcher sUriMatcher;
	// projections
	private static HashMap<String, String> sDetailProjectionMap = new HashMap<String, String>();
	private static HashMap<String, String> sMasterProjectionMap = new HashMap<String, String>();

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, "detail/#", DETAIL_ITEM);
		sUriMatcher.addURI(AUTHORITY, "details", DETAIL_DIR);
		sUriMatcher.addURI(AUTHORITY, "details/master/#", DETAIL_MASTER_DIR);
		sUriMatcher.addURI(AUTHORITY, "master/#", MASTER_ITEM);
		sUriMatcher.addURI(AUTHORITY, "masters", MASTER_DIR);
		sUriMatcher.addURI(AUTHORITY, "restore", RESTORE_ALL);
		//projection detail
		sDetailProjectionMap.put("_ID", "_ID");
		sDetailProjectionMap.put("Name", "Name");
		sDetailProjectionMap.put("masterID", "masterID");
		//projection master
		sMasterProjectionMap.put("_ID", "_ID");
		sMasterProjectionMap.put("Name", "Name");
		sMasterProjectionMap.put("detailsCount", "detailsCount");
	}
}
