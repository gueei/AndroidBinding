package gueei.binding.markupDemoICS.viewModels;

import android.os.Parcel;
import android.os.Parcelable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class RawEntry implements Parcelable {
	public final StringObservable Title = new StringObservable();

	public final IntegerObservable ResId = new IntegerObservable();
	
	public final StringObservable Type = new StringObservable();

	public RawEntry(String title, int resId, String type) {
		Title.set(title);
		ResId.set(resId);
		Type.set(type);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(Title.get());
		out.writeInt(ResId.get());
		out.writeString(Type.get());
	}

	public static final Parcelable.Creator<RawEntry> CREATOR = new Parcelable.Creator<RawEntry>() {
		public RawEntry createFromParcel(Parcel in) {
			return new RawEntry(in.readString(), in.readInt(), in.readString());
		}

		public RawEntry[] newArray(int size) {
			return new RawEntry[size];
		}
	};
}
