package gueei.binding.v30.viewAttributes.absListView;

import android.util.SparseBooleanArray;

public class Helper {
	// helper method to clone Sparse Boolean Array
	public static SparseBooleanArray cloneSBArray(SparseBooleanArray arr){
		try{
			return arr.clone();
		}catch(Exception e){
			// Happens when using HC - should clone manually
			int size = arr.size();
			SparseBooleanArray clone = new SparseBooleanArray();
			for(int i=0; i<size; i++){
				if (arr.get(arr.keyAt(i))){
					clone.put(arr.keyAt(i), true);
				}
			}
			return clone;
		}
	}
}
