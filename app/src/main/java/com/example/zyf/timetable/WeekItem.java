package com.example.zyf.timetable;

import android.os.Parcel;
import android.os.Parcelable;

public class WeekItem implements Parcelable {
    private String weekNum;
    private boolean lit;

    public WeekItem(String num, boolean lit){
        weekNum = num;
        this.lit = lit;
    }

    public WeekItem(){

    }

    protected WeekItem(Parcel in) {
        weekNum = in.readString();
        lit = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weekNum);
        dest.writeByte((byte) (lit ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeekItem> CREATOR = new Creator<WeekItem>() {
        @Override
        public WeekItem createFromParcel(Parcel in) {
            return new WeekItem(in);
        }

        @Override
        public WeekItem[] newArray(int size) {
            return new WeekItem[size];
        }
    };

    public String getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(String weekNum) {
        this.weekNum = weekNum;
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
    }

}
