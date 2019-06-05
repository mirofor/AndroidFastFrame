package com.fast.frame.dialog;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewConvertListener implements Parcelable {
    protected ViewConvertListener() {
    }
    protected ViewConvertListener(Parcel in) {
    }

    public static final Creator<ViewConvertListener> CREATOR = new Creator<ViewConvertListener>() {
        @Override
        public ViewConvertListener createFromParcel(Parcel in) {
            return new ViewConvertListener(in);
        }

        @Override
        public ViewConvertListener[] newArray(int size) {
            return new ViewConvertListener[size];
        }
    };

    public void convertView(ViewHolder holder, BaseNiceDialog dialog){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
