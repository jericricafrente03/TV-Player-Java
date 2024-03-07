package com.ph.bittelasia.libvlc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class Channels implements Parcelable {

    private ArrayList<VideoInfo> list;

    public Channels(ArrayList<VideoInfo> list)
    {
        this.list=list;
    }

    protected Channels(Parcel in) {
        this.list =  in.readArrayList(null);
    }

    public static final Creator<Channels> CREATOR = new Creator<Channels>() {
        @Override
        public Channels createFromParcel(Parcel in) {
            return new Channels(in);
        }

        @Override
        public Channels[] newArray(int size) {
            return new Channels[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
           parcel.writeList(list);
    }

    public Channels sort(){
        if(list!=null)
            Collections.sort(list);
        return this;
    }

    public ArrayList<VideoInfo> getList() {
        return list;
    }
}
