package com.gueei.demos.markupDemo.viewModels;

import gueei.binding.TwoWayDependentObservable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;

public class SingleChoiceList {
	public final IntegerObservable CheckedPosition = new IntegerObservable(4);
	
	public final ArrayListObservable<String> GenreList = 
		new ArrayListObservable<String>(String.class);

	private static final String[] GENRES = new String[] {
        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
    };
	
	public SingleChoiceList(){
		GenreList.setArray(GENRES);
	}
	
	public final TwoWayDependentObservable<Float> SeekPosition =
			new TwoWayDependentObservable<Float>(Float.class, CheckedPosition){
			@Override
			public boolean ConvertBack(Object value, Object[] outResult) {
				outResult[0] = Math.round((Float)value * GenreList.size());
				return true;
			}

			@Override
			public Float calculateValue(Object... args) throws Exception {
				return (float)CheckedPosition.get() / (float)GenreList.size(); 
			}
	};
}