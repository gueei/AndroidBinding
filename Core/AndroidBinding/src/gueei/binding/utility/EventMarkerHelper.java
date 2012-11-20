package gueei.binding.utility;

import gueei.binding.Binder;
import gueei.binding.viewAttributes.adapterView.listView.ItemViewEventMark;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: =ra=
 * Date: Dec 12, 2011
 * Time: 6:53:59 PM
 */

public class EventMarkerHelper {

	public static void mark(View view, ItemViewEventMark mark) {
		markView(view, mark);
		if (view instanceof ViewGroup) {
			markViewGroup((ViewGroup) view, mark);
		}
	}

	public static void removeMark(View view) {
		markView(view, null);
		if (view instanceof ViewGroup) {
			markViewGroup((ViewGroup) view, null);
		}
	}

	public static ItemViewEventMark getMark(View view) {
		ItemViewEventMark mark = Binder.getViewTag(view).get(ItemViewEventMark.class);
		return mark;
	}

	private static void markView(View view, ItemViewEventMark mark) {
		Binder.getViewTag(view).put(ItemViewEventMark.class, mark);
	}

	private static void markViewGroup(ViewGroup viewGroup, ItemViewEventMark mark) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			View view = viewGroup.getChildAt(i);
			markView(view, mark);
			if (view instanceof ViewGroup)
				markViewGroup((ViewGroup) view, mark);
		}
	}

}
