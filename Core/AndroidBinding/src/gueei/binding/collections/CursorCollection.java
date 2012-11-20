package gueei.binding.collections;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionChangedEventArg.Action;
import gueei.binding.cursor.CursorField;
import gueei.binding.cursor.ICursorRowModel;
import gueei.binding.cursor.IRowModelFactory;
import gueei.binding.cursor.RowModelFactory;
import gueei.binding.utility.CacheHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import android.database.Cursor;
import android.database.DataSetObserver;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 11:58
 */
public class CursorCollection<T extends ICursorRowModel> extends ObservableCollection<T>{
	//
	public static interface ICursorCacheManager<ElType> {
		public void clear(); // remove all items from cache
		public void put(int key, ElType value);
		public ElType get(int key);
		public int getSize();
		
		/**
		 * Hint the Cache Manager the desire cache size needed, 
		 * Size is not compulsory and implementations can choose it's own strategy to use this
		 * Most probably this size is the total visible item on screen
		 * @param originator
		 * @param total
		 */
		public void hintCacheSize(Object originator, int total);
	}

	public CursorCollection(Class<T> rowModelType, ICursorCacheManager<T> cacheManager) {
		this(rowModelType, null, cacheManager, null);
	}
	
	public CursorCollection(Class<T> rowModelType) {
		this(rowModelType, null, null, null);
	}

	public CursorCollection(Class<T> rowModelType, Cursor cursor) {
		this(rowModelType, null, null, cursor);
	}

	public CursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory) {
		this(rowModelType, factory, null, null);
	}

	public CursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory, 
			ICursorCacheManager<T> cacheManager) {
		this(rowModelType, factory, cacheManager, null);
	}
	
	public CursorCollection(Class<T> rowModelType, IRowModelFactory<T> factory, 
			ICursorCacheManager<T> cacheManager, Cursor cursor) {
		mRowModelType = rowModelType;
		mFactory = factory == null ?
				new RowModelFactory<T>(rowModelType) : factory;
		mCursor = cursor;
		mCacheManager = cacheManager == null ? 
				new DefaultCursorCacheManager<T>() : cacheManager;
		initFieldDataFromModel();
		if (null != cursor) {
			cursor.registerDataSetObserver(mCursorDataSetObserver);
			mCursorDataSetObserver.onChanged();
		}

	}

	public void setCursor(Cursor cursor) {
		if (mCursor == cursor) {
			// cursor is the same, nothing to do
			return;
		}
		if (null != mCursor) {
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

	public Cursor getCursor() {
		return mCursor;
	}

	public T getItem(int position) {
		if (mCursor==null) return null;
		// Check the cache first
		T row = mCacheManager.get(position);
		if (null == row) { // no such position row cached
			mCursor.moveToPosition(position);
			row = createRowModel();
			mCacheManager.put(position, row);
		}
		return row;
	}

	public Class<T> getComponentType() {
		return mRowModelType;
	}

	public int size() {
		return mCursorRowsCount;
	}

	@Override
	public long getItemId(int position) {
		if (0 < mCursorRowsCount) {
			return getItem(position).getId(position);
		}
		return position;
	}

	public void onLoad(int position) {
	}

	public void requery() {
		// to be sure data is correct
		if (null != mCursor) {
			mCursor.requery(); // fires mCursorDataSetObserver.onChanged()
		}
		else {
			mCursorDataSetObserver.onChanged();// fire manually
		}
	}

	protected void reInitCacheCursorRowCount() {
		mCacheManager.clear();
		mCursorRowsCount = (null == mCursor) ? 0 : mCursor.getCount();
	}

	protected void initFieldDataFromModel() {
		for (Field f : mRowModelType.getFields()) {
			if (!CursorField.class.isAssignableFrom(f.getType())) {
				continue;
			}
			mCursorFields.add(f);
		}
	}

	protected T createRowModel() {
		T rowModel = mFactory.createInstance();
		for (Field f : mCursorFields) {
			try {
				((CursorField<?>) f.get(rowModel)).fillValue(mCursor);
			} catch (Exception ignored) {
			}
		}
		rowModel.onInitialize();
		return rowModel;
	}

	protected void finalize() throws Throwable {
		try {
			mCursorRowsCount = 0;
			if (null != mCursor) {
				mCursor.unregisterDataSetObserver(mCursorDataSetObserver);
				if (!mCursor.isClosed()) {
					mCursor.close();
				}
				mCursor = null;
			}
		} catch (Exception ignored) {
		} finally {
			super.finalize();
		}
	}

	protected final Class<T>         mRowModelType;
	protected final IRowModelFactory<T> mFactory;
	protected final ArrayList<Field> mCursorFields = new ArrayList<Field>();
	protected int                 mCursorRowsCount;
	protected Cursor              mCursor;
	// Hold the cached row models
	protected ICursorCacheManager<T> mCacheManager;
	protected final DataSetObserver mCursorDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			reInitCacheCursorRowCount();
			CollectionChangedEventArg e = new CollectionChangedEventArg(Action.Reset, (List<?>)null);
			notifyCollectionChanged(e);
		}
	};

	/**
	 * Default Implementation of CursorCacheManager.
	 * This will grow according to the number of widget accessing it, where
	 * 		size = (Sum of Visible Item on Each widget) * Extra Percentage
	 * and size is always larger than 10
	 * @author andy
	 *
	 * @param <T>
	 */
	public static class DefaultCursorCacheManager<T extends ICursorRowModel> implements ICursorCacheManager<T> {
		private CacheHashMap<Integer, T> mCache;
		
		private WeakHashMap<Object, Integer> cachingOriginators = 
				new WeakHashMap<Object, Integer>();

		private int mMinSize = 30;
		private float mExtra = 2.0f;

		public DefaultCursorCacheManager() {
			mCache = new CacheHashMap<Integer, T>(mMinSize);
		}

		public DefaultCursorCacheManager(int minSize, float extra) {
			mCache = new CacheHashMap<Integer, T>((int)(minSize * extra));
			mMinSize = minSize > 0 ? minSize : mMinSize;
			mExtra = extra > 1.0 ? extra: mExtra;
		}

		@Override
		public void clear() {
			mCache.clear();
		}

		@Override
		public void put(int key, T value) {
			mCache.put((Integer) key, value);
		}

		@Override
		public T get(int key) {
			return mCache.get((Integer) key);
		}

		@Override
		public int getSize() {
			return mCache.size();
		}

		@Override
		public void hintCacheSize(Object originator, int total) {
			cachingOriginators.put(originator, total);
			int size = 0;
			for(Integer t: cachingOriginators.values()){
				size += t;
			}
			if (size<mMinSize){
				size = mMinSize;
			}
			mCache.reSize((int)(size * mExtra));
		}
	}

	@Override
	public void setVisibleChildrenCount(Object setter, int total) {
		mCacheManager.hintCacheSize(setter, total);
	}

	@Override
	public boolean isNull() {
		return false;
	}
}