package gueei.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectionChangedEventArg {
	public enum Action {
		Add,		// one or more items
		Remove,		// one or more items
		Replace,	// one or more items
		Move,		// one or more items
		Reset		//
	};
	
	private Action mAction = Action.Reset;
	private List<?> mNewItems = null;
	private List<?> mOldItems = null;
	private int mNewStartingIndex = -1;
	private int mOldStartingIndex = -1;
	
	/**
	 * @param action must be Action.Reset
	 */
	public CollectionChangedEventArg(Action action) {
		if (action != Action.Reset)
			throw new IllegalArgumentException("ctor is for Action.Reset only");
		this.initAdd(action, null, -1);
	}	
	
	/**
	 * single item changed
	 * @param action must be Action.Add, Action.Reset or Action.Remove
	 * @param item must be empty for Action.Reset
	 */
	public CollectionChangedEventArg(Action action, Object item) {
		if (action != Action.Add && action != Action.Remove && action != Action.Reset)
			throw new IllegalArgumentException("ctor is for Action.Add or Action.Remove or Action.Reset only");
		
		if (action != Action.Reset) {
			this.initAddOrRemove(action, Arrays.asList(new Object[]{item}), -1);
			return;
		}
		if (item != null)
			throw new IllegalArgumentException("Action.Reset item must be null");
		this.initAdd(action, null, -1);
	}
	
	
	/**
	 * multi item changed
	 * @param action must be Action.Add, Action.Reset or Action.Remove
	 * @param changes must be empty for Action.Reset
	 */
	public CollectionChangedEventArg(Action action, List<?> changes) {
		if (action != Action.Add && action != Action.Remove && action != Action.Reset)
			throw new IllegalArgumentException("ctor is for Action.Add or Action.Remove or Action.Reset only");
		
		if (action == Action.Reset) {
			if (changes != null)
				throw new IllegalArgumentException("Action.Reset changes must be null");
			this.initAdd(action, null, -1);
			return;
		} else {
			if (changes == null)
				throw new IllegalArgumentException("changes");
			this.initAddOrRemove(action, changes, -1);
			return;
		}
	}

	/**
	 * multi item changed
	 * @param action must be Action.Add, Action.Reset or Action.Remove
	 * @param changes must be empty for Action.Reset
	 * @param startingIndex
	 */
	public CollectionChangedEventArg(Action action, List<?> changes, int startingIndex) {
		if (action != Action.Add && action != Action.Remove && action != Action.Reset)
			throw new IllegalArgumentException("ctor is for Action.Add or Action.Remove or Action.Reset only");
		
		if (action == Action.Reset) {
			if (changes != null)
				throw new IllegalArgumentException("Action.Reset changes must be null");
			if (startingIndex != -1)
				throw new IllegalArgumentException("Action.Reset requires index = -1");
			this.initAdd(action, null, -1);
			return;
		} else {
			if (changes == null)
				throw new IllegalArgumentException("changes");
			if (startingIndex < -1)
				throw new IllegalArgumentException("startindex must be not negative");
			this.initAddOrRemove(action, changes, startingIndex);
			return;
		}
	}	
	
	/**
	 * replace change
	 * @param action
	 * @param newItem
	 * @param oldItem
	 */
	public CollectionChangedEventArg(Action action, Object newItem, Object oldItem) {
		if (action != Action.Replace)
			throw new IllegalArgumentException("ctor is for Action.Replace only");
		this.initMoveOrReplace(action, Arrays.asList(new Object[]{newItem}), Arrays.asList(new Object[]{oldItem}), -1, -1);
	}	
	
	/**
	 * replace change
	 * @param action
	 * @param newItem
	 * @param oldItem
	 * @param index
	 */
	public CollectionChangedEventArg(Action action, Object newItem, Object oldItem, int index) {
		if (action != Action.Replace)
			throw new IllegalArgumentException("ctor is for Action.Replace only");
		this.initMoveOrReplace(action, Arrays.asList(new Object[]{newItem}), Arrays.asList(new Object[]{oldItem}), index, index);
	}
	
	/**
	 * replace change
	 * @param action
	 * @param newItems
	 * @param oldItems
	 */
	public CollectionChangedEventArg(Action action, List<?> newItems, List<?> oldItems) {
		if (action != Action.Replace)
			throw new IllegalArgumentException("ctor is for Action.Replace only");		
		if (newItems == null)
			throw new IllegalArgumentException("newItems");
		if (oldItems == null)
			throw new IllegalArgumentException("newItems");
		this.initMoveOrReplace(action, newItems, oldItems, -1, -1);
	}
	
	/**
	 * replace change
	 * @param action
	 * @param newItems
	 * @param oldItems 
	 * @param startingIndex
	 */
	public CollectionChangedEventArg(Action action, List<?> newItems, List<?> oldItems, int startingIndex) {
		if (action != Action.Replace)
			throw new IllegalArgumentException("ctor is for Action.Replace only");
		if (newItems == null)
			throw new IllegalArgumentException("newItems");
		if (oldItems == null)
			throw new IllegalArgumentException("newItems");
		this.initMoveOrReplace(action, newItems, oldItems, startingIndex, startingIndex);
	}
	
	
	/**
	 * move single item
	 * @param action
	 * @param changedItem
	 * @param index
	 * @param oldIndex
	 */
	public CollectionChangedEventArg(Action action, Object changedItem, int index, int oldIndex) {
		if (action != Action.Move)
			throw new IllegalArgumentException("ctor is for Action.Move only");
		
		if (index < 0)
			throw new IllegalArgumentException("index can't be negative");

		List<?> items = Arrays.asList(new Object[]{changedItem});
		this.initMoveOrReplace(action, items, items, index, oldIndex);
	}

	/**
	 * move multi items
	 * @param action
	 * @param changedItems
	 * @param index
	 * @param oldIndex
	 */
	public CollectionChangedEventArg(Action action, List<?> changedItems, int index, int oldIndex) {
		if (action != Action.Move)
			throw new IllegalArgumentException("ctor is for Action.Move only");
		if (index < 0)
			throw new IllegalArgumentException("index can't be negative");
		this.initMoveOrReplace(action, changedItems, changedItems, index, oldIndex);
	}	
	
	public Action getAction() {
		return this.mAction;
	}
	
	public List<?> getNewItems() {
		return this.mNewItems;		
	}

	public List<?> getOldItems() {
		return this.mOldItems;		
	}	
	
	public int getNewStartingIndex() {
		return this.mNewStartingIndex;
	}
	
	public int getOldStartingIndex() {
		return this.mOldStartingIndex;
	}
	
	
	private void initAdd(Action action, List<?> newItems, int newStartingIndex) {
		this.mAction = action;
		
		this.mNewItems = null;
		if( newItems != null ) {
			// make this readonly
			ArrayList<Object> list = new ArrayList<Object>();
			list.addAll(newItems);
			Collections.unmodifiableList(list);
			this.mNewItems = list;
		}
		this.mNewStartingIndex = newStartingIndex;
	}
	
	private void initAddOrRemove(Action action, List<?> changedItems, int startingIndex) {
		if (action == Action.Add)
			this.initAdd(action, changedItems, startingIndex);
		else if (action == Action.Remove)
			this.initRemove(action, changedItems, startingIndex);
	}
	
	private void initRemove(Action action, List<?> oldItems, int oldStartingIndex) {
		this.mAction = action;
		this.mOldItems = null;
		if( oldItems != null ) {
			// make this readonly
			ArrayList<Object> list = new ArrayList<Object>();
			list.addAll(oldItems);
			Collections.unmodifiableList(list);
			this.mOldItems = list;
		}		
		this.mOldStartingIndex = oldStartingIndex;
	}

	private void initMoveOrReplace(Action action, List<?> newItems, List<?> oldItems, int startingIndex, int oldStartingIndex) {
		this.initAdd(action, newItems, startingIndex);
		this.initRemove(action, oldItems, oldStartingIndex);
	}

	
}
