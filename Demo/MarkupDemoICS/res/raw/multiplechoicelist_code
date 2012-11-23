package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import android.util.SparseBooleanArray;

public class MultipleChoiceList {
	public final Observable<SparseBooleanArray> CheckedPositions 
		= new Observable<SparseBooleanArray>(SparseBooleanArray.class);
	
	public final ArrayListObservable<String> GenreList = 
		new ArrayListObservable<String>(String.class);

	private static final String[] GENRES = new String[] {
        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
    };
	
	public MultipleChoiceList(){
		GenreList.setArray(GENRES);
	}
}