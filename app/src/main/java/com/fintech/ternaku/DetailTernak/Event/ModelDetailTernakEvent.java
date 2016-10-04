package com.fintech.ternaku.DetailTernak.Event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pandhu on 9/27/16.
 */

public class ModelDetailTernakEvent implements Parcelable{
    public String title;
    public String desciption;
    public boolean isExpanded;

    public ModelDetailTernakEvent(){}

    public ModelDetailTernakEvent(Parcel in){
        title = in.readString();
        desciption = in.readString();
        isExpanded = in.readInt() == 1;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(title);
        dest.writeString(desciption);
        dest.writeInt(isExpanded ? 1 : 0);
    }

    public static final Creator<ModelDetailTernakEvent> CREATOR = new Creator<ModelDetailTernakEvent>(){
        @Override
        public ModelDetailTernakEvent createFromParcel(Parcel source){
            return new ModelDetailTernakEvent(source);
        }

        @Override
        public ModelDetailTernakEvent[] newArray(int size) {
            return new ModelDetailTernakEvent[size];
        }

    };
}