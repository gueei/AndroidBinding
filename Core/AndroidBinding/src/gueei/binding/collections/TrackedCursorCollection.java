package gueei.binding.collections;

import gueei.binding.cursor.ICursorRowModel;
import gueei.binding.cursor.IRowModelFactory;
import java.lang.ref.WeakReference;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 12:05
 */
public class TrackedCursorCollection<T extends ICursorRowModel> extends CursorCollection<T> {

	public TrackedCursorCollection(Class<T> rowModelType, gueei.binding.collections.CursorCollection.ICursorCacheManager<T> cacheManager) {
		super(rowModelType, cacheManager);
	}

	public TrackedCursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory,
			gueei.binding.collections.CursorCollection.ICursorCacheManager<T> cacheManager, Cursor cursor) {
		super(rowModelType, factory, cacheManager, cursor);
	}

	public TrackedCursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory,
			gueei.binding.collections.CursorCollection.ICursorCacheManager<T> cacheManager) {
		super(rowModelType, factory, cacheManager);
	}

	public TrackedCursorCollection(Class<T> rowModelType, Cursor cursor) {
		super(rowModelType, cursor);
	}

	public TrackedCursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory) {
		super(rowModelType, factory);
	}

	public TrackedCursorCollection(Class<T> rowModelType) {
		super(rowModelType);
	}

	@Override
	public void setCursor(Cursor cursor) {
		if (mCursor == cursor) {
			// cursor is the same, nothing to do
			return;
		}
		if (null != mCursor) {
			// unregister content observer related to previous cursor
			if (null != mCursorContentObserver) {
				mCursorContentObserver.unregisterUri();
			}
			// unregister previous cursor listener
			mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
		}
		mCursor = cursor;
		if (null != mCursor) {
			// register listener to new cursor
			mCursor.registerDataSetObserver(mCursorDataSetObserver);
		}
		mCursorDataSetObserver.onChanged(); // imitate changes
	}

	/**
	 * There are no obvious methods like AddItem(s), RemoveItem(s), etc for Сursor
	 * data could be changed anywhere and anytime out from model
	 * sometimes we need to know about data changes
	 * Not sure if we have to track more than one uri (!!!)
	 *
	 * @param context              Context to register for data changes
	 * @param uri                  The URI to watch for changes. This can be a specific row URI, or a base URI
	 *                             for a whole class of content.
	 * @param notifyForDescendants If <code>true</code> changes to URIs beginning with <code>uri</code>
	 *                             will also cause notifications to be sent. If <code>false</code> only changes to
	 *                             the exact URI
	 *                             specified by <em>uri</em> will cause notifications to be sent. If true,
	 *                             than any URI values
	 *                             at or below the specified URI will also trigger a match.
	 */
	public void setContentObserverTrackingUri(Context context, Uri uri, boolean notifyForDescendants) {
		if (null != mCursorContentObserver) {
			mCursorContentObserver.unregisterUri();
			mCursorContentObserver = null;
		}
		if (null != uri) {
			mCursorContentObserver = new CollectionContentObserver(new Handler());
			mCursorContentObserver.registerUri(context, uri, notifyForDescendants);
		}
	}

	public void setContentObserverTrackingUri(Context context, Uri uri) {
		setContentObserverTrackingUri(context, uri, false);
	}

	protected class CollectionContentObserver extends ContentObserver {

		public CollectionContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			if (mCursor != null) {
				mCursor.requery(); // notifyCollectionChanged will be fired by super.mCursorDataSetObserver.onChange
			}
		}

		public void registerUri(Context context, Uri uri, boolean notifyForDescendants) {
			unregisterUri();
			mContextWeakReference = new WeakReference<Context>(context);
			if (null != context) {
				context.getContentResolver().registerContentObserver(uri, notifyForDescendants, this);
			}
		}

		public void unregisterUri() {
			Context context = (null != mContextWeakReference) ? mContextWeakReference.get() : null;
			if (null != context) {
				context.getContentResolver().unregisterContentObserver(this);
				mContextWeakReference = null;
			}
		}

		protected WeakReference<Context> mContextWeakReference = null;
	}

	private CollectionContentObserver mCursorContentObserver;

	protected void finalize() throws Throwable {
		try {
			mCursorContentObserver.unregisterUri();
			mCursorContentObserver = null;
		} catch (Exception ignored) {
		} finally {
			super.finalize();
		}
	}
}
