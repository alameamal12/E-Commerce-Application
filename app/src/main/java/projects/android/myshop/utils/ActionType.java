package projects.android.myshop.utils;

import android.os.Parcel;
import android.os.Parcelable;

// action type to know action is add or update or delete
public enum ActionType implements Parcelable {
    ADD, UPDATE_DELETE;

    public static final Creator<ActionType> CREATOR = new Creator<>() {
        @Override
        public ActionType createFromParcel(Parcel in) {
            return ActionType.values()[in.readInt()];
        }

        @Override
        public ActionType[] newArray(int size) {
            return new ActionType[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
