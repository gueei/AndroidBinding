package gueei.binding.utility;

/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2001 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A simple list which holds only weak references to the original objects.
 * original_author  Martin Entlicher
 * Modified to Generic List, while the original is not Generic 
 */
public class WeakList<E> extends AbstractList<E> {
    
    private volatile ArrayList<WeakReference<E>> items;

    /** Creates new WeakList */
    public WeakList() {
        items = new ArrayList<WeakReference<E>>();
    }
    
    public WeakList(Collection<E> c) {
        items = new ArrayList<WeakReference<E>>();
        addAll(0, c);
    }

    public WeakList(int initCapcacity) {
        items = new ArrayList<WeakReference<E>>(initCapcacity);
    }

    
    public void add(int index, E element) {
    	synchronized(this){
    		items.add(index, new WeakReference<E>(element));
    	}
    }
    
    public Iterator<E> iterator() {
    	throw new UnsupportedOperationException();
//    	synchronized(this){
//    		return new WeakListIterator();
//    	}
    }
    
    public int size() {
    	synchronized(this){
	        removeReleased();
	        return items.size();
    	}
    }    
    
    public E get(int index) {
    	synchronized(this){
    		return ((WeakReference<E>) items.get(index)).get();
    	}
    }
    
    private void removeReleased() {
    	synchronized(this){
    		ArrayList<WeakReference<E>> removeList = new ArrayList<WeakReference<E>>();
	        for (Iterator<WeakReference<E>> it = items.iterator(); it.hasNext(); ) {
	            WeakReference<E> ref = (WeakReference<E>) it.next();
	            if (ref.get() == null) removeList.add(ref);
	        }
	        for(int i=0; i<removeList.size(); i++){
	        	items.remove(removeList.get(i));
	        }
    	}
    }
    
    
	public Object[] toArray() {
		synchronized(this){
	    	removeReleased();
	    	@SuppressWarnings("unchecked")
			WeakReference<E>[] itemArray = items.toArray(new WeakReference[0]);
	    	int len = itemArray.length;
	    	Object[] eArray = new Object[len];
	    	for(int i=0; i<len; i++){
	    		eArray[i] = itemArray[i].get();
	    	}
	    	return eArray;
		}
	}

	@Override
	public boolean remove(Object object) {
		synchronized(this){
			int len = items.size();
			for(int i=0; i<len; i++){
				if (items.get(i).get() == null){
					items.remove(i);
					return remove(object);
				}
				if (items.get(i).get().equals(object)){
					items.remove(i);
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean add(E object) {
		synchronized(this){
			return items.add(new WeakReference<E>(object));
		}
	}

	@SuppressWarnings("unchecked")
	public E[] toItemArray(E[] arr) {
		int size = this.size();
		E[] copy = (E[]) Array.newInstance(arr.getClass().getComponentType(), size);
		WeakReference<E>[] itemArray = items.toArray(new WeakReference[0]);
    	for(int i=0; i<size; i++){
    		copy[i] = itemArray[i].get();
    	}
		return copy;
	}
}
